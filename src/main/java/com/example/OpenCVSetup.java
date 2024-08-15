package com.example;

public class OpenCVSetup {
    static {
        // Print the java.library.path for debugging
        System.out.println("java.library.path: " + System.getProperty("java.library.path"));

        // Set the library path (if needed)
        // System.setProperty("java.library.path", "D:\\Open_cv\\opencv\\build\\java\\x64");
        
        // Load the OpenCV native library
        
      
            System.out.println("java.library.path: " + System.getProperty("java.library.path"));
            System.loadLibrary("opencv_java4100"); // Use the correct library name
        

    }

    public static void init() {
        // Additional OpenCV initialization if needed
    }
}
