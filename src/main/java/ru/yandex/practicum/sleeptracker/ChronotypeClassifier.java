package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ChronotypeClassifier implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("Вы относитель к хронотипу",
                    "Список сессий пуст", logFile);
        }

        List<SleepingSession> nightSessions = sessions.stream()
                .filter(this::isNightSession)
                .collect(Collectors.toList());

        if (nightSessions.isEmpty()) {
            return new SleepAnalysisResult("Вы относитель к хронотипу",
                    "Нет ночных сессий сна", logFile);
        }

        AtomicInteger countOwl = new AtomicInteger();
        AtomicInteger countPigeon = new AtomicInteger();
        AtomicInteger countLark = new AtomicInteger();

        nightSessions.forEach(session -> {
            LocalTime endTime = session.getEndTime().toLocalTime();
            LocalTime startTime = session.getStartTime().toLocalTime();

            if (startTime.isAfter(LocalTime.of(23, 0)) &&
                    endTime.isAfter(LocalTime.of(9, 0))) {
                countOwl.getAndIncrement();
            } else if (startTime.isBefore(LocalTime.of(22, 0)) &&
                    endTime.isBefore(LocalTime.of(7, 0))) {
                countLark.getAndIncrement();
            } else {
                countPigeon.getAndIncrement();
            }
        });

        String chronotype = determineChronotype(countOwl.get(), countLark.get(), countPigeon.get());

        return new SleepAnalysisResult("Вы относитель к хронотипу", chronotype, logFile);
    }

    private boolean isNightSession(SleepingSession session) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalDateTime sessionEnd = session.getEndTime();

        LocalDate startDate = sessionStart.toLocalDate();
        LocalDate endDate = sessionEnd.toLocalDate();

        List<LocalDate> dates = generateDatesInRange(startDate, endDate);

        return dates.stream().anyMatch(date -> {
            LocalTime nightStart = LocalTime.of(0, 0);
            LocalTime nightEnd = LocalTime.of(6, 0);
            LocalDateTime nightStartDateTime = date.atTime(nightStart);
            LocalDateTime nightEndDateTime = date.atTime(nightEnd);

            return !sessionStart.isAfter(nightEndDateTime) &&
                    !sessionEnd.isBefore(nightStartDateTime);
        });
    }

    private String determineChronotype(int owlCount, int larkCount, int pigeonCount) {
        int maxCount = Math.max(Math.max(owlCount, larkCount), pigeonCount);

        boolean owlIsMax = owlCount == maxCount;
        boolean larkIsMax = larkCount == maxCount;
        boolean pigeonIsMax = pigeonCount == maxCount;

        if ((owlIsMax && larkIsMax) || (owlIsMax && pigeonIsMax) || (larkIsMax && pigeonIsMax)) {
            return "Голубь";
        }

        if (owlIsMax) {
            return "Сова";
        } else if (larkIsMax) {
            return "Жаворонок";
        } else {
            return "Голубь";
        }
    }

    private List<LocalDate> generateDatesInRange(LocalDate startDate, LocalDate endDate) {
        return startDate.datesUntil(endDate.plusDays(1))
                .collect(Collectors.toList());
    }
}
