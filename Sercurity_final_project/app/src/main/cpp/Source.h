
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <opencv2/opencv.hpp>
#include "opencv2/features2d/features2d.hpp"
#include <iostream>
#include <stdio.h>
#include <cmath>
#include <list>

#define PI 3.14159265358979323846

using namespace std;
using namespace cv;

/** Function Headers */
Mat findAndExtractIris( Mat original);
int findIrisRadius(Mat input , Point startPoint, int radius);
Mat fillHoles(Mat input);
Mat normalize(Mat input);
vector<int> CHT(Mat input, int minRadius, int maxRadius);

/** Global variables */
string window = "Output";
int pupilx, pupily, pupilRadius, irisRadius;
int histogramValues[58] = {0, 1, 2, 3, 4, 6, 7, 8, 12, 14, 15, 16, 24, 28, 30, 31, 32, 48, 56, 60, 62, 63, 64, 96, 112, 120, 124, 126, 127, 128, 129, 131, 135, 143, 159, 191, 192, 193, 195, 199, 207, 223, 224, 225, 227, 231, 239, 240, 241, 243, 247, 248, 249, 251, 252, 253, 254, 255};
