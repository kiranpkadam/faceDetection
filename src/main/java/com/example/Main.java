package com.example;

import com.example.ui.VideoFrame;

public class Main {
    public static void main(String[] args) {
        // Initialize and start the video stream
        VideoStream videoStream = new VideoStream();
        VideoFrame videoFrame = new VideoFrame(videoStream);
        videoStream.start();
    }
}
