# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.
cmake_minimum_required(VERSION 3.4.1)

#设置生成的so动态库最后输出的路径
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})
set(LOCAL_LDLIBS  "-llog -lGLESv1_CM -ldl -lz")
set(ORI_AMR_WB_LOC ${CMAKE_SOURCE_DIR}/src/main/cpp/opencore_amr/amrwb)
set(ORI_AMR_WB_DEC ${ORI_AMR_WB_LOC}/dec)
set(ORI_AMR_NB_LOC ${CMAKE_SOURCE_DIR}/src/main/cpp/opencore_amr/amrnb)
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
add_library( # Sets the name of the library.
        ori_amr_wb

        # Sets the library as a shared library.
        SHARED

        ${ORI_AMR_WB_DEC}/src/agc2_amr_wb.cpp
 	    ${ORI_AMR_WB_DEC}/src/band_pass_6k_7k.cpp
 	    ${ORI_AMR_WB_DEC}/src/dec_acelp_2p_in_64.cpp
 	    ${ORI_AMR_WB_DEC}/src/dec_acelp_4p_in_64.cpp
 	    ${ORI_AMR_WB_DEC}/src/dec_alg_codebook.cpp
 	    ${ORI_AMR_WB_DEC}/src/dec_gain2_amr_wb.cpp
# 	    ${ORI_AMR_WB_DEC}/src/decoder_amr_wb.cpp
 	    ${ORI_AMR_WB_DEC}/src/deemphasis_32.cpp
 	    ${ORI_AMR_WB_DEC}/src/dtx_decoder_amr_wb.cpp
 	    ${ORI_AMR_WB_DEC}/src/get_amr_wb_bits.cpp
 	    ${ORI_AMR_WB_DEC}/src/highpass_400hz_at_12k8.cpp
 	    ${ORI_AMR_WB_DEC}/src/highpass_50hz_at_12k8.cpp
 	    ${ORI_AMR_WB_DEC}/src/homing_amr_wb_dec.cpp
 	    ${ORI_AMR_WB_DEC}/src/interpolate_isp.cpp
 	    ${ORI_AMR_WB_DEC}/src/isf_extrapolation.cpp
 	    ${ORI_AMR_WB_DEC}/src/isp_az.cpp
 	    ${ORI_AMR_WB_DEC}/src/isp_isf.cpp
 	    ${ORI_AMR_WB_DEC}/src/lagconceal.cpp
 	    ${ORI_AMR_WB_DEC}/src/low_pass_filt_7k.cpp
 	    ${ORI_AMR_WB_DEC}/src/median5.cpp
 	    ${ORI_AMR_WB_DEC}/src/mime_io.cpp
 	    ${ORI_AMR_WB_DEC}/src/noise_gen_amrwb.cpp
 	    ${ORI_AMR_WB_DEC}/src/normalize_amr_wb.cpp
 	    ${ORI_AMR_WB_DEC}/src/oversamp_12k8_to_16k.cpp
 	    ${ORI_AMR_WB_DEC}/src/phase_dispersion.cpp
 	    ${ORI_AMR_WB_DEC}/src/pit_shrp.cpp
 	    ${ORI_AMR_WB_DEC}/src/pred_lt4.cpp
 	    ${ORI_AMR_WB_DEC}/src/preemph_amrwb_dec.cpp
 	    ${ORI_AMR_WB_DEC}/src/pvamrwb_math_op.cpp
 	    ${ORI_AMR_WB_DEC}/src/pvamrwbdecoder.cpp
 	    ${ORI_AMR_WB_DEC}/src/q_gain2_tab.cpp
 	    ${ORI_AMR_WB_DEC}/src/qisf_ns.cpp
 	    ${ORI_AMR_WB_DEC}/src/qisf_ns_tab.cpp
 	    ${ORI_AMR_WB_DEC}/src/qpisf_2s.cpp
 	    ${ORI_AMR_WB_DEC}/src/qpisf_2s_tab.cpp
 	    ${ORI_AMR_WB_DEC}/src/scale_signal.cpp
 	    ${ORI_AMR_WB_DEC}/src/synthesis_amr_wb.cpp
 	    ${ORI_AMR_WB_DEC}/src/voice_factor.cpp
 	    ${ORI_AMR_WB_DEC}/src/wb_syn_filt.cpp
 	    ${ORI_AMR_WB_DEC}/src/weight_amrwb_lpc.cpp
        # Provides a relative path to your source file(s).
        ${ORI_AMR_WB_LOC}/wrapper.cpp)
