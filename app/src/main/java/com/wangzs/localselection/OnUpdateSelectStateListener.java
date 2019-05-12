package com.wangzs.localselection;


public interface OnUpdateSelectStateListener {

    void setOnShowSelectSize(boolean flag, String path, long size);

    boolean isMaxCount();

    int getMaxCount();
}
