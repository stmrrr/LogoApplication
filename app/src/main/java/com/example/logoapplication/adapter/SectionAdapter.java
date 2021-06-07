package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.entities.Section;
import com.example.logoapplication.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionsViewHolder> implements SectionAdapterInterface{
    private List<Section> sections;
    private final SectionClickListener sectionClickListener;

    class SectionsViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView description;
        MaterialCardView materialCardView;

        public SectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sectionTitle);
            description = itemView.findViewById(R.id.sectionDescription);
            materialCardView = itemView.findViewById(R.id.cardSection);

            materialCardView.setOnClickListener(v -> {
                if(sectionClickListener != null){
                    sectionClickListener.onClickSection(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(v -> {
                if(sectionClickListener != null){
                    sectionClickListener.onClickSection(getAdapterPosition());
                }
            });
        }

        public void bind(Section section){
            title.setText(section.getName());
            description.setText(section.getDescription());
        }
    }

    public SectionAdapter(SectionClickListener sectionClickListener) {
        this.sectionClickListener = sectionClickListener;
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
    public SectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.SectionsViewHolder holder, int position) {
        holder.bind(sections.get(position));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }
}
