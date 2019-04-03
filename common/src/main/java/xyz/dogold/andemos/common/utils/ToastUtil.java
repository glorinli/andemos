package xyz.dogold.andemos.common.utils;

import android.content.Context;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * ToastUtil
 * Created by glorin on 29/08/2017.
 */

public class ToastUtil {
    public static void showToast(Context context, @StringRes int id) {
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
