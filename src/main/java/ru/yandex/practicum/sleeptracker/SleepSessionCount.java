package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.util.List;

public class SleepSessionCount implements SleepAnalysisFunction {

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("Количество сессий сна", "Список сессий пуст", logFile);
        }
        long count = sessions.size();
        return new SleepAnalysisResult("Количество сессий сна", count, logFile);
    }
}