package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.ZoneOffset;
import java.util.List;

public class SleepAnalysisResult {
    private final String description;
    private final Object result;
    private final PrintWriter logFile;

    public SleepAnalysisResult(String description, Object result, PrintWriter logFile) {
        this.description = description;
        this.result = result;
        this.logFile = logFile;
    }

    public SleepAnalysisResult analyze(List<SleepingSession> sessions) throws SleepFileException {
        logMessage("Начало анализа сессий сна.");

        if (sessions.isEmpty()) {
            logError("Список сессий пуст.");
            return new SleepAnalysisResult("Среднее время сна в секундах", 0, logFile);
        }

        try {
            long totalDuration = sessions.stream()
                    .mapToLong(session -> session.getEndTime().toInstant(ZoneOffset.UTC)
                            .getEpochSecond() - session.getStartTime().toInstant(ZoneOffset.UTC).getEpochSecond())
                    .sum();

            logMessage("Анализ завершен. Общее количество сессий: " + sessions.size());
            return new SleepAnalysisResult("Среднее время сна в секундах", totalDuration / sessions.size(), logFile);
        } catch (Exception e) {
            throw new SleepFileException("Ошибка при анализе сессий: " + e.getMessage());
        }
    }

    public String getDescription() {
        return description;
    }

    public Object getResult() {
        return result;
    }

    public void logError(String errorMessage) {
        logFile.println("Ошибка: " + errorMessage);
        logFile.flush();
    }

    public void logMessage(String message) {
        logFile.println(message);
        logFile.flush();
    }
}
