package dev.training.the_riddle.app_system.interfaces;

public interface AnswerCallback {
    void onSuccess(int riddleId);
    void onFailed(int riddleId);
}
