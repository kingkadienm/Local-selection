/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.wangzs.localselection.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * 文件操作工具类
 * Created by Jorstin on 2015/3/17.
 */
public class FileAccessor {


    public static final String TAG = FileAccessor.class.getName();
    public static String EXTERNAL_STOREPATH = getExternalStorePath();
    public static String APPS_ROOT_DIR;
    public static String EXPORT_DIR;
    public static String CAMERA_PATH;
    public static String TACK_PIC_PATH;
    public static String IMESSAGE_VOICE;
    public static String IMESSAGE_IMAGE;
    public static String IMESSAGE_AVATAR;
    public static String IMESSAGE_FILE;
    public static String IMESSAGE_VIDEO;
    public static String LOCAL_PATH;
    public static String MULTI_MESSAGE_PATH;
    public static String WEB_DIR_PATH;
    public static String TEMP_DIR_PATH;
    public static String MEDIA_DEBUG_PATH;
    public static String CLOUD_DISK;

    /**
     * 初始化应用文件夹目录
     */
    public static void initFileAccess() {
        initFilePath();

        File rootDir = new File(APPS_ROOT_DIR);
        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        File imessageDir = new File(IMESSAGE_VOICE);
        if (!imessageDir.exists()) {
            imessageDir.mkdir();
        }

        File imageDir = new File(IMESSAGE_IMAGE);
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }

