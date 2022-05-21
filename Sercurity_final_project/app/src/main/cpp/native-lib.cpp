
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include <opencv2/opencv.hpp>
#include "opencv2/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include <iostream>
#include <stdio.h>
#include <cmath>
#include <list>>
#include "jni.h"

#define PI 3.14159265358979323846


using namespace cv;
using namespace std;


int pupilx, pupily, pupilRadius, irisRadius;
int histogramValues[58] = {0, 1, 2, 3, 4, 6, 7, 8, 12, 14, 15, 16, 24, 28, 30, 31, 32, 48, 56, 60, 62, 63, 64, 96, 112, 120, 124, 126, 127, 128, 129, 131, 135, 143, 159, 191, 192, 193, 195, 199, 207, 223, 224, 225, 227, 231, 239, 240, 241, 243, 247, 248, 249, 251, 252, 253, 254, 255};




//Mat findAndExtractIris(Mat input, Mat unprocessed, Mat original);
//int findIrisRadius(Mat input , Point startPoint, int radius);
//Mat fillHoles(Mat input);
//Mat normalize(Mat input);
//vector<int> CHT(Mat input, int minRadius, int maxRadius);


Mat findAndExtractIris(Mat input, Mat unprocessed, Mat original);
int findIrisRadius(Mat input , Point startPoint, int radius);
Mat fillHoles(Mat input);
Mat normalize(Mat input);
vector<int> CHT(Mat input, int minRadius, int maxRadius);

extern "C"
JNIEXPORT void JNICALL
Java_com_example_IrisREC_Function_NativeFunctionCall_1IrisFunction_DetectIris(JNIEnv *env,
                                                                              jclass clazz,
                                                                              jlong addr_input,
                                                                              jlong addr_output,
                                                                              jlong addr_output_normalized,
                                                                              jlong addr_original) {

    Mat& currentImage = *(Mat*)addr_input;
    Mat& output = *(Mat*)addr_output;
    Mat& outputNormalized = *(Mat*)addr_output_normalized;
    Mat& original = *(Mat*)addr_original;

    Mat unprocessed = currentImage.clone();
    cvtColor(currentImage, currentImage, COLOR_BGR2GRAY);
    output = findAndExtractIris(currentImage, unprocessed, original);
    outputNormalized = normalize(output);
    unprocessed.release();
}

//The function that finds the pupil and iris


Mat findAndExtractIris(Mat input, Mat unprocessed, Mat original)
{
    Mat processed;
    threshold(input, processed, 90, 255, THRESH_BINARY_INV);
    processed = fillHoles(input);

    cvtColor(unprocessed, unprocessed, CV_BGR2GRAY);

    GaussianBlur(processed, processed, Size(9, 9), 3, 3);

    //Find the circles
    //vector<int> circle = CHT(processed, 10, 30);
    vector<Vec3f> circles;
    HoughCircles(processed, circles, CV_HOUGH_GRADIENT, 2, original.rows / 8, 255, 30, 0, 0);
    if (circles.size() == 0)
        unprocessed.release();
    for (size_t i = 0; i < 1; i++) //Check first circle found
    {
        Point center(cvRound(circles[i][0]), cvRound(circles[i][1]));
        pupilx = cvRound(circles[i][0]), pupily = cvRound(circles[i][1]);

        //Normal
        if (cvRound(circles[i][2]) < 20)
        {
            pupilRadius = cvRound(circles[i][2]);
            irisRadius = findIrisRadius(unprocessed, center, pupilRadius);
        }
        else //Brown eyes
        {
            irisRadius = cvRound(circles[i][2]);
            pupilRadius = 15;
        }
        circle(unprocessed, center, pupilRadius, Scalar(0), CV_FILLED);
        circle(unprocessed, center, irisRadius, Scalar(0), 2, 8, 0);
    }

    return unprocessed;
}


