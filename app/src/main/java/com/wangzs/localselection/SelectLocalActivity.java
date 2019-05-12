package com.wangzs.localselection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.flyco.tablayout.SlidingTabLayout;
import com.wangzs.localselection.adapter.SkyDriveChangeAdapter;
import com.wangzs.localselection.fragment.LocalDocumentFragment;
import com.wangzs.localselection.fragment.LocalPhotoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class SelectLocalActivity extends AppCompatActivity implements OnUpdateSelectStateListener {
    public ProgressDialog dialog;
    private String TAG = "SelectLocalActivity";
    private TextView tv_send;
    private ViewPager viewPager;
    private int MAX_COUNT = 9;


    public static final String SELECT_TYPY = "SelectLocalActivity.select_file_type";
    public static final String SELECT_MAX_COUNT = "SelectLocalActivity.is_wbss_select_max_count";
    public static final String TASKID_ARRAY = "SelectLocalActivity.uopload_taskId_list";
    public static final String UPLOAD_FILE_LIST = "upload_files_list";
    private ArrayList<String> pathList;

    private int select_type;
    private SkyDriveChangeAdapter pagerAdapter;
    private long fileSize = 0;
    private TextView tv_all_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_local);
//        setActionBarTitle("选择本地文件");
        select_type = getIntent().getIntExtra(SELECT_TYPY, 0);
        MAX_COUNT = getIntent().getIntExtra(SELECT_MAX_COUNT, 9);
        initView();

    }



    private void initView() {

        pathList = new ArrayList<>();

        tv_all_size = (TextView) findViewById(R.id.tv_all_size);
        tv_send = (TextView) findViewById(R.id.tv_send);
        TextView tv_preview = (TextView) findViewById(R.id.tv_preview);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.local_tablelayout);
        viewPager = (ViewPager) findViewById(R.id.vp_myfile);
        updateSizAndCount();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalDocumentFragment());
        fragments.add(new LocalPhotoFragment());

        pagerAdapter = new SkyDriveChangeAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(viewPager, mTitles);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                if (!NetWorkUtils.IsNetWorkEnable(SelectLocalActivity.this)) {
//                    return;
//                }
//                if (!NetWorkUtils.isWifiAvailable(SelectLocalActivity.this)) {
//                    createUpLoadDialog();
//                } else {
//                    uploadFile();
//                }
            }
        });
    }

    private final String[] mTitles = {"文档", "照片"};

    public void updateSizAndCount() {

        if (pathList.size() == 0) {

            tv_send.setBackgroundResource(R.drawable.shape_bt_send);
            tv_send.setTextColor(getResources().getColor(R.color.md_grey_700));
            tv_send.setEnabled(false);
            tv_all_size.setText("已选" + "0/" + MAX_COUNT + "(" + fileSize + ")");
        } else {
            tv_all_size.setText("已选" + pathList.size() + "/" + MAX_COUNT + "(" + fileSize + ")");
            tv_send.setEnabled(true);
            tv_send.setBackgroundResource(R.drawable.shape_bt_send_blue);
            tv_send.setTextColor(getResources().getColor(R.color.white));
        }
        tv_send.setText(getString(R.string.upload_tips, "" + pathList.size() + "/" + MAX_COUNT));
    }


    @Override
    public void setOnShowSelectSize(boolean flag, String path, long size) {
        if (flag) {
            pathList.add(path);
            fileSize += size;
        } else {
            pathList.remove(path);
            fileSize -= size;
        }
        updateSizAndCount();
    }

    @Override
    public boolean isMaxCount() {
        if (pathList == null) return false;
        return pathList.size() >= MAX_COUNT;
    }


    @Override
    public int getMaxCount() {
        return MAX_COUNT;
    }


