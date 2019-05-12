package com.wangzs.localselection.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;


import com.wangzs.localselection.bean.FileInfo;
import com.wangzs.localselection.bean.LocalDocSection;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Admin on 2017/11/9.
 */

public class FileUtil {
    public static final int DOC = 0x1;
    public static final int XLS = 0x2;
    public static final int TXT = 0x3;
    public static final int PPT = 0x4;
    public static final int UNKNOW = 0x5;
    public static final int PDF = 0x6;
    public static final int PNG = 0x7;

    //Cursor cursor = cr.query(imageUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or " + MediaStore.Images.Media.MIME_TYPE + " = ? ",
    //                            new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

    public static List<LocalDocSection> getSpecificTypeOfFile(Context context, String[] extension) {
        Uri fileUri = MediaStore.Files.getContentUri("external");
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATE_MODIFIED,
                MediaStore.Files.FileColumns._ID
        };
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        List<LocalDocSection> specificTypeOfFile = new ArrayList<>();
        //按时间递增顺序对结果进行排序;待会从后往前移动游标就可实现时间递减
        String sortOrder = MediaStore.Files.FileColumns.DATE_MODIFIED;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(fileUri, projection, selection, null, sortOrder);
        if (cursor != null) {
            if (cursor.moveToLast()) {
                do {
                    String data = cursor.getString(0);
                    String title = cursor.getString(1);
                    long size = cursor.getLong(2);
                    long modified_date = cursor.getLong(3);
                    int id = cursor.getInt(4);
                    int position_do = data.lastIndexOf(".");
                    if (position_do == -1) continue;
                    String type = data.substring(position_do + 1, data.length());
                    FileInfo fileInfo = new FileInfo();

//                    document.setUrl(data);
//                    document.setTYPE(doc_type);
//                    document.setMimeType(modified_date);
//                    document.setSize(size);
//                    document.setId(id);
//                    String path = data.substring(data.lastIndexOf("/") + 1);
//                    document.setDisplayName(path);
                    fileInfo.setFileName(title + "." + type);
                    fileInfo.setFilePath(data);
                    fileInfo.setFileSize(size);
                    fileInfo.setTime(getFileLastModifiedTime(modified_date));
                    fileInfo.setSuffix(type);
                    fileInfo.setType(type);
                    LocalDocSection content = new LocalDocSection(fileInfo);
                    specificTypeOfFile.add(content);
                } while (cursor.moveToPrevious());
            }
            cursor.close();
        }
        return specificTypeOfFile;
    }


    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String getFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }

    public static String FormentFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 字符串时间戳转时间格式
     *
     * @param timeStamp
     * @return
     */
    public static String getStrTime(String timeStamp) {
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
        long l = Long.valueOf(timeStamp) * 1000;
        timeString = sdf.format(new Date(l));
        return timeString;
    }

    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    /**
     * 读取文件的最后修改时间的方法
     */
    public static String getFileLastModifiedTime(long f) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(f);
        return formatter.format(cal.getTime());
    }

    /**
     * 获取扩展内存的路径
     *
     * @param mContext
     * @return
     */
    public static String getStoragePath(Context mContext) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean checkSuffix(String fileName,
                                      String[] fileSuffix) {
        for (String suffix : fileSuffix) {
            if (fileName != null) {
                if (fileName.toLowerCase().contains(suffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 文件过滤,将手机中隐藏的文件给过滤掉
     */
    public static File[] fileFilter(File file) {
        File[] files = file.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isHidden();
            }
        });
        return files;
    }


    public static List<FileInfo> getFilesInfo(List<String> fileDir, Context mContext) {
        List<FileInfo> mlist = new ArrayList<>();
        for (int i = 0; i < fileDir.size(); i++) {
            if (new File(fileDir.get(i)).exists()) {
                mlist = FilesInfo(new File(fileDir.get(i)), mContext);
            }
        }
        return mlist;
    }

    private static List<FileInfo> FilesInfo(File fileDir, Context mContext) {
        List<FileInfo> videoFilesInfo = new ArrayList<>();
        File[] listFiles = fileFilter(fileDir);
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.isDirectory()) {
                    FilesInfo(file, mContext);
                } else {
                    FileInfo fileInfo = getFileInfoFromFile(file);
                    videoFilesInfo.add(fileInfo);
                }
            }
        }
        return videoFilesInfo;
    }

    public static List<FileInfo> getFileInfosFromFileArray(File[] files) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (File file : files) {
            FileInfo fileInfo = getFileInfoFromFile(file);
            fileInfos.add(fileInfo);
        }
        Collections.sort(fileInfos, new FileNameComparator());
        return fileInfos;
    }

    /**
     * 根据文件名进行比较排序
     */
    public static class FileNameComparator implements Comparator<FileInfo> {
        protected final static int
                FIRST = -1,
                SECOND = 1;

        @Override
        public int compare(FileInfo lhs, FileInfo rhs) {
            if (lhs.isDirectory() || rhs.isDirectory()) {
                if (lhs.isDirectory() == rhs.isDirectory())
                    return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
                else if (lhs.isDirectory()) return FIRST;
                else return SECOND;
            }
            return lhs.getFileName().compareToIgnoreCase(rhs.getFileName());
        }
    }

    public static FileInfo getFileInfoFromFile(File file) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(file.getName());
        fileInfo.setFilePath(file.getPath());
        fileInfo.setFileSize(file.length());
        fileInfo.setDirectory(file.isDirectory());
        fileInfo.setTime(getFileLastModifiedTime(file));
        fileInfo.setMd5(getFileMD5Value(file));
        int lastDotIndex = file.getName().lastIndexOf(".");
        if (lastDotIndex > 0) {
            String fileSuffix = file.getName().substring(lastDotIndex + 1);
            fileInfo.setSuffix(fileSuffix);
        }
        return fileInfo;
    }


    public static List<FileInfo> queryFilerInfo(Context context, List<Uri> mlist, String selection, String[] selectionArgs) {
        List<FileInfo> fileInfos = new ArrayList<>();
        for (int i = 0; i < mlist.size(); i++) {
            String[] projection = new String[]{
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.TITLE,
                    MediaStore.Files.FileColumns.DATE_MODIFIED
            };
            Cursor cursor = context.getContentResolver().query(
                    mlist.get(i),
                    projection, selection,
                    selectionArgs, projection[2] + " DESC");

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int dataindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    int nameindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.TITLE);
                    int timeindex = cursor
                            .getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED);
                    do {
                        FileInfo fileInfo = new FileInfo();
                        String path = cursor.getString(dataindex);
                        String name = cursor.getString(nameindex);
                        String time = cursor.getString(timeindex);
                        fileInfo.setFileSize(new File(path).length());
                        fileInfo.setFilePath(path);
                        fileInfo.setFileName(name);
                        fileInfo.setTime(time);
                        fileInfos.add(fileInfo);

                    } while (cursor.moveToNext());
                }
            }
            cursor.close();
        }
        return fileInfos;

    }

    public static void ScanFile(Context context) {
        String[] path = new String[]{Environment.getExternalStorageDirectory().toString()};
        MediaScannerConnection.scanFile(context, path, mimeType, null);
    }

    public static void saveMediaScanner() {
//        MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);

    }

    private static String[] mimeType = new String[]{
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/x-dot",
            "application/pdf",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "application/x-cprplayer",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "application/mspowerpoint",
            "application/ms-powerpoint",
            "application/powerpoint",
            "application/x-powerpoint",
            "application/mspowerpnt",
            "application/vnd-mspowerpoint",
            "application/x-mspowerpoint",
            "application/x-m",
            "application/vnd.ms-works",
    };


    public static Observable<File> listFiles(final File f) {
        if (f.isDirectory()) {
            return Observable.from(f.listFiles()).flatMap(new Func1<File, Observable<File>>() {
                @Override
                public Observable<File> call(File file) {
                    return listFiles(file);
                }
            });
        } else {
            return Observable.just(f).filter(new Func1<File, Boolean>() {
                @Override
                public Boolean call(File file) {
                    return f.exists() && f.canRead() && FileUtil.checkSuffix(f.getAbsolutePath(), new String[]{"doc", "docx", "dot", "xls", "pdf", "ppt", "pptx", "txt"});
                }
            });
        }
    }

    public static String getFileMD5Value(File file) {
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[512 * 1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 如果为空则返回控制付出
     * @param value 源字符串
     * @return 结果
     */
    public static String nullAsNil(String value) {
        return isNullOrNil(value) ? "" : value;

    }
    /**
     * 判断是否为空
     * @param value 判断字符串
     * @return 是否空
     */
    public static boolean isNullOrNil(String value) {
        return !((value != null) && (value.length() > 0));
    }
    public static String getFormatSize(long size) {
        long kiloByte = size/1024;
        if(kiloByte < 1) {
            return size + "B";
        }

        long megaByte = kiloByte/1024;
        if(megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        long gigaByte = megaByte/1024;
        if(gigaByte < 1) {
            BigDecimal result2  = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        long teraBytes = gigaByte/1024;
        if(teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * dip转化像素
     * @param dipValue
     * @return
     */
    public static int dip2px(float dipValue){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);

    }
}

