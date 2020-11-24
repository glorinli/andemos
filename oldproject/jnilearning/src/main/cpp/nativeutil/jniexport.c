//
// Created by Glorin on 2019/11/27.
//

#include <jni.h>
#include "include/nativeutil.h"
#include <stdio.h>

JNIEXPORT jint JNICALL Java_xyz_glorin_jnilearning_NativeUtil_add(JNIEnv *env,
        jclass clazz, jint a, jint b)
{
    return add(a, b);
}

JNIEXPORT jint JNICALL Java_xyz_glorin_jnilearning_NativeUtil_sub(JNIEnv *env,
                                                                  jclass clazz, jint a, jint b)
{
    return sub(a, b);
}

JNIEXPORT jint JNICALL Java_xyz_glorin_jnilearning_NativeUtil_multi(JNIEnv *env,
                                                                  jclass clazz, jint a, jint b)
{
    return multi(a, b);
}

JNIEXPORT jint JNICALL Java_xyz_glorin_jnilearning_NativeUtil_div(JNIEnv *env,
                                                                  jclass clazz, jint a, jint b)
{
    return div(a, b);
}



