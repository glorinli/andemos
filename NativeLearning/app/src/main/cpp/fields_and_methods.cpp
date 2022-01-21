#include <jni.h>
#include <string>

extern "C"
JNIEXPORT void JNICALL
Java_com_jxtech_nativelearning_FieldsAndMethods_changeName(JNIEnv *env, jobject thiz) {
    jfieldID fid;
    jstring jstr;
    const char *str;

    jclass cls = env->GetObjectClass(thiz);
    fid = env->GetFieldID(cls, "name", "Ljava/lang/String;");

    if (fid == NULL) return;

    jstr = static_cast<jstring>(env->GetObjectField(thiz, fid));
    str = env->GetStringUTFChars(jstr, NULL);
    if (str == NULL) return;

    printf("old name = %s\n", str);
    env->ReleaseStringUTFChars(jstr, str);

    jstr = env->NewStringUTF("New name");
    if (jstr == NULL) return;

    env->SetObjectField(thiz, fid, jstr);
}