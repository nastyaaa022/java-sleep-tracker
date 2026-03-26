package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.util.List;

public class MinimumSleepSessionDuration implements SleepAnalysisFunction {
    private static final int MINUTES_IN_AN_HOUR = 60;

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("минимальная продолжительность сессии (в минутах)",
                    "Список сессий пуст", logFile);
        }

        long minDuration = sessions.stream()
                .mapToLong(session -> session.getEndTime().toInstant(ZoneOffset.UTC).getEpochSecond()
                        - session.getStartTime().toInstant(ZoneOffset.UTC).getEpochSecond())
                .min()
                .orElse(0L);

        long minDurationInMinutes = minDuration / MINUTES_IN_AN_HOUR;

        return new SleepAnalysisResult("минимальная продолжительность сессии (в минутах)",
                minDurationInMinutes, logFile);
    }
}
