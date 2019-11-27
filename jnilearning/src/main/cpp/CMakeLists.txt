cmake_minimum_required(VERSION 3.4.1)

add_library(nativeutil SHARED ./nativeutil/nativeutil.c nativeutil/jniexport.c)