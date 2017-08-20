package com.nandy.reader.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by yana on 02.01.17.
 */

public class StorageUtils {


    public static List<StorageInfo> getStorageList() {

        List<StorageInfo> list = new ArrayList<>();

        String storagePath = Environment.getExternalStorageDirectory().getPath();
        String externalStorageState = Environment.getExternalStorageState();

        boolean isStorageRemovable = !Environment.isExternalStorageRemovable();
        boolean isStorageAvailable = externalStorageState.equals(Environment.MEDIA_MOUNTED)
                || externalStorageState.equals(Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean isStorageToReadOnly = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED_READ_ONLY);

        BufferedReader bufferedReader = null;
        try {
            HashSet<String> paths = new HashSet<>();
            bufferedReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            int currentNumber = 1;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("vfat") || line.contains("/mnt")) {
                    StringTokenizer tokens = new StringTokenizer(line, " ");
                    String unused = tokens.nextToken(); //device
                    String mount_point = tokens.nextToken(); //mount point
                    if (paths.contains(mount_point)) {
                        continue;
                    }
                    unused = tokens.nextToken(); //file system
                    List<String> flags = Arrays.asList(tokens.nextToken().split(",")); //flags
                    boolean readonly = flags.contains("ro");

                    if (mount_point.equals(storagePath)) {
                        paths.add(storagePath);
                        list.add(0, new StorageInfo(storagePath, isStorageRemovable, readonly, -1));
                    } else if (line.contains("/dev/block/vold")) {
                        if (!line.contains("/mnt/secure")
                                && !line.contains("/mnt/asec")
                                && !line.contains("/mnt/obb")
                                && !line.contains("/dev/mapper")
                                && !line.contains("tmpfs")) {
                            paths.add(mount_point);
                            list.add(new StorageInfo(mount_point, false, readonly, currentNumber++));
                        }
                    }
                }
            }

            if (!paths.contains(storagePath) && isStorageAvailable) {
                list.add(0, new StorageInfo(storagePath, isStorageRemovable, isStorageToReadOnly, -1));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static boolean deleteDir(File directory) {
        if (directory.isDirectory()) {

            for (File file : directory.listFiles()) {
                boolean success = deleteDir(file);
                if (!success) {
                    return false;
                }
            }
        }
        return directory.delete();
    }
}
