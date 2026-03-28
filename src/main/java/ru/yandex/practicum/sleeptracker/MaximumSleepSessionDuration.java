package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.util.List;

public class MaximumSleepSessionDuration implements SleepAnalysisFunction {
    private static final int MINUTES_IN_AN_HOUR = 60;

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("максимальная продолжительность сессии (в минутах)",
                    "Список сессий пуст", logFile);
        }
        long maxDuration = sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .mapToLong(session -> session.getEndTime().toInstant(ZoneOffset.UTC).getEpochSecond()
                        - session.getStartTime().toInstant(ZoneOffset.UTC).getEpochSecond())
                .max()
                .orElse(0L);
        long maxDurationInMinutes = maxDuration / MINUTES_IN_AN_HOUR;

        return new SleepAnalysisResult("максимальная продолжительность сессии (в минутах)",
                maxDurationInMinutes, logFile);

    }
}
