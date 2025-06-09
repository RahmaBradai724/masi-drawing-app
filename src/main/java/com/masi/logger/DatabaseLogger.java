package com.masi.logger;
// DatabaseLogger.java
public class DatabaseLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("Database: " + message); // Simulated DB log
        // In a real app, add DB connection logic here
    }
}
