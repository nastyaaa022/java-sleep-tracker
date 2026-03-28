package ru.yandex.practicum.sleeptracker;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SleeplessNightsDetector implements SleepAnalysisFunction {
    @Override
    public SleepAnalysisResult analyze(List<SleepingSession> sessions, PrintWriter logFile) {
        if (sessions.isEmpty()) {
            logMessage(logFile, "Список сессий пуст");
            return new SleepAnalysisResult("количество бессонных ночей",
                    "Список сессий пуст", logFile);
        }

        SleepingSession firstSession = sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .min(Comparator.comparing(SleepingSession::getStartTime))
                .orElseThrow(() -> new IllegalArgumentException("Список сессий должен быть непустым"));

        SleepingSession lastSession = sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .max(Comparator.comparing(SleepingSession::getEndTime))
                .orElseThrow(() -> new IllegalArgumentException("Список сессий должен быть непустым"));

        LocalDate firstCheckedDate = determineFirstCheckedDate(firstSession);

        LocalDate lastCheckedDate;

        if (sessions.size() == 1) {
            lastCheckedDate = firstCheckedDate;
        } else {
            lastCheckedDate = lastSession.getEndTime().toLocalDate();
        }

        if (firstCheckedDate.isAfter(lastCheckedDate)) {
            return new SleepAnalysisResult(
                    "количество бессонных ночей",
                    " 0 из 0 ночей", logFile
            );
        }

        List<LocalDate> datesToCheck = generateDatesInRange(firstCheckedDate, lastCheckedDate);

        long totalNights = datesToCheck.size();

        long sleeplessCount = datesToCheck.stream()
                .filter(date -> isSleeplessNight(date, sessions))
                .count();

        return new SleepAnalysisResult(
                "количество бессонных ночей",
                String.format(" %d из %d ночей", sleeplessCount, totalNights), logFile
        );
    }

    private LocalDate determineFirstCheckedDate(SleepingSession firstSession) {
        LocalDateTime firstSessionStart = firstSession.getStartTime();
        LocalTime noon = LocalTime.of(12, 0);

        if (firstSessionStart.toLocalTime().isAfter(noon)) {
            return firstSessionStart.toLocalDate().plusDays(1);
        } else {
            return firstSessionStart.toLocalDate().minusDays(1);
        }
    }

    private boolean isSleeplessNight(LocalDate date, List<SleepingSession> sessions) {
        LocalTime nightStart = LocalTime.of(0, 0);
        LocalTime nightEnd = LocalTime.of(6, 0);
        LocalDateTime nightStartDateTime = date.atTime(nightStart);
        LocalDateTime nightEndDateTime = date.atTime(nightEnd);

        return sessions.stream()
                .filter(sleepingSession -> sleepingSession != null && sleepingSession.getEndTime() != null)
                .noneMatch(session -> intersectsWithNightInterval(session, nightStartDateTime, nightEndDateTime));
    }

    private boolean intersectsWithNightInterval(SleepingSession session,
                                                LocalDateTime nightStart, LocalDateTime nightEnd) {
        LocalDateTime sessionStart = session.getStartTime();
        LocalDateTime sessionEnd = session.getEndTime();

        return !sessionStart.isAfter(nightEnd) && !sessionEnd.isBefore(nightStart);
    }

    private List<LocalDate> generateDatesInRange(LocalDate startDate, LocalDate endDate) {
        long numOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());
    }
}
