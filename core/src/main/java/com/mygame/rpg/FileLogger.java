package com.mygame.rpg;

import com.badlogic.gdx.ApplicationLogger;
import java.io.*;

public class FileLogger implements ApplicationLogger {
    private PrintWriter writer;

    public FileLogger(String logFilePath) {
        try {
            writer = new PrintWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String tag, String message) {
        writer.println("[" + tag + "] " + message);
        writer.flush();
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        writer.println("[" + tag + "] " + message);
        exception.printStackTrace(writer);
        writer.flush();
    }

    @Override
    public void error(String tag, String message) {
        log(tag, "ERROR: " + message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        log(tag, "ERROR: " + message, exception);
    }

    @Override
    public void debug(String tag, String message) {
        log(tag, "DEBUG: " + message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        log(tag, "DEBUG: " + message, exception);
    }
}
