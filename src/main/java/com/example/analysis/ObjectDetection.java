package com.example.analysis;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;







public class ObjectDetection {
    private static final CascadeClassifier faceDetector = new CascadeClassifier("D:\\Open_cv\\haarcascade_frontalface_alt.xml");

    public Mat detectObjects(Mat frame) {
        MatOfRect faces = new MatOfRect();
        faceDetector.detectMultiScale(frame, faces, 1.1, 3, 0, new Size(30, 30), new Size());

        for (Rect rect : faces.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }
        System.out.println("Detected faces: " + faces.toArray().length);
        return frame; // Return the processed image
    }
}


