package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.util.List;

public class AverageSleepDuration implements SleepAnalysisFunction {
    private static final int SECONDS_IN_AN_HOUR = 3600;
    private static final int SECONDS_IN_A_MINUTE = 60;

    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("Среднее время сна", "Список сессий пуст", logFile);
        }
        long totalDuration = sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .mapToLong(session -> session.getEndTime().toInstant(ZoneOffset.UTC)
                        .getEpochSecond() - session.getStartTime().toInstant(ZoneOffset.UTC).getEpochSecond())
                .sum();

        long averageDurationSeconds = totalDuration / sessions.size();
        long hours = averageDurationSeconds / SECONDS_IN_AN_HOUR;
        long minutes = (averageDurationSeconds % SECONDS_IN_AN_HOUR) / SECONDS_IN_A_MINUTE;

        return new SleepAnalysisResult("Среднее время сна",
                String.format("%d часов %d минут", hours, minutes), logFile);
    }
}
