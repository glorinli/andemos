#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_jxtech_nativelib_NativeLib_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++ Library project";
    return env->NewStringUTF(hello.c_str());
}