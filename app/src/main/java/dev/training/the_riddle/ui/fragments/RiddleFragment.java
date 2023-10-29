package dev.training.the_riddle.ui.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;
import java.util.Objects;

import dev.training.the_riddle.R;
import dev.training.the_riddle.app_system.constants.RiddleType;
import dev.training.the_riddle.app_system.interfaces.AnswerCallback;
import dev.training.the_riddle.app_system.interfaces.FragmentAskForSkipListener;
import dev.training.the_riddle.app_system.interfaces.TimerListener;

public class RiddleFragment extends Fragment {
    // ThirdParty Objects...
    private MediaPlayer mediaPlayerSuccess, mediaPlayerFailed;

    // Listeners...
    private AnswerCallback answerCallback;
    private TimerListener timerListener;
    private FragmentAskForSkipListener fragmentAskForSkipListener;

    // KEYS...
    private static final String ARG_RIDDLE_ID = "riddleId";
    private static final String ARG_RIDDLE_TIME_BY_SEC = "timeBySec";
    private static final String ARG_RIDDLE_TYPE = "riddleType";
    private static final String
            ARG_RIDDLE_Q = "riddleQ", ARG_RIDDLE_ANS1 = "riddleA1",
            ARG_RIDDLE_ANS2 = "riddleA2", ARG_RIDDLE_ANS3 = "riddleA3",
            ARG_RIDDLE_ANS4 = "riddleA4";

    private static final String ARG_RIDDLE_IS_TRUE = "riddleTrue";
    private static final String ARG_RIDDLE_RIGHT_ANS = "riddleTRUE_ANSWER_IN_TEXT";

    // Containers..
    private int riddleId, riddleType;
    private String riddleQ;
    private boolean isTheQuestionTrue;
    private String ans1, ans2, ans3, ans4;
    private String theRightAnswer;
    private long riddleTimeBySec;

    // lifecycle..init
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (mediaPlayerSuccess == null && mediaPlayerFailed == null) {
            mediaPlayerSuccess = MediaPlayer.create(context, R.raw.riddle_bonus_collect);
            mediaPlayerFailed = MediaPlayer.create(context, R.raw.riddle_negative_answer);
        }

