package com.jxtech.nativelearning;

import android.util.Log;

public class Exceptions {
    private native void doit() throws IllegalArgumentException;
    private void callback() throws NullPointerException {
        throw new NullPointerException(("Exceptions.callback"));
    }

    public static void run() {
        Exceptions e = new Exceptions();

        try {
            e.doit();
        } catch (Exception ex) {
            Log.e("lgr>>", "In Java: \n\t" + e);
        }
    }
}
