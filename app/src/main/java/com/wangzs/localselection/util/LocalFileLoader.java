package com.wangzs.localselection.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wangzs.localselection.R;
import com.wangzs.localselection.bean.FileInfo;
import com.wangzs.localselection.bean.SubItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 2018/6/28.
 */

public class LocalFileLoader {

    private SubItem excelItem, pdfItem, wordItem, PPTItem;
    private List<FileInfo> fileInfoList;
    private List<String> md5List;
    private Context context;

    public LocalFileLoader(final Context context, final IReadCallBack iReadCallBack) {
        this.context = context;
        fileInfoList = new ArrayList<>();
        md5List = new ArrayList<>();
//        readFile(context);
//        readAPPFile();
        getFileList(iReadCallBack);
        wordItem = new SubItem("DOC", 1);
        excelItem = new SubItem("XLS", 2);
        pdfItem = new SubItem("PDF", 3);
        PPTItem = new SubItem("PPT", 4);

    }

    String[] projection = new String[]{
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns._ID
    };

    public Observable<List<FileInfo>> readAPPFile() {
        final List<File> m = new ArrayList<>();
//        m.add(new File(FileAccessor.CLOUD_DISK));
//        m.add(new File(FileAccessor.IMESSAGE_FILE));
        m.add(new File(FileAccessor.EXTERNAL_STOREPATH + "/tencent/QQfile_recv"));
        m.add(new File(FileAccessor.EXTERNAL_STOREPATH + "/tencent/MicroMsg/Download"));

        return Observable.just(context).map(new Func1<Context, List<FileInfo>>() {
            @Override
            public List<FileInfo> call(Context context) {
                final List<FileInfo> list = new ArrayList<>();
                Observable.from(m)
                        .flatMap(new Func1<File, Observable<File>>() {
                            @Override
                            public Observable<File> call(File file) {
                                return FileUtil.listFiles(file);

                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(File files) {
                        FileInfo fileInfoFromFile = FileUtil.getFileInfoFromFile(files);
                        list.add(fileInfoFromFile);
                    }
                });
                return list;
            }
        });

    }

    public Observable<List<FileInfo>> readFile() {
        return Observable.just(context).map(new Func1<Context, List<FileInfo>>() {
            @Override
            public List<FileInfo> call(Context context) {
                List<FileInfo> list = new ArrayList<>();
                Uri uri = MediaStore.Files.getContentUri("external");
                String selection = buildDocSelection();
                String sortOrder = MediaStore.Files.FileColumns.MIME_TYPE + " asc, " + MediaStore.Files.FileColumns.TITLE + " asc";

                if (uri == null) {
                    return list;
                }

                ContentResolver resolver = context.getContentResolver();
                Cursor cursor = resolver.query(uri, projection, selection, null, sortOrder);
                if (cursor != null) {
                    if (cursor.moveToLast()) {
                        do {
                            String data = cursor.getString(0);
                            String title = cursor.getString(1);
                            File file = new File(data);
                            if (!file.exists()) {
                                continue;
                            }
                            long size = cursor.getLong(2);
                            long modifiedDate = cursor.getLong(3);
                            int id = cursor.getInt(4);
                            int positionDo = data.lastIndexOf(".");
                            if (positionDo == -1) {
                                continue;
                            }
                            String type = data.substring(positionDo + 1, data.length());
                            Log.e("file's localPath is ----", data);
                            FileInfo fileInfo = new FileInfo();
                            fileInfo.setFileName(title + "." + type);
                            fileInfo.setFilePath(data);
                            fileInfo.setFileSize(size);
                            fileInfo.setTime(FileUtil.getFileLastModifiedTime(modifiedDate));
                            fileInfo.setSuffix(type);
                            fileInfo.setType(type);
                            fileInfo.setMd5(FileUtil.getFileMD5Value(file));
                            list.add(fileInfo);

                        } while (cursor.moveToPrevious());
                    }
                    cursor.close();

                }
                return list;
            }
        });
    }

    private void getFileList(final IReadCallBack callBack) {
        final List<MultiItemEntity> list = new ArrayList<>();
       Observable.zip(readAPPFile(), readFile(), new Func2<List<FileInfo>, List<FileInfo>, List<FileInfo>>() {
           @Override
           public List<FileInfo> call(List<FileInfo> fileInfoList, List<FileInfo> fileInfoList2) {
               fileInfoList.addAll(fileInfoList2);
               return fileInfoList;
           }
       }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<FileInfo>>() {
           @Override
           public void call(List<FileInfo> fileInfoList) {
               List<FileInfo> fileInfos = removeByMd5(fileInfoList);
               if (fileInfos.size()>0){
                   for (int i = 0; i < fileInfos.size(); i++) {
                       FileInfo fileInfo = fileInfos.get(i);
                       if (FileUtil.checkSuffix(fileInfo.getFilePath(), new String[]{"doc", "docx", "dot"})) {
                           fileInfo.setFileImage(R.drawable.chatting_item_file_doc);
                           wordItem.addSubItem(fileInfo);
                       } else if (FileUtil.checkSuffix(fileInfo.getFilePath(), new String[]{"xls,xlsx"})) {
                           fileInfo.setFileImage(R.drawable.chatting_item_file_xls);
                           excelItem.addSubItem(fileInfo);
                       } else if (FileUtil.checkSuffix(fileInfo.getFilePath(), new String[]{"pdf"})) {
                           fileInfo.setFileImage(R.drawable.chatting_item_file_pdf);
                           pdfItem.addSubItem(fileInfo);
                       } else if (FileUtil.checkSuffix(fileInfo.getFilePath(), new String[]{"ppt", "pptx"})) {
                           fileInfo.setFileImage(R.drawable.chatting_item_file_ppt);
                           PPTItem.addSubItem(fileInfo);
                       }
                   }
                   if (wordItem.getSubItems() != null) {
                       list.add(wordItem);
                   }
                   if (excelItem.getSubItems() != null) {
                       list.add(excelItem);
                   }
                   if (pdfItem.getSubItems() != null) {
                       list.add(pdfItem);
                   }
                   if (PPTItem.getSubItems() != null) {
                       list.add(PPTItem);
                   }
                   if (list.size()>0){
                       callBack.complete(list);
                   }else {
                       callBack.fail();
                   }
               }else {
                   callBack.fail();
               }
           }
       });
    }


    private String buildDocSelection() {
        StringBuilder selection = new StringBuilder();
        for (String aSDocMimeTypesSet : sDocMimeTypesSet) {
            selection.append("(" + MediaStore.Files.FileColumns.MIME_TYPE + "=='")
                    .append(aSDocMimeTypesSet)
                    .append("') OR ");
        }
        return selection.substring(0, selection.lastIndexOf(")") + 1);
    }


    public static HashSet<String> sDocMimeTypesSet = new HashSet<String>() {

        {
            add("application/msword");
            add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            add("application/x-dot");
            add("application/pdf");
            add("application/vnd.ms-powerpoint");
            add("application/vnd.openxmlformats-officedocument.presentationml.presentation");
            add("application/x-cprplayer");
            add("application/vnd.ms-excel");
            add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            add("application/mspowerpoint");
            add("application/ms-powerpoint");
            add("application/powerpoint");
            add("application/x-powerpoint");
            add("application/mspowerpnt");
            add("application/vnd-mspowerpoint");
            add("application/x-mspowerpoint");
            add("application/x-m");
            add("application/vnd.ms-works");
        }
    };

    public interface IReadCallBack {
        void complete(List<MultiItemEntity> map);

        void fail();
    }
    public static List<FileInfo> removeByMd5(List<FileInfo> persons) {
        Set<FileInfo> personSet = new TreeSet<>(new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo o1, FileInfo o2) {
                return TextUtils.isEmpty(o1.getMd5()) ? -1 : o1.getMd5().compareTo(o2.getMd5());
            }
        });
        personSet.addAll(persons);
        return new ArrayList<>(personSet);
    }
}
