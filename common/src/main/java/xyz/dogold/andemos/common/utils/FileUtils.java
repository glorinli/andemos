package xyz.dogold.andemos.common.utils;

import android.os.Environment;

import java.io.File;

import static android.text.TextUtils.isEmpty;

/**
 * FileUtils
 * Created by glorin on 17/10/2017.
 */

public class FileUtils {
    public static String APP_EXTERNAL_STORAGE_DIR = Environment.getExternalStorageDirectory() + File.separator + "Andemos";

    public static boolean isFileExists(String path) {
        if (isEmpty(path)) {
            return false;
        }
        try {
            return new File(path).exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
