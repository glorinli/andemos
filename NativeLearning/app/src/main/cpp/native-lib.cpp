#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_jxtech_nativelearning_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
extern "C"
JNIEXPORT jint JNICALL
Java_com_jxtech_nativelearning_PrimitiveTypes_plus(JNIEnv *env, jobject thiz, jint a, jint b) {
    return a + b;
}
extern "C"
JNIEXPORT jfloat JNICALL
Java_com_jxtech_nativelearning_PrimitiveTypes_sumOfFloatArray(JNIEnv *env, jobject thiz,
                                                              jfloatArray array) {
    jint length = env->GetArrayLength(array);
    jint i = 0;
    jfloat sum = 0.0;
    jfloat *carr;
    carr = env->GetFloatArrayElements(array, NULL);

    if (carr == NULL) {
        return 0;
    }

    for (i = 0; i < length; i++) {
        sum += carr[i];
    }

    env->ReleaseFloatArrayElements(array, carr, 0);

    return sum;
}