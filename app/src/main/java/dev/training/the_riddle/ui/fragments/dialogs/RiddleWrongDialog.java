package dev.training.the_riddle.ui.fragments.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import dev.training.the_riddle.R;
import dev.training.the_riddle.app_system.interfaces.DialogListener;

public class RiddleWrongDialog extends DialogFragment {
    private DialogListener okay_btn_listener;

    private TextView tv_hint_for_solved_wrong;

    private Button btn_dialog_okay;

    private ImageView ic_riddle_solved_wrong;

    private static final String ARG_RIDDLE_HINT = "riddle_hint";

    private String riddle_hint;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            // Instead of the context being cast to DialogTimeOutCallback we use the getParentFragment() due the place of the view that use the listener are in a subFragment to the parent fragment.
            okay_btn_listener = (DialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement DialogListener ");
        }
    }

    public RiddleWrongDialog() {
    }


    //just for the hint answer
    public static RiddleWrongDialog newInstance(String riddle_hint) {
        Log.i("RiddleWrongDialog", "newInstance: Works!");
        RiddleWrongDialog riddleWrongDialog = new RiddleWrongDialog();
        Bundle args = new Bundle();
        args.putString(ARG_RIDDLE_HINT, riddle_hint);
        riddleWrongDialog.setArguments(args);
        return riddleWrongDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //For Preventing The User From Dismiss The Dialog
        this.setCancelable(false);
        if (getArguments() != null) {
            //test
            Bundle bundle = getArguments();
            riddle_hint = bundle.getString(ARG_RIDDLE_HINT);
        } else {
            Log.i("RiddleDialog", "onCreate: theArgumentsAreEmpty");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // for inflating and finds ids
        View view = findViews(inflater, container);
        //setting the hint into the dialog % the hint came from the newInstance from this Class
        tv_hint_for_solved_wrong.setText(riddle_hint);

        setViewsAnimation();

        //when btn inside dialog is clicked == will run my custom listener "okay_btn_listener" AND dismiss();
        btn_dialog_okay.setOnClickListener(view1 -> {
            if (okay_btn_listener != null) {
                okay_btn_listener.onClickForWrongAnswer();
                dismiss();
            }
        });
        return view;
    }
    // Inflate the layout for this fragment
    private View findViews(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.dialog_wrong_answer, container, false);
        tv_hint_for_solved_wrong = view.findViewById(R.id.hint_for_riddle_solved_wrong);
        btn_dialog_okay = view.findViewById(R.id.btn_wrong_answer_okay);

        ic_riddle_solved_wrong = view.findViewById(R.id.ic_riddle_solved_wrong);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearViewsAnimation();
    }

    private void setViewsAnimation(){
        Animation animation = AnimationUtils.loadAnimation(requireContext(),R.anim.bounce);
        ic_riddle_solved_wrong.startAnimation(animation);
    }
    private void clearViewsAnimation(){
        ic_riddle_solved_wrong.clearAnimation();
    }
}
