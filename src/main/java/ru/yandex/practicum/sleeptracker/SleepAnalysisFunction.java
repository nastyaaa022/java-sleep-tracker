package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.util.List;

public interface SleepAnalysisFunction {
    SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile);

    default void logMessage(PrintWriter logFile, String message) {
        logFile.println(message);
        logFile.flush();
    }

    default void logError(PrintWriter logFile, String errorMessage) {
        logMessage(logFile, "Ошибка: " + errorMessage);
    }
}