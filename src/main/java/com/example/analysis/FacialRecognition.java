package com.example.analysis;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

public class FacialRecognition {
    private Net net;

    public FacialRecognition() {
        String modelWeights = "src/main/resources/dnn/res10_300x300_ssd_iter_140000.caffemodel";
        String modelConfiguration = "src/main/resources/dnn/deploy.prototxt";
        net = Dnn.readNetFromCaffe(modelConfiguration, modelWeights);
    }

    public void recognizeFaces(Mat frame) {
        Mat inputBlob = Dnn.blobFromImage(frame, 1.0, new Size(300, 300), new Scalar(104.0, 177.0, 123.0), false, false);
        net.setInput(inputBlob);
        Mat detections = net.forward();

        int cols = frame.cols();
        int rows = frame.rows();
        detections = detections.reshape(1, (int) detections.total() / 7);

        for (int i = 0; i < detections.rows(); i++) {
            double confidence = detections.get(i, 2)[0];
            if (confidence > 0.6) {
                int xLeftBottom = (int) (detections.get(i, 3)[0] * cols);
                int yLeftBottom = (int) (detections.get(i, 4)[0] * rows);
                int xRightTop = (int) (detections.get(i, 5)[0] * cols);
                int yRightTop = (int) (detections.get(i, 6)[0] * rows);

                Imgproc.rectangle(frame, new Point(xLeftBottom, yLeftBottom), new Point(xRightTop, yRightTop), new Scalar(0, 255, 0));
            }
        }
    }
}
