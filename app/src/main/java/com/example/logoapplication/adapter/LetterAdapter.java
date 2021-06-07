package com.example.logoapplication.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.entities.Section;
import com.example.logoapplication.entities.SubSection;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.LetterViewHolder> implements SectionAdapterInterface{
    private List<Section> sections;
    private final SectionClickListener letterClickListener;

    class LetterViewHolder extends RecyclerView.ViewHolder{
        TextView letter1;
        MaterialCardView materialCardView1;

        public LetterViewHolder(@NonNull View itemView) {
            super(itemView);
            letter1 = itemView.findViewById(R.id.letter_textViewnew);
            materialCardView1 = itemView.findViewById(R.id.letterItemnew);
            materialCardView1.setOnClickListener(v -> {
                if(letterClickListener != null){
                    letterClickListener.onClickSection(getAdapterPosition());
                }
            });

            itemView.setOnClickListener(v -> {
                if(letterClickListener != null){
                    letterClickListener.onClickSection(getAdapterPosition());
                }
            });
        }

        public void bind(Section section){
            String name = section.getName();
            String[] arr = name.split("-");
            letter1.setText(arr[0]);
            //letter2.setText(arr[1]);
        }
    }

    public LetterAdapter (SectionClickListener subSectionClickListener){
        this.letterClickListener = subSectionClickListener;
        sections = new ArrayList<>();
    }

    @Override
    public void setSections(List<Section> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    @Override
    public Section getSection(int position){
        return sections.get(position);
    }

    @NonNull
    @Override
    public LetterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter1, parent, false);
        return new LetterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LetterAdapter.LetterViewHolder holder, int position) {
        holder.bind(sections.get(position));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }
}
