package dev.training.the_riddle.ui.fragments.dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import dev.training.the_riddle.R;

public class FinishedRiddlesDialog extends DialogFragment {

    public FinishedRiddlesDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.setCancelable(false);
        new Handler().postDelayed(this::dismiss,2950);
        return inflater.inflate(R.layout.dialog_finished_riddles, container, false);
    }

}
