package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {
    private final List<SleepAnalysisFunction> functions;

    public SleepTrackerApp() {
        functions = new ArrayList<>();
    }

    public void addFunction(SleepAnalysisFunction function) {
        functions.add(function);
    }

    public List<SleepAnalysisResult> executeFunctions(List<SleepingSession> sessions, PrintWriter logFile) {
        return functions.stream()
                .map(function -> function.analyze(sessions, logFile))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws SleepFileException {
        List<SleepingSession> sessions = new ArrayList<>();
        PrintWriter logFile = null;
        try {
            logFile = new PrintWriter(Paths.get("log.txt").toFile());
            SleepFileHandler sleepFileHandler = new SleepFileHandler(sessions, logFile);
            List<String> lines = sleepFileHandler.readSleepDataFromFile("src/main/resources/sleep_log.txt");
            sessions = lines.stream()
                    .map(line -> {
                        String[] parts = line.split(";");
                        if (parts.length == 3) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
                            LocalDateTime startTime = LocalDateTime.parse(parts[0], formatter);
                            LocalDateTime endTime = LocalDateTime.parse(parts[1], formatter);
                            String sleepQuality = parts[2];
                            return new SleepingSession(startTime, endTime, sleepQuality);
                        } else {
                            return null;
                        }
                    })
                    .filter(session -> session != null)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logFile != null) {
                logFile.close();
            }
        }

        SleepTrackerApp app = new SleepTrackerApp();
        app.addFunction(new SleepSessionCount());
        app.addFunction(new AverageSleepDuration());
        app.addFunction(new MinimumSleepSessionDuration());
        app.addFunction(new MaximumSleepSessionDuration());
        app.addFunction(new PoorQualitySleepSessionsCount());
        app.addFunction(new SleeplessNightsDetector());
        app.addFunction(new ChronotypeClassifier());

        System.out.println("Добро пожаловать в 'Sleep Tracker Analyzer'!");
        System.out.println("---------------------------------------------");
        System.out.println("Начинаем анализ ваших сессий сна...");

        List<SleepAnalysisResult> results = app.executeFunctions(sessions, logFile);

        System.out.println("Результаты анализа:");
        results.stream()
                .forEach(result -> System.out.println(result.getDescription()
                        + ": " + result.getResult()));
        System.out.println("---------------------------------------------");
        System.out.println("Спасибо за использование 'Sleep Tracker Analyzer'. " +
                "Надеемся, результаты были полезны. Приятных снов и до новых встреч!");
    }
}