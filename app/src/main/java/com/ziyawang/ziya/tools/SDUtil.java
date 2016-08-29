package com.ziyawang.ziya.tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
/**
 * 操作SDcard的工具类
 * @author pk
 *
 */

/**
 * Created by 牛海丰 on 2016/07/19 0029.
 */
@SuppressLint("NewApi")
public class SDUtil {

    // 检查SD的状态.
    public static boolean isSDMounted() {

        // Environment:用来访问系统的环境变量的.
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    // 获取SD卡的根路径.
    public static String getSDPath() {
        if (isSDMounted()) {

            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    // 计算SD卡总大小
    public static long getSDSize() {
        if (isSDMounted()) {
            // StatFs:是Linux系统中用来检索文件系统空间的类.
            // 该类是Unix系统中statvfs()方法的一个包装.
            StatFs stat = new StatFs(getSDPath());
            // 得到文件的总大小.要求API最低18.
            // stat.getTotalBytes();

            // int blockCount = stat.getBlockCount();
            // int blockSize = stat.getBlockSize();
            // blockCount*blockSize;
            // 得到块的数量
            long blockCountLong = stat.getBlockCountLong();
            // 得到每块的大小
            long blockSizeLong = stat.getBlockSizeLong();
            // 块的数量*每块的大小=文件总大小
            return blockCountLong * blockSizeLong / 1024 / 1024;
        }

        return 0;
    }

    // 计算SD卡的可用空间
    public static long getSDAvailable() {

        if (isSDMounted()) {
            StatFs stat = new StatFs(getSDPath());
            // 得到文件的可用大小
            // long availableBytes = stat.getAvailableBytes();
            // int availableBlocks = stat.getAvailableBlocks();
            // int blockSize = stat.getBlockSize();
            // 可用的块数
            long availableBlocksLong = stat.getAvailableBlocksLong();
            // 每块的大小
            long blockSizeLong = stat.getBlockSizeLong();
            // 可用大小
            return availableBlocksLong * blockSizeLong / 1024 / 1024;
        }
        return 0;
    }

    // 存储数据到SD卡中
    public static boolean saveDataInfoSDCard(byte[] data, String dir, String filename) {

        if (isSDMounted()) {
            BufferedOutputStream bos = null;
            try {
                // 当文件不存在的时候创建文件
                String path = getSDPath() + File.separator + dir;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 往文件中进行写操作.
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, filename)));
                bos.write(data, 0, data.length);
                bos.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return false;
    }

    // 从SD卡中读取数据
    public static byte[] getDataFromSDCard(String fileName) {
        if (isSDMounted()) {

            ByteArrayOutputStream baos = null;
            FileInputStream fis = null;
            try {

                String path = getSDPath() + File.separator + "ziya"+File.separator +fileName;
                File file = new File(path);
                if (file.exists()) {
                    baos = new ByteArrayOutputStream();
                    fis = new FileInputStream(file);
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    while ((len = fis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                        baos.flush();
                    }

                    return baos.toByteArray();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (baos != null) {
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return null;
    }

    // 保存数据到公共目录中
    public static boolean saveDataInfoSDCardPulbic(byte[] data, String type, String filename) {

        if (isSDMounted()) {
            BufferedOutputStream bos = null;
            try {
                // 当文件不存在的时候创建文件

                String path = Environment.getExternalStoragePublicDirectory(type).getAbsolutePath();

                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 往文件中进行写操作.
                bos = new BufferedOutputStream(new FileOutputStream(new File(
                        file, filename)));
                bos.write(data, 0, data.length);
                bos.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return false;
    }

    // 保存数据到SD的Android目录中
    public static boolean saveDataInfoSDCardPrivate(byte[] data,Context context, String type, String filename) {

        if (isSDMounted()) {
            BufferedOutputStream bos = null;
            try {
                // 当文件不存在的时候创建文件
                String path = context.getExternalFilesDir(type).getAbsolutePath();

                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 往文件中进行写操作.
                bos = new BufferedOutputStream(new FileOutputStream(new File(file, filename)));
                bos.write(data, 0, data.length);
                bos.flush();

                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return false;
    }
}

