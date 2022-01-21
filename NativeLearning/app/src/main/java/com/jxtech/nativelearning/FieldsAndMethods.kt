package com.jxtech.nativelearning

class FieldsAndMethods {
    private var name = "abc"

    private external fun changeName()

    fun callChangeName(): String {
        changeName()
        return "Changed name is $name"
    }

    companion object {
        init {
            System.loadLibrary("nativelearning")
        }
    }
}