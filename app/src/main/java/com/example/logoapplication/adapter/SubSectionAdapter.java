package com.example.logoapplication.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.entities.SubSection;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SubSectionAdapter extends RecyclerView.Adapter<SubSectionAdapter.SubSectionViewHolder> {
    private List<SubSection> subSections;
    private SubSectionOnClickListener subSectionClickListener;

    public interface  SubSectionOnClickListener{
        void onClickSection(int position);
    }

    class SubSectionViewHolder extends RecyclerView.ViewHolder{
        TextView letter1;
        //TextView letter2;
        MaterialCardView materialCardView1;
        //MaterialCardView materialCardView2;

        public SubSectionViewHolder(@NonNull View itemView) {
            super(itemView);
            letter1 = itemView.findViewById(R.id.letter_textViewnew);
            //letter2 = itemView.findViewById(R.id.letter1_textView);
            materialCardView1 = itemView.findViewById(R.id.letterItemnew);
            //materialCardView2 = itemView.findViewById(R.id.letterItem2);
            materialCardView1.setOnClickListener(v -> {
                if(subSectionClickListener != null){
                    subSectionClickListener.onClickSection(getAdapterPosition());
                }
            });

            /*materialCardView2.setOnClickListener(v -> {
                if(subSectionClickListener != null){
                    subSectionClickListener.onClickSection(getAdapterPosition());
                }
            });*/

            itemView.setOnClickListener(v -> {
                if(subSectionClickListener != null){
                    subSectionClickListener.onClickSection(getAdapterPosition());
                }
            });
        }

        public void bind(SubSection section){
            String name = section.getName();
            String[] arr = name.split("-");
            letter1.setText(arr[0]);
            //letter2.setText(arr[1]);
        }
    }

    public SubSectionAdapter(SubSectionOnClickListener subSectionClickListener){
        this.subSectionClickListener = subSectionClickListener;
        subSections = new ArrayList<>();
    }

    public void setSubSections(List<SubSection> subSections) {
        this.subSections = subSections;
        notifyDataSetChanged();
    }

    public SubSection getSubSection(int position){
        return subSections.get(position);
    }

    @NonNull
    @Override
    public SubSectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter1, parent, false);
        return new SubSectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubSectionAdapter.SubSectionViewHolder holder, int position) {
        holder.bind(subSections.get(position));
    }

    @Override
    public int getItemCount() {
        return subSections.size();
    }
}
