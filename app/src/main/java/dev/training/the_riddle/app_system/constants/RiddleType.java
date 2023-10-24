package dev.training.the_riddle.app_system.constants;

public enum RiddleType {

    TrueOrFalse(1),
    ChooseTheCorrectAnswer(2),
    CompleteTheSentence(3);

    public final int riddleTypeNum;

    RiddleType(int riddleTypeNum){
        this.riddleTypeNum = riddleTypeNum;
    }
}
