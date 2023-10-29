package dev.training.the_riddle.data.source_reader;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import dev.training.the_riddle.app_system.AppInstance;
import dev.training.the_riddle.data.local.access.Repository;
import dev.training.the_riddle.data.local.entities.Level;
import dev.training.the_riddle.data.local.entities.Riddle;

public class GameAssetsReader {
    private static final String TAG = "GameAssetsReader";

    //    private final Repository repository ;
    private final Repository repository;

    private GameAssetsReader() {
//        repository = new Repository(AppController.getInstance());
        repository = Repository.INSTANCE;
    }

    private volatile static GameAssetsReader INSTANCE;

    public static synchronized GameAssetsReader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameAssetsReader();
        }
        return INSTANCE;
    }


    private static String loadJSONFromAsset() {
        String json;
        try (InputStream inputStream = AppInstance.getInstance().getAssets().open("puzzleGameData.json")) {
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            int readied = inputStream.read(buffer);
            Log.d("Usage of read method", "int readied = inputStream.read(buffer); returned: " + readied);

            /*`
            int readied = inputStream.read(buffer);
            Log.d("Usage of read method", "int readied = inputStream.read(buffer); returned: " + readied);*/
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void readRiddles() {
        String id, title, hintAnswer, duration_value, point_value, ans1, ans2, ans3, ans4, trueAnswer;
        //******************************
        String level_num_, level_minPoint_;
        //******************************
        try {
            var wholeJsonArray = new JSONArray(loadJSONFromAsset());

            //..FirstLoop #1#
            for (int levelCounter = 0; levelCounter < wholeJsonArray.length(); levelCounter++) {
                // get Level Info
                level_num_ = wholeJsonArray.getJSONObject(levelCounter).getString("level_no");
                level_minPoint_ = wholeJsonArray.getJSONObject(levelCounter).getString("unlock_points");

                //..Parsing ************
                int level_num = Integer.parseInt(level_num_);
                int level_minPoint = Integer.parseInt(level_minPoint_);

                //..Insert
                var obj = new Level(level_num, level_minPoint, null, null);
                repository.insert(obj);
                Log.d(TAG, "Level =>: " + obj + "\n");

                // ReadQuestions..
                var riddlesInnerJsonArray = wholeJsonArray.getJSONObject(levelCounter).getJSONArray("questions");

                //..SecondLoop #2# looping through questions
                for (int riddleCounter = 0; riddleCounter < riddlesInnerJsonArray.length(); riddleCounter++) {

                    id = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("id");
                    title = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("title");
                    trueAnswer = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("true_answer");
                    hintAnswer = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("hint");
                    //****************
                    duration_value = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("duration");
                    point_value = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("points");
                    //------------------
                    ans1 = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("answer_1");
                    ans2 = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("answer_2");
                    ans3 = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("answer_3");
                    ans4 = riddlesInnerJsonArray.getJSONObject(riddleCounter).getString("answer_4");
                    //------------------
                    String patternObject_id = riddlesInnerJsonArray.getJSONObject(riddleCounter).getJSONObject("pattern").getString("pattern_id");
                    // String patternObject_name = json_questionsArray.getJSONObject(i).getString("pattern_name");

                    //..Parsing ************
                    int id_ = Integer.parseInt(id);
                    int pattern_id = Integer.parseInt(patternObject_id);
                    int duration_actual = Integer.parseInt(duration_value);
                    int point_actual = Integer.parseInt(point_value);

                    var rObj = new Riddle(id_, title, point_actual, duration_actual, ans1, ans2, ans3, ans4, trueAnswer, hintAnswer, pattern_id, level_num);
                    //..Insert
                    repository.insert(rObj);
                    Log.d(TAG, "Riddle: " + rObj + "\n");

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
