package com.example.IrisREC.Function;

import org.opencv.core.Mat;
import java.lang.annotation.Native;

public class NativeFunctionCall_IrisFunction {
    public native static void DetectIris(long addrInput, long addrOutput, long addrOutputNormalized, long addrOriginal);
}