vector<int> CHT(Mat input, int minRadius, int maxRadius)
{
    vector<int> outputVector = vector<int>(3);
    Mat cannyimage = input;
    Mat cannyimageLarge = cannyimage;

    Size newSize(cannyimage.cols/4, cannyimage.rows/4);
    resize(cannyimage, cannyimage, newSize);
    maxRadius = maxRadius / 4;
    minRadius = minRadius / 4;
    Canny(cannyimage, cannyimage, 100, 120, 3, false);
    Canny(cannyimageLarge, cannyimageLarge, 100, 120, 3, false);
    int xdim = cannyimage.cols;
    int ydim = cannyimage.rows;
    int rdim = maxRadius;

    vector<vector<vector<double>>> accumulator(xdim, vector<vector<double>>(ydim, vector<double>(rdim)));

    for (int x = 0; x < cannyimage.cols; x++)
    {
        for (int y = 0; y < cannyimage.rows; y++)
        {
            if (cannyimage.at<uchar>(x,y) == 255)
            {
                for (int r = minRadius; r < maxRadius; r++)
                {
                    for (int theta = 0; theta < 360; theta++)
                    {
                        int a = x - r * cos(theta * PI / 180);
                        int b = y - r * sin(theta * PI / 180);
                        if (a > 0 & b > 0 & a < cannyimage.cols & b < cannyimage.rows)
                            accumulator[a][b][r] = accumulator[a][b][r] + 1;
                    }
                }
            }
        }
    }

    int centerx = -1;
    int centery = -1;
    int finalRadius = -1;
    int max = 0;
    for (int x = 0; x < cannyimage.cols; x++)
    {
        for (int y = 0; y < cannyimage.rows; y++)
        {
            for (int r = minRadius; r < maxRadius; r++)
            {
                if (accumulator[x][y][r] > max)
                {
                    centerx = x;
                    centery = y;
                    finalRadius = r;
                    max = accumulator[x][y][r];
                }
            }
        }
    }
    outputVector[0] = centerx*4;
    outputVector[1] = centery*4;
    outputVector[2] = finalRadius*4;

    return outputVector;
}

int findIrisRadius(Mat input , Point startPoint, int radius)
{
    Mat hold;
    threshold(input, hold, 180, 255, CV_THRESH_BINARY);
    int rightIntensity;
    int leftIntensity;
    int position = startPoint.y - (radius+20);
    int newRadius = radius+20;
    while (true)
    {
        rightIntensity = leftIntensity;
        position -= 10;
        newRadius += 10;
        leftIntensity = hold.at<uchar>(startPoint.x, position);
        if (leftIntensity != rightIntensity)
            return newRadius-5;
    }
}


//Find the iris boundary by thresholding and checking where the value changes


//"Fill" the holes made by the reflections

Mat fillHoles(Mat input)
{
    Mat thresholded;
    threshold(input, thresholded, 70, 255, THRESH_BINARY_INV);

    Mat floodfilled = thresholded.clone();
    floodFill(floodfilled, Point(0, 0), Scalar(255));

    bitwise_not(floodfilled, floodfilled);
    return (thresholded | floodfilled);
}

//Normalize the circular shape to the rectangular shape
Mat normalize(Mat input) {
    int yNew = 360;
    int xNew = 100;

    Mat normalized = Mat(xNew, yNew, CV_8U, Scalar(255));
    for (int i = 0; i < yNew; i++) {
        double alpha = 2 * PI * i / yNew;
        for (int j = 0; j < xNew; j++) {
            double r = 1.0 * j / xNew;
            int x = (int) ((1 - r) * (pupilx + pupilRadius * cos(alpha)) +
                           r * (pupilx + irisRadius * cos(alpha)));
            int y = (int) ((1 - r) * (pupily + pupilRadius * sin(alpha)) +
                           r * (pupily + irisRadius * sin(alpha)));
            if (x < 0)
                x = 0;
            if (y < 0)
                y = 0;
            if (x > input.size().width - 1)
                x = input.size().width - 1;
            if (y > input.size().height - 1)
                y = input.size().height - 1;
            normalized.at<uchar>(j, i) = input.at<uchar>(y, x);
        }
    }
    Rect reducedSelection(0, 5, 360, 60);
    normalized = normalized(reducedSelection);
    return normalized;
}


/*
 #include <jni.h>
#include <string>
#include "Source.h"


//JNI function for authenticate







}

//Finds the pupil and iris





*/