package com.masi.logger;
// FileLogger.java
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements Logger {
    @Override
    public void log(String message) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.write("File: " + message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