        File fileDir = new File(IMESSAGE_FILE);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        //  添加隐藏文件  隐藏图片
        File nomedia = new File(IMESSAGE_IMAGE + "/.nomedia");
        if (!nomedia.exists()) {
            try {
                Log.e("createNewFile", "createNewFile" + nomedia.getAbsolutePath());
                nomedia.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("createNewFile", "createNewFile" + e.toString());
            }
        }
        File avatarDir = new File(IMESSAGE_AVATAR);
        if (!avatarDir.exists()) {
            avatarDir.mkdir();
        }
        File videoDir = new File(IMESSAGE_VIDEO);
        if (!videoDir.exists()) {
            videoDir.mkdir();
        }
        //  添加隐藏文件  隐藏视频
        File nomedia_video = new File(IMESSAGE_VIDEO + "/.nomedia");
        if (!nomedia_video.exists()) {
            try {
                Log.d("createNewFile", "nomedia_video" + nomedia.getAbsolutePath());
                nomedia_video.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        File mediaDebugDir = new File(MEDIA_DEBUG_PATH);
        if (!mediaDebugDir.exists()) {
            mediaDebugDir.mkdir();
        }
        File cloudDisk = new File(CLOUD_DISK);
        if (!cloudDisk.exists()) {
            cloudDisk.mkdir();
        }
    }

    /**
     * 初始化应用文件夹路径
     */
    private static void initFilePath() {
//        APPS_ROOT_DIR = EXTERNAL_STOREPATH + File.separator + RXConfig.FLAVOR;
//        EXPORT_DIR = APPS_ROOT_DIR + File.separator + "Youhui_IM";
//        CAMERA_PATH = EXTERNAL_STOREPATH + File.separator + "DCIM" + File.separator + RXConfig.FLAVOR;
//        TACK_PIC_PATH = APPS_ROOT_DIR + File.separator + ".tempchat";
//        IMESSAGE_VOICE = APPS_ROOT_DIR + File.separator + "voice";
//        IMESSAGE_IMAGE = APPS_ROOT_DIR + File.separator + "image";
//        IMESSAGE_AVATAR = APPS_ROOT_DIR + File.separator + "avatar";
//        IMESSAGE_FILE = APPS_ROOT_DIR + File.separator + "file";
//        IMESSAGE_VIDEO = APPS_ROOT_DIR + File.separator + "video";
//        TEMP_DIR_PATH = APPS_ROOT_DIR + File.separator + "temp";
//        LOCAL_PATH = APPS_ROOT_DIR + File.separator + "config.txt";
//        MULTI_MESSAGE_PATH = APPS_ROOT_DIR + File.separator + "multi";
//        WEB_DIR_PATH = APPS_ROOT_DIR + File.separator + "web";
//        MEDIA_DEBUG_PATH = APPS_ROOT_DIR + File.separator + "mediaDebug";
//        CLOUD_DISK = APPS_ROOT_DIR + File.separator + "cloudDisk";
    }

    public static String readContentByFile(String path) {
        BufferedReader reader = null;
        String line = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new FileReader(file));
                while ((line = reader.readLine()) != null) {
                    sb.append(line.trim());
                }
                return sb.toString().trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

//    /**
//     * 获取加密文件目录
//     *
//     * @return
//     */
//    public static File getFileEncodePathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error(RongXinApplicationContext.getContext().getResources().getString(R.string.media_ejected));
//            return null;
//        }
//        File directory = RongXinApplicationContext.getContext().getDir("Hengfengfile", Context.MODE_PRIVATE);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//
//    }

//    /**
//     * 获取语音文件存储目录
//     *
//     * @return
//     */
//    public static File getVoicePathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.info(RongXinApplicationContext.getContext().getResources().getString(R.string.media_ejected));
//            return null;
//        }
//
//        File directory = new File(IMESSAGE_VOICE);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }
//
//    /**
//     * 头像
//     *
//     * @return
//     */
//    public static File getAvatarPathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(IMESSAGE_AVATAR);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }
//

//    /**
//     * 获取文件目录
//     *
//     * @return
//     */
//    public static File getFilePathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(IMESSAGE_FILE);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }

//    /**
//     * 返回图片存放目录
//     *
//     * @return
//     */
//    public static File getImagePathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(IMESSAGE_IMAGE);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }

//    /**
//     * 返回下载目录
//     *
//     * @return
//     */
//    public static File getCloudPathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，网盘功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(CLOUD_DISK);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }
//
//    /**
//     * 返回临时存放目录
//     *
//     * @return
//     */
//    public static File getTempPathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(TEMP_DIR_PATH);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }


//    /**
//     * 返回图片存放目录
//     *
//     * @return
//     */
//    public static File getVideoPathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(IMESSAGE_VIDEO);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }
//
//
//    public static File getMultiMessagePathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.error("储存卡已拔出，语音功能将暂时不可用");
//            return null;
//        }
//
//        File directory = new File(MULTI_MESSAGE_PATH);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//        return directory;
//    }

//    /**
//     * 获取语音文件存储目录
//     *
//     * @return
//     */
//    public static File getMediaDebugPathName() {
//        if (!isExistExternalStore()) {
//            ConfToasty.info(RongXinApplicationContext.getContext().getResources().getString(R.string.media_ejected));
//            return null;
//        }
//
//        File directory = new File(MEDIA_DEBUG_PATH);
//        if (!directory.exists() && !directory.mkdirs()) {
//            ConfToasty.error("Path to file could not be created");
//            return null;
//        }
//
//        return directory;
//    }

    /**
     * 获取文件名
     *
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {

        int start = pathName.lastIndexOf("/");
        if (start != -1) {
            return pathName.substring(start + 1, pathName.length());
        }
        return pathName;

    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
//        if (RongXinApplicationContext.getContext() != null) {
//            return RongXinApplicationContext.getContext().getExternalCacheDir().getAbsolutePath();
//        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

//    /**
//     * /data/data/com.RongXin.bluetooth/files
//     *
//     * @return
//     */
//    public static String getAppContextPath() {
//        return RongXinApplicationContext.getContext().getFilesDir().getAbsolutePath();
//    }

    /**
     * @param fileName
     * @return
     */
    public static String getFileUrlByFileName(String fileName) {
        return FileAccessor.IMESSAGE_IMAGE + File.separator + FileAccessor.getSecondLevelDirectory(fileName) + File.separator + fileName;
    }

    /**
     * @param filePaths
     */
    public static void delFiles(List<String> filePaths) {
        if (filePaths == null || filePaths.size() == 0) {
            return;
        }
        for (String url : filePaths) {
            if (!TextUtils.isEmpty(url)) {
                Log.d(TAG, "to be deleted file local is " + url);
                delFile(url);
            }
        }
    }


    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return false;
        }
        return file.delete();
    }

    /**
     * @param fileName
     * @return
     */
    public static String getSecondLevelDirectory(String fileName) {
        if (TextUtils.isEmpty(fileName) || fileName.length() < 4) {
            return null;
        }

        String sub1 = fileName.substring(0, 2);
        String sub2 = fileName.substring(2, 4);
        return sub1 + File.separator + sub2;
    }

    /**
     * @param root
     * @param srcName
     * @param destName
     */
    public static void renameTo(String root, String srcName, String destName) {
        if (TextUtils.isEmpty(root) || TextUtils.isEmpty(srcName) || TextUtils.isEmpty(destName)) {
            return;
        }

        File srcFile = new File(root + srcName);
        File newPath = new File(root + destName);

        if (srcFile.exists()) {
            srcFile.renameTo(newPath);
        }
    }

    public static File getTackPicFilePath() {
//        File localFile = new File(getExternalStorePath()+ "/RongXin/.tempchat" , "temp.jpg");
//        if ((!localFile.getParentFile().exists())
//                && (!localFile.getParentFile().mkdirs())) {
//            LogUtil.e("hhe", "SD卡不存在");
//            localFile = null;
//        }
//        return localFile;
        return getTackPicFilePath("temp");
    }

    public static File getTackPicFilePath(String picName) {
        if (TextUtils.isEmpty(picName)) {
            return getTackPicFilePath();
        }
        File localFile = new File(getExternalStorePath() + "/RongXin/.tempchat", picName + ".jpg");
        if ((!localFile.getParentFile().exists())
                && (!localFile.getParentFile().mkdirs())) {
            Log.e("hhe", "SD卡不存在");
            localFile = null;
        }
        return localFile;
    }


    public static String getExternalStorageState() {

        return Environment.getExternalStorageState();
    }

    /**
     * 文件名称
     *
     * @param b
     * @return
     */
    public static String getFileNameMD5(byte[] b) {
        char[] src = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(b);
            byte[] digestByte = digest.digest();
            int length = digestByte.length;
            char[] result = new char[length * 2];
            int index = 0;
            for (int i = 0; i < digestByte.length; i++) {
                byte d = digestByte[i];
                result[index] = src[(0xF & d >>> 4)];
                index += 1;
                result[index] = src[d & 0xF];
                index += 1;
            }
            return new String(result);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(IMESSAGE_VIDEO);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "RX_" + timeStamp + ".mp4");
        return mediaFile;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 是否成功
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists())
            return file.delete();
        return true;
    }


//    /**
//     * 判断文件是否存在
//     *
//     * @param fileUrl 文件路径
//     * @return 文件是否存在
//     */
//    public static boolean exists(String fileUrl) {
//        return !BackwardSupportUtil.isNullOrNil(fileUrl) && new File(fileUrl).exists();
//
//    }

    /**
     * 创建文件目录
     *
     * @param dir 绝对路径
     * @return 是否创建成功
     */
    public static boolean mkdirsDir(String dir) {
        if (!isExistExternalStore()) {
            return false;
        }
        File directory = new File(dir);
        if (!directory.exists() && !directory.mkdirs()) {
            return false;
        }
        return true;
    }

    /**
     * 录音文件
     * @param filepath
     * @return
     */
    public static File recordPath(String filepath) {

        File file = new File(filepath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }
}