        try {
            answerCallback = (AnswerCallback) getParentFragment();
            timerListener = (TimerListener)  getParentFragment();

            fragmentAskForSkipListener = (FragmentAskForSkipListener)  getParentFragment();

        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement AnswerCallback ");
        }
    }


    // Constructors.

    //1.true or false
    public static RiddleFragment newInstance(
            int riddleId, String riddleQ,
            boolean isTheQuestionTrue,
            long riddleTimeBySec
    ) {
        var fragment = new RiddleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RIDDLE_TYPE, RiddleType.TrueOrFalse.riddleTypeNum);
        args.putInt(ARG_RIDDLE_ID, riddleId);
        args.putLong(ARG_RIDDLE_TIME_BY_SEC, riddleTimeBySec);
        args.putString(ARG_RIDDLE_Q, riddleQ);
        args.putBoolean(ARG_RIDDLE_IS_TRUE, isTheQuestionTrue);
        fragment.setArguments(args);
        return fragment;
    }

    // 2.choose
    public static RiddleFragment newInstance(
            int riddleId, String riddleQ,
            String ans1, String ans2, String ans3, String ans4,
            String theRightAnswer, long riddleTimeBySec) {
        var fragment = new RiddleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RIDDLE_TYPE, RiddleType.ChooseTheCorrectAnswer.riddleTypeNum);
        args.putInt(ARG_RIDDLE_ID, riddleId);
        args.putLong(ARG_RIDDLE_TIME_BY_SEC, riddleTimeBySec);
        args.putString(ARG_RIDDLE_Q, riddleQ);
        args.putString(ARG_RIDDLE_RIGHT_ANS, theRightAnswer);
        args.putString(ARG_RIDDLE_ANS1, ans1);
        args.putString(ARG_RIDDLE_ANS2, ans2);
        args.putString(ARG_RIDDLE_ANS3, ans3);
        args.putString(ARG_RIDDLE_ANS4, ans4);
        fragment.setArguments(args);
        return fragment;
    }

    //complete
    public static RiddleFragment newInstance(
            int riddleId, String riddleQ,
            String theRightAnswer, long riddleTimeBySec) {
        var fragment = new RiddleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RIDDLE_TYPE, RiddleType.CompleteTheSentence.riddleTypeNum);
        args.putInt(ARG_RIDDLE_ID, riddleId);
        args.putLong(ARG_RIDDLE_TIME_BY_SEC, riddleTimeBySec);
        args.putString(ARG_RIDDLE_Q, riddleQ);
        args.putString(ARG_RIDDLE_RIGHT_ANS, theRightAnswer);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            //get Type
            riddleType = args.getInt(ARG_RIDDLE_TYPE);
            //id re
            riddleId = args.getInt(ARG_RIDDLE_ID);
            //R_text re
            riddleQ = args.getString(ARG_RIDDLE_Q);

            //true or false
            isTheQuestionTrue = args.getBoolean(ARG_RIDDLE_IS_TRUE);

            //choose
            ans1 = args.getString(ARG_RIDDLE_ANS1);
            ans2 = args.getString(ARG_RIDDLE_ANS2);
            ans3 = args.getString(ARG_RIDDLE_ANS3);
            ans4 = args.getString(ARG_RIDDLE_ANS4);

            //complete && choose for Checking with UI Answer if equal add score
            theRightAnswer = args.getString(ARG_RIDDLE_RIGHT_ANS);

            //****************************************************************************

            riddleTimeBySec = args.getLong(ARG_RIDDLE_TIME_BY_SEC, 10);

            //****************************************************************************

        }
    }


    private CountDownTimer countDownTimer;

    private void createATimerForRiddle() {
        countDownTimer = new CountDownTimer(riddleTimeBySec, 1) {
            @Override
            public void onTick(long remain) {
                timerListener.setTimerTickDuration(remain);
            }

            @Override
            public void onFinish() {
                timerListener.onTimerFinished();
            }
        }.start();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        createATimerForRiddle();

        // Basic Views
        FloatingActionButton fabSkip;
        TextView textViewQ;
        RadioButton radioButton_ans1, radioButton_ans2, radioButton_ans3, radioButton_ans4;
        Button btnTrue, btnFalse, btnSubmit;

        //*********************[Start InflatingViews]******************************================

        //inflate RiddleType.TrueOrFalse
        if (riddleType == RiddleType.TrueOrFalse.riddleTypeNum) {
            var viewTrue = inflater.inflate(R.layout.fragment_riddle_true, container, false);
            // Inflate the layout for this fragment
            fabSkip = viewTrue.findViewById(R.id.skip_f_act_btn_trueOrFalse);
            textViewQ = viewTrue.findViewById(R.id.textView_frg_Q_true);
            btnTrue = viewTrue.findViewById(R.id.btn_frag_true);
            btnFalse = viewTrue.findViewById(R.id.btn_frag_false);

            fabSkip.setOnClickListener(view -> {
                countDownTimer.cancel();
                fragmentAskForSkipListener.onFloatingBtnSkipClicked();
            });

            btnTrue.setOnClickListener(view -> {
                if (isTheQuestionTrue) {
                    countDownTimer.cancel();
                    mediaPlayerSuccess.start();
                    answerCallback.onSuccess(riddleId);
                } else {
                    countDownTimer.cancel();
                    mediaPlayerFailed.start();
                    answerCallback.onFailed(riddleId);
                }
            });
            btnFalse.setOnClickListener(view -> {
                if (!isTheQuestionTrue) {
                    countDownTimer.cancel();
                    mediaPlayerSuccess.start();
                    answerCallback.onSuccess(riddleId);
                } else {
                    countDownTimer.cancel();
                    mediaPlayerFailed.start();
                    answerCallback.onFailed(riddleId);
                }
            });

            //set Value
            textViewQ.setText(riddleQ);

            return viewTrue;
        }

        //inflate RiddleType.ChooseTheCorrectAnswer
        else if (riddleType == RiddleType.ChooseTheCorrectAnswer.riddleTypeNum) {
            var viewChooses = inflater.inflate(R.layout.fragment_riddle_choose, container, false);
            // Inflate the layout for this fragment
            fabSkip = viewChooses.findViewById(R.id.skip_f_act_btn_choose);
            textViewQ = viewChooses.findViewById(R.id.textView_frag_Q);
            radioButton_ans1 = viewChooses.findViewById(R.id.radioButton_ans1);
            radioButton_ans2 = viewChooses.findViewById(R.id.radioButton_ans2);
            radioButton_ans3 = viewChooses.findViewById(R.id.radioButton_ans3);
            radioButton_ans4 = viewChooses.findViewById(R.id.radioButton_ans4);
            btnSubmit = viewChooses.findViewById(R.id.btn_frag_choose_submit);

            fabSkip.setOnClickListener(view -> {
                countDownTimer.cancel();
                fragmentAskForSkipListener.onFloatingBtnSkipClicked();
            });

            btnSubmit.setOnClickListener(view -> {
                countDownTimer.cancel();
                String userAnswer = null;
                if (radioButton_ans1.isChecked())
                    userAnswer = radioButton_ans1.getText().toString();
                else if (radioButton_ans2.isChecked())
                    userAnswer = radioButton_ans2.getText().toString();

                else if (radioButton_ans3.isChecked())
                    userAnswer = radioButton_ans3.getText().toString();

                else if (radioButton_ans4.isChecked())
                    userAnswer = radioButton_ans4.getText().toString();
                else {
                    Snackbar.make(viewChooses.findViewById(R.id.guideScreen_from_bottom), "Pick an Answer", Snackbar.LENGTH_LONG).show();
                }

                String finalUserAnswer = null;
                if (userAnswer != null) {
                    finalUserAnswer = userAnswer;
                }

                if (finalUserAnswer != null) {
                    // STOPSHIP: 12/17/2022 HERE FIX ?!!!!!
                    if (Objects.equals(finalUserAnswer, theRightAnswer)) {
                        answerCallback.onSuccess(riddleId);
                        mediaPlayerSuccess.start();
                        //Toast.makeText(getActivity(), "Good job", Toast.LENGTH_SHORT).show();
                    } else {
                        answerCallback.onFailed(riddleId);
                        mediaPlayerFailed.start();
                        // Toast.makeText(getActivity(), "sorry it not right", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            textViewQ.setText(riddleQ);
            radioButton_ans1.setText(ans1);
            radioButton_ans2.setText(ans2);
            radioButton_ans3.setText(ans3);
            radioButton_ans4.setText(ans4);

            return viewChooses;
        }

        //inflate RiddleType.CompleteTheSentence
        else if (riddleType == RiddleType.CompleteTheSentence.riddleTypeNum) {
            var viewComplete = inflater.inflate(R.layout.fragment_riddle_complete, container, false);
            // Inflate the layout for this fragment
            EditText ed_Input = viewComplete.findViewById(R.id.ed_frag_input);
            fabSkip = viewComplete.findViewById(R.id.skip_f_act_btn_complete);
            textViewQ = viewComplete.findViewById(R.id.textView_frg_Q_complete);
            btnSubmit = viewComplete.findViewById(R.id.btn_frag_complete_submit);

            fabSkip.setOnClickListener(view -> {
                countDownTimer.cancel();
                fragmentAskForSkipListener.onFloatingBtnSkipClicked();
//2                if (fragmentAskForSkipListener.onFloatingBtnSkipClicked()){
//                    Toast.makeText(AppController.getInstance(), "TimerGotA_isClickedTrueOkaySkipDialogWork", Toast.LENGTH_SHORT).show();
//
//                    floatingActionButtonSkipp.setVisibility(View.GONE);
//                }
            });

            btnSubmit.setOnClickListener(view -> {
                String userAnswer = ed_Input.getText().toString().trim().toLowerCase(Locale.ROOT);
                if (!userAnswer.isEmpty()) {
                    if (userAnswer.equals(theRightAnswer)) {
                        answerCallback.onSuccess(riddleId);
                        mediaPlayerSuccess.start();
                        countDownTimer.cancel();
                    } else {
                        answerCallback.onFailed(riddleId);
                        mediaPlayerFailed.start();
                        countDownTimer.cancel();
                    }
                } else {
                    Snackbar.make(viewComplete.findViewById(R.id.guideScreen_from_bottom), "Enter An Answer", Snackbar.LENGTH_LONG).show();
                }
                //here any way will run
            });

            textViewQ.setText(riddleQ);

            return viewComplete;
        }

        //*********************[End InflatingViews]******************************================
        else return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyMediaPlayer();
        destroyTimerObject();
    }

    private void destroyMediaPlayer() {
        if (mediaPlayerSuccess != null && mediaPlayerFailed != null) {
            mediaPlayerFailed.stop();
            mediaPlayerSuccess.stop();
            mediaPlayerFailed.release();
            mediaPlayerSuccess.release();
            mediaPlayerSuccess = null;
            mediaPlayerFailed = null;
        }
    }

    private void destroyTimerObject() {
        countDownTimer.cancel();
        countDownTimer = null;
    }

}