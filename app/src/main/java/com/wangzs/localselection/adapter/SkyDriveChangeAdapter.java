package com.wangzs.localselection.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;


import java.util.List;


public class SkyDriveChangeAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public SkyDriveChangeAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
    //防止重新销毁视图
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "全部";
        } else if (position == 1) {
            return "照片";
        } else if (position == 2) {
            return "文件";}
        else{
                return "全部";
            }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        //super.destroyItem(container, position, object);
    }
}

