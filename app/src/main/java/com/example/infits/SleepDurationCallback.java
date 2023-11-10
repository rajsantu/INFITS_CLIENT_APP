package com.example.infits;

public interface SleepDurationCallback {
    void onSleepDurationReceived(String sleepDuration);
    void onError(String errorMessage);
}
