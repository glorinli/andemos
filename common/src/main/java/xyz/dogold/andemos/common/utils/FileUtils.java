package xyz.dogold.andemos.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

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

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor;
        String[] proj = {MediaStore.MediaColumns.DATA};
        cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        } else {
            return null;
        }
    }
}
