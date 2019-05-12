package com.wangzs.localselection.bean;


import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Admin on 2017/11/10.
 */

public class SubItem extends AbstractExpandableItem implements MultiItemEntity {

    public String title;

    public int getConferenceType() {
        return conferenceType;
    }

    public void setConferenceType(int conferenceType) {
        this.conferenceType = conferenceType;
    }

    private int conferenceType;

    public SubItem(String title, int conferenceType) {
        this.title = title;
        this.conferenceType = conferenceType;
    }


    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
