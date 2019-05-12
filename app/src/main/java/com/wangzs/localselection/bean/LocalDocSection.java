package com.wangzs.localselection.bean;


import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @description:
 * @author: wangzs
 * @date: 2019-05-12 23:27
 * @version:
 *
*/
public class LocalDocSection extends SectionEntity<FileInfo> {


    public LocalDocSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public LocalDocSection(FileInfo fileInfos) {
        super(fileInfos);
    }


}