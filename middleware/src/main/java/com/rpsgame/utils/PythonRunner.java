package com.rpsgame.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonRunner {

    public static String runScript(String imagePath) throws Exception {
        ProcessBuilder builder = new ProcessBuilder("python", "gesture_detector.py", imagePath);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        StringBuilder output = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        process.waitFor();
        return output.toString();
    }
}
