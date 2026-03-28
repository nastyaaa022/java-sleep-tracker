package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.util.List;

public class PoorQualitySleepSessionsCount implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("количество сессий с плохим качеством сна",
                    "Список сессий пуст", logFile);
        }
        long badDuration = sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .filter(session -> session.getSleepQuality().equals("BAD"))
                .count();
        return new SleepAnalysisResult("количество сессий с плохим качеством сна", badDuration, logFile);
    }
}
