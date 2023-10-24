package dev.training.the_riddle.app_system.interfaces;

public interface TimerListener {
    void setTimerTickDuration(long remainingTime);

    void onTimerFinished();
}