//    private void createUpLoadDialog() {
//        RXAlertDialog dialog = new RXAlertDialog.Builder(this)
//                .setMessage("非wifi网络上传会消耗您的数据流量，是否确定上传下载")
//                .setNegativeButton(R.string.app_cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        uploadFile();
//                        dialog.dismiss();
//
//                    }
//                })
//                .create();
//        dialog.show();
//    }

//    private void uploadFile() {
//        if (fileSize > 1024 * 1024 * 20) {
//            createOverSizeLog();
//            return;
//        }
//        Intent intent = new Intent();
//        if (pathList.size() > 0) {
//            switch (select_type) {
//                case 0:
//                    intent.putStringArrayListExtra("choosed_file_path_list", pathList);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
//                    break;
//                case 1:
//                    intent.setAction(UPLOAD_FILE_LIST);
//                    createUpTasks(pathList);
//                    intent.putExtra(TASKID_ARRAY, taskID);
//                    RongXinApplicationContext.sendBroadcast(intent);
//                    ConfToasty.success("添加到上传列表");
//                    finish();
//                    break;
//                case 2:
//                    intent.putStringArrayListExtra("upload_doc_list", pathList);
//                    createUpTasks(pathList);
//                    intent.putExtra(TASKID_ARRAY, taskID);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    break;
//                case 3:
//                    intent.putExtra("plugin_select_file_result", pathList.get(0));
//                    setResult(RESULT_OK, intent);
//                    finish();
//                    break;
//            }
//        }
//    }

//    private void createOverSizeLog() {
//        RXAlertDialog dialog = new RXAlertDialog.Builder(this)
//                .setMessage("文件大小超过限制，最大不能超过20M")
//                .setPositiveButton(R.string.app_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .create();
//        dialog.showButton(DialogInterface.BUTTON_NEGATIVE, false);
//        dialog.show();
//    }


    private String[] taskID;

//    private void createUpTasks(List<String> pathList) {
//        if (tasks == null)
//            tasks = new ArrayList<>();
//        if (taskID == null) {
//            taskID = new String[pathList.size()];
//        }
//        for (int i = 0; i < pathList.size(); i++) {
//            String path = pathList.get(i);
//            final String uuid = java.util.UUID.randomUUID().toString();
//            File file = new File(path);
//            if (!file.exists()) {
//                continue;
//            }
//            taskID[i] = uuid;
//            ITask builder = new Task.Builder()
//                    .setTaskType(TaskType.TYPE_HTTP_UPLOAD)
//                    .setName(file.getName())
//                    .setSourceUrl(path)
//                    .setState(TaskState.STATE_UNKOWN)
//                    .setTaskId(uuid)
//                    .setLength(file.length())
//                    .setSessionId(uuid)
//                    .setGroupId(TextUtil.isEmpty(confId) ? ConstantUtils.DEFAULT_GROUP_ID : confId)
//                    .setFilePubId(TextUtils.isEmpty(confId) ? ConstantUtils.DEFAULT_FILEPUBID : ConstantUtils.DEFAULT_CONF_FILEPUBID)
//                    .setcreatedAt(DateUtil.getNowZoneData())
//                    .setUserId(AppMgr.getUserId())
//                    .setDestUrl(TextUtil.isEmpty(confId) ? ConstantUtils.SKYDRIVE_UPLOAD_URL + AppMgr.getUserId() : confUploadUrl())
//                    .build();
//            tasks.add(builder);
//        }
//        TaskCmd cmd = new TaskCmd.Builder()
//                .setTasks(tasks)
//                .setTaskType(TaskType.TYPE_HTTP_UPLOAD)
//                .setProcessType(ProcessType.TYPE_ADD_TASKS)
//                .setUserId(AppMgr.getUserId())
//                .build();
//        TaskEventBus.getDefault().execute(cmd);
//    }
//
//
//    private String confUploadUrl() {
//        return ConstantUtils.CONF_UPLOAD_URL + AppMgr.getUserId() + "&ConfId=" + confId + "&HistoryConf=" + historyConf;
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return super.onKeyDown(keyCode, event);
//    }
}
