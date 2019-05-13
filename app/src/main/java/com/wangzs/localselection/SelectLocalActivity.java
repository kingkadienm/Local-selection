package com.wangzs.localselection;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.flyco.tablayout.SlidingTabLayout;
import com.wangzs.localselection.adapter.SkyDriveChangeAdapter;
import com.wangzs.localselection.fragment.LocalDocumentFragment;
import com.wangzs.localselection.fragment.LocalPhotoFragment;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class SelectLocalActivity extends AppCompatActivity implements OnUpdateSelectStateListener, EasyPermissions.PermissionCallbacks {
    private TextView tvSend;
    private ViewPager viewPager;
    private int maxCount = 9;


    public static final String SELECT_TYPY = "SelectLocalActivity.select_file_type";
    public static final String SELECT_MAX_COUNT = "SelectLocalActivity.is_wbss_select_max_count";
    private ArrayList<String> pathList;

    private SkyDriveChangeAdapter pagerAdapter;
    private long fileSize = 0;
    private TextView tvAllSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_local);
        maxCount = getIntent().getIntExtra(SELECT_MAX_COUNT, 9);
        getPermission();

    }

    private void getPermission() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            initView();
        } else {
            EasyPermissions.requestPermissions(this, "读取存储卡权限", 0x11, Manifest.permission.READ_EXTERNAL_STORAGE);

        }
    }

    private void initView() {

        pathList = new ArrayList<>();

        tvAllSize = (TextView) findViewById(R.id.tv_all_size);
        tvSend = (TextView) findViewById(R.id.tv_send);
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.local_tablelayout);
        viewPager = (ViewPager) findViewById(R.id.vp_myfile);
        updateSizAndCount();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LocalDocumentFragment());
        fragments.add(new LocalPhotoFragment());

        pagerAdapter = new SkyDriveChangeAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        slidingTabLayout.setViewPager(viewPager, mTitles);

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
            }
        });
    }

    private final String[] mTitles = {"文档", "照片"};

    public void updateSizAndCount() {

        if (pathList.size() == 0) {

            tvSend.setBackgroundResource(R.drawable.shape_bt_send);
            tvSend.setTextColor(getResources().getColor(R.color.md_grey_700));
            tvSend.setEnabled(false);
            tvAllSize.setText("已选" + "0/" + maxCount + "(" + fileSize + ")");
        } else {
            tvAllSize.setText("已选" + pathList.size() + "/" + maxCount + "(" + fileSize + ")");
            tvSend.setEnabled(true);
            tvSend.setBackgroundResource(R.drawable.shape_bt_send_blue);
            tvSend.setTextColor(getResources().getColor(R.color.white));
        }
        tvSend.setText(getString(R.string.upload_tips, "" + pathList.size() + "/" + maxCount));
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
        if (pathList == null) {
            return false;
        }
        return pathList.size() >= maxCount;
    }


    @Override
    public int getMaxCount() {
        return maxCount;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 0x11) {
            initView();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要获取读取内存卡储存权限")
                .setPositiveButton("打开", (dialog2, which) -> {
                    startActivity(new Intent(Settings.ACTION_APPLICATION_SETTINGS));
                })
                .setNegativeButton("取消", (dialog3, which) -> {
                    dialog3.dismiss();
                })
                .create();
        dialog.show();
    }

}
