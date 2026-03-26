package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {
    PrintWriter logFile;

    public SleepTrackerAppTest() {
        try {
            logFile = new PrintWriter("log.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    LocalDateTime startTime = LocalDateTime.of(2025, 10, 1, 23, 0);
    LocalDateTime endTime = LocalDateTime.of(2025, 10, 2, 7, 0);
    String sleepQuality = "GOOD"; //

    LocalDateTime startTime1 = LocalDateTime.of(2025, 10, 2, 23, 0);
    LocalDateTime endTime1 = LocalDateTime.of(2025, 10, 3, 6, 30);
    String sleepQuality1 = "NORMAL";

    LocalDateTime startTime2 = LocalDateTime.of(2025, 10, 4, 3, 40);
    LocalDateTime endTime2 = LocalDateTime.of(2025, 10, 4, 8, 0);
    String sleepQuality2 = "BAD";

    List<SleepingSession> sessions = new ArrayList<>();

    @Test
    public void testSleepSessionCount() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality));
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1));

        SleepAnalysisFunction countFunction = new SleepSessionCount();
        SleepAnalysisResult result = countFunction.analyze(sessions, logFile);

        assertEquals("Количество сессий сна", result.getDescription());
        assertEquals(2, ((Long) result.getResult()).intValue());
        logFile.close();
    }

    @Test
    public void testSleepSessionCountNull() {
        try {
            PrintWriter logFile = new PrintWriter("log.txt");
            SleepAnalysisFunction countFunction = new SleepSessionCount();
            SleepAnalysisResult result = countFunction.analyze(sessions, logFile);

            assertEquals("Количество сессий сна", result.getDescription());
            assertEquals("Список сессий пуст", result.getResult());

            logFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAverageSleepDuration() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality));
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1));

        AverageSleepDuration averageSleepDuration = new AverageSleepDuration();
        SleepAnalysisResult result = averageSleepDuration.analyze(sessions, logFile);

        assertEquals("Среднее время сна", result.getDescription());
        assertEquals("7 часов 45 минут", result.getResult());
        logFile.close();
    }

    @Test
    public void testAverageSleepDurationNull() {
        AverageSleepDuration averageSleepDuration = new AverageSleepDuration();
        SleepAnalysisResult result = averageSleepDuration.analyze(sessions, logFile);

        assertEquals("Среднее время сна", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }

    @Test
    public void testAverageSleepDurationMore() {
        LocalDateTime startTimeM = LocalDateTime.of(2025, 10, 3, 1, 40);
        LocalDateTime endTimeM = LocalDateTime.of(2025, 10, 5, 8, 0);
        String sleepQualityM = "BAD";
        sessions.add(new SleepingSession(startTimeM, endTimeM, sleepQualityM));

        AverageSleepDuration averageSleepDuration = new AverageSleepDuration();
        SleepAnalysisResult result = averageSleepDuration.analyze(sessions, logFile);

        assertEquals("Среднее время сна", result.getDescription());
        assertEquals("54 часов 20 минут", result.getResult());
        logFile.close();
    }

    @Test
    public void testAverageSleepDurationMin() {
        LocalDateTime startTimeM = LocalDateTime.of(2025, 10, 3, 1, 40);
        LocalDateTime endTimeM = LocalDateTime.of(2025, 10, 3, 1, 45);
        String sleepQualityM = "BAD";
        sessions.add(new SleepingSession(startTimeM, endTimeM, sleepQualityM));

        AverageSleepDuration averageSleepDuration = new AverageSleepDuration();
        SleepAnalysisResult result = averageSleepDuration.analyze(sessions, logFile);

        assertEquals("Среднее время сна", result.getDescription());
        assertEquals("0 часов 5 минут", result.getResult());
        logFile.close();
    }

    @Test
    public void testMinimumSleepSessionDuration() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality));
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1));

        MinimumSleepSessionDuration minSleepSession = new MinimumSleepSessionDuration();
        SleepAnalysisResult result = minSleepSession.analyze(sessions, logFile);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals(450, ((Long) result.getResult()).intValue());
        logFile.close();
    }

    @Test
    public void testMinimumSleepSessionDurationNull() {
        MinimumSleepSessionDuration minSleepSession = new MinimumSleepSessionDuration();
        SleepAnalysisResult result = minSleepSession.analyze(sessions, logFile);

        assertEquals("минимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }

    @Test
    public void testMaximumSleepSessionDuration() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality)); //8hh - 480mm
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1)); //7:30 -450mm

        MaximumSleepSessionDuration maxSleepSession = new MaximumSleepSessionDuration();
        SleepAnalysisResult result = maxSleepSession.analyze(sessions, logFile);

        assertEquals("максимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals(480, ((Long) result.getResult()).intValue());
        logFile.close();
    }

    @Test
    public void testMaximumSleepSessionDurationNull() {
        MaximumSleepSessionDuration maxSleepSession = new MaximumSleepSessionDuration();
        SleepAnalysisResult result = maxSleepSession.analyze(sessions, logFile);

        assertEquals("максимальная продолжительность сессии (в минутах)", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }

    @Test
    public void testPoorQualitySleepSessionsCount() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality));
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1));
        sessions.add(new SleepingSession(startTime2, endTime2, sleepQuality2));

        PoorQualitySleepSessionsCount poorQualitySleepSessions = new PoorQualitySleepSessionsCount();
        SleepAnalysisResult result = poorQualitySleepSessions.analyze(sessions, logFile);

        assertEquals("количество сессий с плохим качеством сна", result.getDescription());
        assertEquals(1, ((Long) result.getResult()).intValue());
        logFile.close();
    }

    @Test
    public void testPoorQualitySleepSessionsCountNull() {

        PoorQualitySleepSessionsCount poorQualitySleepSessions = new PoorQualitySleepSessionsCount();
        SleepAnalysisResult result = poorQualitySleepSessions.analyze(sessions, logFile);

        assertEquals("количество сессий с плохим качеством сна", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }

    @Test
    public void testPoorQualitySleepSessionsCount0() {
        sessions.add(new SleepingSession(startTime, endTime, sleepQuality));
        sessions.add(new SleepingSession(startTime1, endTime1, sleepQuality1));

        PoorQualitySleepSessionsCount poorQualitySleepSessions = new PoorQualitySleepSessionsCount();
        SleepAnalysisResult result = poorQualitySleepSessions.analyze(sessions, logFile);

        assertEquals("количество сессий с плохим качеством сна", result.getDescription());
        assertEquals(0, ((Long) result.getResult()).intValue());
        logFile.close();
    }

    @Test
    public void testSleeplessNightsDetector() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 3, 18, 0),
                LocalDateTime.of(2025, 10, 3, 23, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 4, 7, 0),
                LocalDateTime.of(2025, 10, 4, 16, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 6, 1, 0),
                LocalDateTime.of(2025, 10, 6, 10, 0), sleepQuality));

        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        SleepAnalysisResult result = detector.analyze(sessions, logFile);

        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals(" 3 из 5 ночей", result.getResult());
        logFile.close();
    }

    @Test
    public void testSleeplessNightsDetectorMonth() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 9, 30, 1, 0),
                LocalDateTime.of(2025, 9, 30, 7, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 1, 0),
                LocalDateTime.of(2025, 10, 1, 10, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 3, 1, 0),
                LocalDateTime.of(2025, 10, 3, 15, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 5, 1, 0),
                LocalDateTime.of(2025, 10, 5, 10, 0), sleepQuality));

        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        SleepAnalysisResult result = detector.analyze(sessions, logFile);

        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals(" 3 из 7 ночей", result.getResult());
        logFile.close();
    }

    @Test
    public void testSingleSessionBeforeNoon() {
        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 8, 0),
                LocalDateTime.of(2025, 10, 1, 20, 0), sleepQuality));
        SleepAnalysisResult result = detector.analyze(sessions, logFile);
        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals(" 1 из 1 ночей", result.getResult());
        logFile.close();
    }

    @Test
    public void testSingleSessionAfterNoon() {
        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0), sleepQuality));
        SleepAnalysisResult result = detector.analyze(sessions, logFile);
        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals(" 0 из 1 ночей", result.getResult());
        logFile.close();
    }


    @Test
    public void testSleeplessNightsDetectorZero() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 0),
                LocalDateTime.of(2025, 10, 2, 7, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 10, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 3, 23, 0),
                LocalDateTime.of(2025, 10, 4, 15, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 4, 23, 0),
                LocalDateTime.of(2025, 10, 5, 10, 0), sleepQuality));

        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        SleepAnalysisResult result = detector.analyze(sessions, logFile);

        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals(" 0 из 4 ночей", result.getResult());
        logFile.close();
    }

    @Test
    public void testSleeplessNightsDetectorNull() {
        SleeplessNightsDetector detector = new SleeplessNightsDetector();
        SleepAnalysisResult result = detector.analyze(sessions, logFile);

        assertEquals("количество бессонных ночей", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }


    //надо еще 1 теста

    //тест на сов и жаворонков
    @Test
    public void testChronotypeClassifierLark() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 20, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 2, 20, 0),
                LocalDateTime.of(2025, 10, 2, 5, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 3, 1, 0),
                LocalDateTime.of(2025, 10, 3, 1, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 5, 1, 0),
                LocalDateTime.of(2025, 10, 5, 8, 0), sleepQuality));

        ChronotypeClassifier chronotype = new ChronotypeClassifier();
        SleepAnalysisResult result = chronotype.analyze(sessions, logFile);

        assertEquals("Вы относитель к хронотипу", result.getDescription());
        assertEquals("Жаворонок", result.getResult());
        logFile.close();
    }

    @Test
    public void testChronotypeClassifierOwl() {
        List<SleepingSession> sessions = new ArrayList<>();
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 1, 23, 50),
                LocalDateTime.of(2025, 10, 2, 10, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 10, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 3, 20, 50),
                LocalDateTime.of(2025, 10, 4, 5, 0), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 4, 23, 50),
                LocalDateTime.of(2025, 10, 5, 10, 0), sleepQuality));

        ChronotypeClassifier chronotype = new ChronotypeClassifier();
        SleepAnalysisResult result = chronotype.analyze(sessions, logFile);

        assertEquals("Вы относитель к хронотипу", result.getDescription());
        assertEquals("Сова", result.getResult());
        logFile.close();
    }

    @Test
    public void testChronotypeClassifierPigeon() {
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 2, 20, 0),
                LocalDateTime.of(2025, 10, 2, 5, 30), sleepQuality));
        sessions.add(new SleepingSession(LocalDateTime.of(2025, 10, 2, 23, 0),
                LocalDateTime.of(2025, 10, 3, 10, 0), sleepQuality));

        ChronotypeClassifier chronotype = new ChronotypeClassifier();
        SleepAnalysisResult result = chronotype.analyze(sessions, logFile);

        assertEquals("Вы относитель к хронотипу", result.getDescription());
        assertEquals("Голубь", result.getResult());
        logFile.close();
    }

    @Test
    public void testChronotypeClassifierNull() {
        ChronotypeClassifier chronotype = new ChronotypeClassifier();
        SleepAnalysisResult result = chronotype.analyze(sessions, logFile);

        assertEquals("Вы относитель к хронотипу", result.getDescription());
        assertEquals("Список сессий пуст", result.getResult());
        logFile.close();
    }
}