package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.entities.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExerciseViewHolder> {
    private List<Exercise> exercises;
    private final ExerciseOnClickListener exerciseOnClickListener;


    public interface  ExerciseOnClickListener{
        void onClickExercise(int position);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;

        public ExerciseViewHolder(@NonNull View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.exerciseTitle);
            description = itemView.findViewById(R.id.exerciseDescription);

            itemView.setOnClickListener(v -> {
                if(exerciseOnClickListener != null){
                    exerciseOnClickListener.onClickExercise(getAdapterPosition());
                }
            });
        }

        public void bind(Exercise exercise){
            String titles = "Упражнение " + exercise.getNumber();
            title.setText(titles);
            String descriptionStr = exercise.getDescription().substring(0, 100)+"...";
            description.setText(descriptionStr);
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

    public Exercise getExercise(int position){
        return exercises.get(position);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
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
}
