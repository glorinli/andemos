package xyz.dogold.andemos.common.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Created by glorin on 19/04/2017.
 */

public class ActivityUtils {
    public static void startActivity(Context context, Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }
}
