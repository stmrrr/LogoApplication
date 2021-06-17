package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.CompletedTask;
import com.example.logoapplication.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private final ExerciseOnClickListener exerciseOnClickListener;
    private List<CompletedTask> completedTasks = new ArrayList<>();
    public static final int TASK_UNLOCK = 1;
    public static final int TASK_LOCK = 0;


    public interface ExerciseOnClickListener {
        void onClickExercise(int position, boolean flag);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        CardView cardView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.exerciseTitle);
            description = itemView.findViewById(R.id.exerciseDescription);
            cardView = itemView.findViewById(R.id.card_exercise);
        }

        public void bind(Exercise exercise) {
            boolean flag = false;
            if (completedTasks.size() != 0) {
                for(CompletedTask completedTask : completedTasks){
                    if(completedTask.getTaskId().equals(exercise.getId())){
                        cardView.setOnClickListener(v -> {
                            boolean isCompleted = completedTask.getStatus().equals("completed");
                            if (exerciseOnClickListener != null) {
                                exerciseOnClickListener.onClickExercise(getAdapterPosition(), isCompleted);
                            }
                        });
                        String titles = "Упражнение " + exercise.getNumber();
                        title.setText(titles);
                        String descriptionStr;
                        if(exercise.getDescription().length()<100){
                            descriptionStr = exercise.getDescription();
                        } else {
                            descriptionStr = exercise.getDescription().substring(0, 100) + "...";
                        }
                        description.setText(descriptionStr);
                        flag = true;
                        break;
                    }
                }
            }
            if(MyApplication.getInstance().teacher!=null){
                String titles = "Упражнение " + exercise.getNumber();
                title.setText(titles);
                String descriptionStr;
                if(exercise.getDescription().length()<100){
                    descriptionStr = exercise.getDescription();
                } else {
                    descriptionStr = exercise.getDescription().substring(0, 100) + "...";
                }
                description.setText(descriptionStr);
                cardView.setOnClickListener(v -> {
                    if (exerciseOnClickListener != null) {
                        exerciseOnClickListener.onClickExercise(getAdapterPosition(), false);
                    }
                });
            }else if (!flag){
                String titles = "Упражнение " + exercise.getNumber();
                title.setText(titles);
                description.setText("Упражнение заблокировано");
                cardView.setOnClickListener(v -> {
                    if(MyApplication.getInstance().user!=null) {
                        Toast.makeText(itemView.getContext(), "Для выполнения данного упражнения, выполните предыдущие", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(itemView.getContext(), "Для выполнения данного упражнения, войдите или зарегистрируйтесь", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    public ExercisesAdapter(ExerciseOnClickListener exerciseOnClickListener) {
        this.exerciseOnClickListener = exerciseOnClickListener;
        exercises = new ArrayList<>();
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
        notifyDataSetChanged();
    }

    public Exercise getExercise(int position) {
        return exercises.get(position);
    }

    public void setCompletedTasks(List<CompletedTask> completedTasks) {
        this.completedTasks = completedTasks;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == TASK_UNLOCK) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_locked, parent, false);
        }
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.ExerciseViewHolder holder, int position) {
        holder.bind(exercises.get(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (completedTasks.size() != 0) {
            for (CompletedTask completedTask : completedTasks) {
                if (completedTask.getTaskId().equals(exercises.get(position).getId())) {
                    return TASK_UNLOCK;
                }
            }
        }
        if(MyApplication.getInstance().teacher!=null) {
            return TASK_UNLOCK;
        }
        return TASK_LOCK;
    }
}
