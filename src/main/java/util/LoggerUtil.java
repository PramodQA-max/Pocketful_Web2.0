package util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static boolean initialized = false;

    public static Logger getLogger(Class<?> clazz) {
        if (!initialized) {
            initializeLogger();
            initialized = true;
        }
        return Logger.getLogger(clazz.getName());
    }

    private static void initializeLogger() {
        try {
            // Create a FileHandler
            FileHandler fileHandler = new FileHandler("src/web.log", true); // Adjust the path as needed
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new CustomFormatter());

            // Get the root logger
            Logger rootLogger = Logger.getLogger("");
            rootLogger.setLevel(Level.ALL);

            // Remove default handlers
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }

            // Add custom file handler only
            rootLogger.addHandler(fileHandler);

            // Set logging levels for specific loggers
            Logger freemarkerLogger = Logger.getLogger("freemarker");
            freemarkerLogger.setLevel(Level.WARNING); // Set to WARNING level to suppress FINE messages

            Logger shutdownLogger = Logger.getLogger("java.lang.Shutdown");
            shutdownLogger.setLevel(Level.WARNING); // Set to WARNING level to suppress FINE messages
        } catch (IOException e) {
            Logger.getLogger(LoggerUtil.class.getName()).log(Level.SEVERE, "Error setting up logger", e);
        }
    }

    private static class CustomFormatter extends SimpleFormatter {
        @Override
        public synchronized String format(java.util.logging.LogRecord record) {
            return String.format("[%1$tF %1$tT] [%2$s] [%3$s.%4$s] %5$s%n",
                    new java.util.Date(record.getMillis()),
                    record.getLevel().getName(),
                    record.getSourceClassName(),
                    record.getSourceMethodName(),
                    formatMessage(record));
        }
    }
}
