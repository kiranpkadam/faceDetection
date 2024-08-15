package com.example.analysis;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MotionTracking {
    private Mat previousFrame = new Mat();
    private static final int MIN_CONTOUR_AREA = 500;

    public boolean detectMotion(Mat currentFrame) {
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(currentFrame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(grayFrame, grayFrame, new Size(21, 21), 0);

        if (previousFrame.empty()) {
            grayFrame.copyTo(previousFrame);
            return false;
        }

        Mat frameDelta = new Mat();
        Core.absdiff(previousFrame, grayFrame, frameDelta);
        Imgproc.threshold(frameDelta, frameDelta, 25, 255, Imgproc.THRESH_BINARY);
        Imgproc.dilate(frameDelta, frameDelta, new Mat(), new Point(-1, -1), 2);

        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(frameDelta, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        boolean motionDetected = false;
        for (MatOfPoint contour : contours) {
            if (Imgproc.contourArea(contour) < MIN_CONTOUR_AREA) {
                continue;
            }
            Rect boundingRect = Imgproc.boundingRect(contour);
            Imgproc.rectangle(currentFrame, boundingRect.tl(), boundingRect.br(), new Scalar(0, 255, 0), 2);
            motionDetected = true;
        }

        grayFrame.copyTo(previousFrame);
        return motionDetected;
    }
}
