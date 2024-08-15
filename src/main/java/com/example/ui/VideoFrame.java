package com.example.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import com.example.VideoStream;
import com.example.analysis.ObjectDetection;

public class VideoFrame extends JFrame {
    private JLabel videoLabel;
    private VideoStream videoStream;
    private ObjectDetection objectDetection;
    private Mat currentFrame;

    public VideoFrame(VideoStream videoStream) {
        this.videoStream = videoStream;
        videoStream.setVideoFrame(this);
        this.objectDetection = new ObjectDetection(); // Initialize object detection

        videoLabel = new JLabel();
        add(videoLabel, BorderLayout.CENTER);

        JButton saveButton = new JButton("Save Snapshot");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveSnapshot();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Real-Time Video Stream Analysis");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void updateFrame(Mat frame) {
        this.currentFrame = frame.clone(); // Keep a copy of the current frame

        // Process the frame with object detection
        Mat processedFrame = objectDetection.detectObjects(frame);

        // Convert Mat to BufferedImage
        ImageIcon imageIcon = new ImageIcon(matToBufferedImage(processedFrame));
        videoLabel.setIcon(imageIcon);
        videoLabel.repaint();
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        int type = (mat.channels() == 1) ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());
        return image;
    }

    private void saveSnapshot() {
        if (currentFrame != null && !currentFrame.empty()) {
            // Define the output file path
            String outputPath = "E:\\DetectedImage_" + System.currentTimeMillis() + ".jpg";
            boolean result = Imgcodecs.imwrite(outputPath, currentFrame);

            if (result) {
                System.out.println("Snapshot saved to: " + outputPath);
            } else {
                System.out.println("Failed to save snapshot.");
            }
        } else {
            System.out.println("No frame available to save.");
        }
    }
}
