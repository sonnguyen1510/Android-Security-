
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <opencv2/opencv.hpp>
#include "opencv2/features2d/features2d.hpp"
#include <iostream>
#include <stdio.h>
#include <cmath>
#include <list>>
#include "jni.h"
#include "opencv2/core.hpp"
#define PI 3.14159265358979323846

using namespace cv;
using namespace std;


/** Function Headers */
Mat findAndExtractIris(Mat input, Mat unprocessed, Mat original);
int findIrisRadius(Mat input , Point startPoint, int radius);
Mat fillHoles(Mat input);
Mat normalize(Mat input);
vector<int> CHT(Mat input, int minRadius, int maxRadius);

/** Global variables */
