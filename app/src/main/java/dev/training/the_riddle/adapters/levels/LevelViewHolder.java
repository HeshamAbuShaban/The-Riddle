package dev.training.the_riddle.adapters.levels;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.MessageFormat;

import dev.training.the_riddle.R;
import dev.training.the_riddle.data.local.entities.Level;
import dev.training.the_riddle.databinding.ItemLevelBinding;

public class LevelViewHolder extends RecyclerView.ViewHolder {
    private final ItemLevelBinding binding;
    private ImageView imageView_level_state;
    private TextView textView_level_item, textView_level_minPoint, level_evaluation_num;
    private RatingBar ratingBarLevel_evaluation;

    public LevelViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = ItemLevelBinding.bind(itemView);
        findViews();
    }

    private void findViews() {
        textView_level_item = binding.textViewLevelItem;
        textView_level_minPoint = binding.textViewLevelMinPoint;
        imageView_level_state = binding.imageViewLevelState;
        level_evaluation_num = binding.levelEvaluationNum;
        ratingBarLevel_evaluation = binding.ratingBarLevelEvaluation;
    }

    protected void setBindData(Level level) {
        // set the num of the level
        textView_level_item.setText(String.valueOf(level.getLevelNum()));
        // set the points that req for each level
        textView_level_minPoint.setText(String.valueOf(level.getMinPointToUnlock()));
        // set the ratingBarLevel_evaluation each level
        float realLevel_evaluation = level.getLevelEvaluation() != null ? level.getLevelEvaluation() : 0.0f;
        level_evaluation_num.setText(MessageFormat.format("%{0}", realLevel_evaluation));
        checkLevelEvaluation(realLevel_evaluation);
        //if min point for this class = the user score
        imageView_level_state.setImageResource((level.getLevelOpenStatus() != null ? level.getLevelOpenStatus() : false) ? R.drawable.ic_lock_open : R.drawable.ic_locked);
    }

    private void checkLevelEvaluation(float realLevel_evaluation) {
        float value_forRatingbar = (realLevel_evaluation * 3) / 100;
        ratingBarLevel_evaluation.setRating(value_forRatingbar);
    }

}