add_library( # Sets the name of the library.
        ori_amr_wb_dec

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
		src/main/cpp/ori_amr_wb_dec.cpp)
add_library( # Sets the name of the library.
        ori_amr

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_amr_wb.cpp)

add_library( # Sets the name of the library.
        oricv

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_cv.cpp)


add_library( # Sets the name of the library.
        origami

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/test.cpp)

add_library( # Sets the name of the library.
        ori_bitmap

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/bitmap_handler.cpp)

add_library( # Sets the name of the library.
        ori_hyn

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_queue.cpp
        src/main/cpp/ori_audio.cpp
        src/main/cpp/ori_hyn.cpp)

add_library( # Sets the name of the library.
        ori_ppn

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_queue.cpp
        src/main/cpp/ori_audio.cpp
        src/main/cpp/ori_ppn.cpp)

add_library( # Sets the name of the library.
        ori_transcoding

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_transcoding.cpp)
add_library( # Sets the name of the library.
        ori_mp

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        src/main/cpp/ori_mp.cpp)
#动态方式加载 STATIC：表示静态的.a的库 SHARED：表示.so的库。
add_library(qrdatadecode SHARED IMPORTED)

add_library(avcodec SHARED IMPORTED)
add_library(avfilter SHARED IMPORTED)
add_library(avformat SHARED IMPORTED)
add_library(avutil SHARED IMPORTED)
add_library(swresample SHARED IMPORTED)
add_library(swscale SHARED IMPORTED)
add_library(opencv_java4 SHARED IMPORTED)

#设置要连接的so的相对路径 ${CMAKE_SOURCE_DIR}：表示CMake.txt的当前文件夹路径 ${ANDROID_ABI}：编译时会自动根据CPU架构去选择相应的库
set_target_properties(qrdatadecode PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libqrdatadecode.so)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavcodec.so)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavfilter.so)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavformat.so)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libavutil.so)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswresample.so)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libswscale.so)
set_target_properties(opencv_java4 PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libopencv_java4.so)


#添加第三方头文件
target_include_directories(ori_amr_wb PRIVATE
        ${CMAKE_SOURCE_DIR}/src/main/cpp/opencore_amr/common/dec/include
        ${CMAKE_SOURCE_DIR}/src/main/cpp/opencore_amr/oscl
        ${ORI_AMR_WB_LOC})

target_include_directories(ori_amr PRIVATE
		${ORI_AMR_WB_LOC}
		${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(ori_amr PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(origami PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(ori_hyn PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(ori_ppn PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(ori_transcoding PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
target_include_directories(oricv PRIVATE ${CMAKE_SOURCE_DIR}/src/main/cpp/include)
# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)


find_library(
        OpenSLES-lib
        OpenSLES )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        origami
        qrdatadecode
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        ori_bitmap
        jnigraphics
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        ori_hyn
        android

        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${OpenSLES-lib})

target_link_libraries( # Specifies the target library.
        ori_ppn
        android

        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib}
        ${OpenSLES-lib})

target_link_libraries( # Specifies the target library.
        ori_mp
        android

        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        ori_transcoding
        android

        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        ori_amr
        android

        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
		ori_amr_wb_dec

        ori_amr_wb
        android
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})

target_link_libraries( # Specifies the target library.
        oricv
        android
        jnigraphics

        opencv_java4
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})