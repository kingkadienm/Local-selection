package com.wangzs.localselection.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class FileInfo implements Parcelable, MultiItemEntity {

    public static final int WORLD = 0;
    public static final int PPT = 1;
    public static final int PDF = 2;
    public static final int EXCEL = 3;


    private String fileName;
    private String filePath;
    private long fileSize;
    private boolean isDirectory;
    private String suffix;
    private String time;
    private String md5;

    protected FileInfo(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        fileSize = in.readLong();
        isDirectory = in.readByte() != 0;
        suffix = in.readString();
        time = in.readString();
        md5 = in.readString();
        isCheck = in.readByte() != 0;
        isPhoto = in.readByte() != 0;
        type = in.readString();
        fileImage = in.readInt();
    }

    public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
        @Override
        public FileInfo createFromParcel(Parcel in) {
            return new FileInfo(in);
        }

        @Override
        public FileInfo[] newArray(int size) {
            return new FileInfo[size];
        }
    };

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    boolean isCheck = false;
    private boolean isPhoto;




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public int getFileImage() {
        return fileImage;
    }

    public void setFileImage(int fileImage) {
        this.fileImage = fileImage;
    }

    private int fileImage;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }


    public boolean getIsDirectory() {
        return this.isDirectory;
    }

    public void setIsDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public boolean getIsCheck() {
        return this.isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public FileInfo() {

    }

    public void setIsPhoto(boolean isPhoto) {
        this.isPhoto = isPhoto;
    }

    public boolean getIsPhoto() {
        return isPhoto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(fileName);
        parcel.writeString(filePath);
        parcel.writeLong(fileSize);
        parcel.writeByte((byte) (isDirectory ? 1 : 0));
        parcel.writeString(suffix);
        parcel.writeString(time);
        parcel.writeString(md5);
        parcel.writeByte((byte) (isCheck ? 1 : 0));
        parcel.writeByte((byte) (isPhoto ? 1 : 0));
        parcel.writeString(type);
        parcel.writeInt(fileImage);
    }


    @Override
    public int getItemType() {
        return 2;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", md5='" + md5 + '\'' +
                '}';
    }
}
