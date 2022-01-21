package com.jxtech.nativelearning

class PrimitiveTypes {
    external fun plus(a: Int, b: Int): Int

    external fun sumOfFloatArray(array: FloatArray): Float

    companion object {
        // Used to load the 'nativelearning' library on application startup.
        init {
            System.loadLibrary("nativelearning")
        }
    }
}