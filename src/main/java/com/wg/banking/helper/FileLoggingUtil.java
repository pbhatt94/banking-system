package com.wg.banking.helper;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLoggingUtil {
    private static final Logger logger = Logger.getLogger(LoggingUtil.class.getName());

    static {
        // Setup file handler
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("application.log", true); 
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to set up file handler.");
            e.printStackTrace();
        }

        if (fileHandler != null) {
            logger.addHandler(fileHandler);
        }

        // Set logger level
        logger.setLevel(Level.ALL);
    }

    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
}
