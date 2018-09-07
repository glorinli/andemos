package xyz.dogold.andemos.common.utils;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

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

    public static ArrayList<String> getSelectedMediaPaths(Context context, Intent data) {

        ArrayList<String> result = new ArrayList<>();

        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getRealPathFromURI(context, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = getRealPathFromURI(context, videoURI);
            result.add(filePath);
        }

        return result;
    }
}
