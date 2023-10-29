package dev.training.the_riddle.adapters.levels;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import dev.training.the_riddle.R;
import dev.training.the_riddle.data.local.entities.Level;

public class LevelAdapter extends RecyclerView.Adapter<LevelViewHolder> {
    private LevelCallback callback;

    public void registerLevelCallback(LevelCallback callback) {
        this.callback = callback;
    }

    private final DiffUtil.ItemCallback<Level> diffUtils = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull Level oldItem, @NonNull Level newItem) {
            return oldItem.getLevelNum() == newItem.getLevelNum();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Level oldItem, @NonNull Level newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    private final AsyncListDiffer<Level> differ = new AsyncListDiffer<>(this, diffUtils);

    public void levels(List<Level> levels) {
        differ.submitList(levels);
    }

    public List<Level> levels() {
        return differ.getCurrentList();
    }

    @NonNull
    @Override
    public LevelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_level, parent, false);
        return new LevelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelViewHolder holder, int position) {
        Level level = levels().get(position);
        holder.setBindData(level);

        //for clicking on any item
        holder.itemView.setOnClickListener(view -> {
            var levelOpenStatus = level.getLevelOpenStatus() != null ? level.getLevelOpenStatus() : false;
            if (levelOpenStatus) {
                view.setBackgroundResource(R.drawable.shape_level_item_clicked);
            }
            callback.onClick(level.getLevelNum(), levelOpenStatus);
        });
    }

    @Override
    public int getItemCount() {
        return levels().size();
    }

    public interface LevelCallback {
        void onClick(int levelNum, boolean levelOpenStatus);
    }
}
