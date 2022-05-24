#Added by the IDE on project create
cmake_minimum_required(VERSION 3.4.1)

#Two sets suggested by Bruno Alexandre Krinski 20160825
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")


#Addition suggested by Bruno Alexandre Krinski 20160825
include_directories(${CMAKE_SOURCE_DIR}/src/main/JNILib)
#Addition suggested by Bruno Alexandre Krinski 20160825
add_library( lib_opencv SHARED IMPORTED )
#Added by IDE on project create
add_library( native-lib SHARED src/main/cpp/native-lib.cpp )




#Addition suggested by Bruno Alexandre Krinski 20160825
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI}/libopencv_java3.so)

#Added by IDE on project create
find_library( log-lib log )

#Added by IDE on project create, Removed and replace with additional parameter suggested by Bruno Alexandre Krinski 20160825
#target_link_libraries( native-lib $\{log-lib} )
target_link_libraries( native-lib  ${log-lib} lib_opencv)
