package xyz.glorin.jnilearning

object NativeUtil {
    external fun add(a: Int, b: Int): Int
    external fun sub(a: Int, b: Int): Int
    external fun multi(a: Int, b: Int): Int
    external fun div(a: Int, b: Int): Int

    init {
        System.loadLibrary("nativeutil")
    }
}