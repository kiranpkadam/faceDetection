package com.example;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import com.example.analysis.MotionTracking;
import com.example.ui.VideoFrame;

public class VideoStream {
    private VideoCapture capture;
    private boolean isRunning;
    private VideoFrame videoFrame;
    private MotionTracking motionTracking;
    private static final String OUTPUT_PATH = "E:\\MotionDetectedImage_";

    public VideoStream() {
        OpenCVSetup.init();
        capture = new VideoCapture(0); // 0 for default camera
        isRunning = false;
        motionTracking = new MotionTracking(); // Initialize motion tracking
    }

    public void setVideoFrame(VideoFrame videoFrame) {
        this.videoFrame = videoFrame;
    }

    public void start() {
        isRunning = true;
        new Thread(() -> {
            int snapshotCounter = 0;
            while (isRunning) {
                Mat frame = new Mat();
                if (capture.read(frame)) {
                    // Process the frame with motion detection
                    boolean motionDetected = motionTracking.detectMotion(frame);
                    
                    // Update UI
                    if (videoFrame != null) {
                        videoFrame.updateFrame(frame);
                    }
                    
                    // Save snapshot if motion is detected
                    if (motionDetected) {
                        String outputPath = OUTPUT_PATH + snapshotCounter++ + ".jpg";
                        boolean result = Imgcodecs.imwrite(outputPath, frame);
                        if (result) {
                            System.out.println("Motion detected! Snapshot saved to: " + outputPath);
                        } else {
                            System.out.println("Failed to save snapshot.");
                        }
                    }
                }
            }
            capture.release();
        }).start();
    }

    public void stop() {
        isRunning = false;
    }
}
