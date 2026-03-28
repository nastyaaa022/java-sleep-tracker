package ru.yandex.practicum.sleeptracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class SleepFileHandler {
    private final PrintWriter logFile;

    public SleepFileHandler(List<SleepingSession> sleepFilePath, PrintWriter logFile) throws SleepFileException {
        this.logFile = logFile;
        if (sleepFilePath == null) {
            logFile.println("logFile передан как null.");
            throw new SleepFileException("logFile не должен быть null.");
        }
    }

    public List<String> readSleepDataFromFile(String sleepFilePath) throws SleepFileException {
        logMessage("Начало чтения данных из файла: " + sleepFilePath);

        try {
            Path path = Paths.get(sleepFilePath);
            if (!Files.exists(path)) {
                logError("Файл не найден: " + sleepFilePath);
                throw new SleepFileException("Файл не найден: " + sleepFilePath);
            }
            return Files.lines(path)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logError("Ошибка при чтении файла: " + e.getMessage());
            throw new SleepFileException("Ошибка при чтении файла: " + e.getMessage());
        } finally {
            logMessage("Завершение чтения данных из файла.");
        }
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
