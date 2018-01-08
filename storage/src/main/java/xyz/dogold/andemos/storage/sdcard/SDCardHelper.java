package xyz.dogold.andemos.storage.sdcard;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;

import xyz.dogold.andemos.common.reflect.Reflect;
import xyz.dogold.andemos.common.utils.ToastUtil;

/**
 * SDCardHelper
 * Created by glorin on 08/01/2018.
 */

public class SDCardHelper {
    private static final String TAG = "SDCardHelper";

    private static final int VOLUME_SDCARD_INDEX = 1;

    private SDCardHelper() {
    }

    private static Uri sSDCardTreeUri;

    private static boolean sInit = false;

    private static SharedPreferences sSDCardPrefs;

    public static synchronized void init(Context context) {
        if (sInit) return;

        sSDCardPrefs = context.getSharedPreferences("sd_card_prefs", Context.MODE_PRIVATE);

        loadAndInitSDCardInfos();

        sInit = true;
    }

    private static void loadAndInitSDCardInfos() {
        // TODO, read sdcard path and uri
    }

    public static void handleSelectSDCardPathResult(Context context, Intent data) {
        Uri uri = data.getData();

        Log.d(TAG, String.format("handleSDCardSelected, uri: %1$s", uri));

        if (!SDCardHelper.checkSDCardUri(uri)) {
            ToastUtil.showToast(context, "Please select valid SD card path");
            return;
        }

        sSDCardTreeUri = uri;

        sSDCardPrefs.edit().putString("sd_card_uri_string", uri.toString())
                .putString("sd_card_path", getCurrentSDCardPath(context))
                .apply();

        // Take persistent permission
        final int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION |
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        context.getContentResolver().takePersistableUriPermission(uri, takeFlags);
    }

    public static Uri createFileOnSDCard(Context context, String fileName) throws FileNotFoundException {
        if (sSDCardTreeUri == null) {
            throw new IllegalStateException("Please select sdcard before using!!!");
        }

        Uri sdCardUri = DocumentsContract.buildDocumentUriUsingTree(sSDCardTreeUri,
                DocumentsContract.getTreeDocumentId(sSDCardTreeUri));

        return DocumentsContract.createDocument(context.getContentResolver(),
                sdCardUri, getMimeTypeFromFileName(fileName), fileName);
    }

    private static String getMimeTypeFromFileName(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else {
            return "video/mp4";
        }
    }

    public static boolean checkSDCardUri(Uri uri) {
        if (uri == null) {
            return false;
        }

        final String path = uri.getPath();

        if (!":".equals(path.substring(path.length() - 1)) || path.contains("primary")) {
            return false;
        }

        return true;
    }

    public static String getCurrentSDCardPath(Context context) {
        try {
            StorageManager mStorageMan = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

            Object[] volumes = Reflect.on(mStorageMan).call("getVolumeList").get();
            if (volumes != null && volumes.length > VOLUME_SDCARD_INDEX) {
                return Reflect.on(volumes[VOLUME_SDCARD_INDEX]).call("getPath").get();
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error query sdcard status", e);
            return null;
        }
    }
}
