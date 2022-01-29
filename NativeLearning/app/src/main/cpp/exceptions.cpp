#include <jni.h>
#include <string>

extern "C"
JNIEXPORT void JNICALL
Java_com_jxtech_nativelearning_Exceptions_doit(JNIEnv *env, jobject thiz) {
    jthrowable exc;
    jclass cls = env->GetObjectClass(thiz);
    jmethodID mid = env->GetMethodID(cls, "callback", "()V");
    env->DeleteLocalRef(cls);
    if (mid == NULL) return;

    env->CallVoidMethod(thiz, mid);
    exc = env->ExceptionOccurred();

    if (exc) {
        // Print the exception
        env->ExceptionDescribe();
        env->ExceptionClear();

        jclass newExcCls = env->FindClass("java/lang/IllegalArgumentException");

        if (newExcCls == NULL) return;

        env->ThrowNew(newExcCls, "Thrown from cpp");
    }
}