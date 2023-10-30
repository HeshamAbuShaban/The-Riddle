package dev.training.the_riddle.app_system.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import dev.training.the_riddle.app_system.AppInstance;

public class ScoreSharedPreferences {
    private enum SharedKeys {
        SCORE_KEY,
    }

    public SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    private static volatile ScoreSharedPreferences Instance;

    private ScoreSharedPreferences() {
        sharedPreferences = AppInstance.getInstance().getApplicationContext().getSharedPreferences("ScoreSharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized ScoreSharedPreferences getInstance() {
        if (Instance == null) {
            Instance = new ScoreSharedPreferences();
        }
        return Instance;
    }


    public void putScore(int integer) {
        editor.putInt(SharedKeys.SCORE_KEY.name(), integer);
        editor.apply();
    }

    public void removeValueOfKey(String key) {
        editor.remove(key).apply();
    }

    public int getScore() {
        return sharedPreferences.getInt(SharedKeys.SCORE_KEY.name(), 0);
    }

}
