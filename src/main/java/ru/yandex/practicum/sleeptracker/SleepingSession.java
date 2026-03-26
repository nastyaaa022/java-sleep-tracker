package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String sleepQuality;

    public SleepingSession(LocalDateTime startTime, LocalDateTime endTime, String sleepQuality) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.sleepQuality = sleepQuality;
    }

    public String formatDateTimeFile(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        return dateTime.format(formatter);
    }


    //сеттеры и геттеры
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(String sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}