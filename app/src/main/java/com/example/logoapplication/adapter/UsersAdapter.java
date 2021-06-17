package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.R;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private final OnDeleteUserListener onDeleteUserListener;
    private final OnClickUser onClickUser;
    private List<User> users;
    private List<Teacher> teachers;

    public interface OnDeleteUserListener{
        void OnDelete(int position);
    }

    public interface OnClickUser{
        void viewProfile(int position);
    }

    public UsersAdapter(OnDeleteUserListener onDeleteUserListener, OnClickUser onClickUser) {
        users = new ArrayList<>();
        teachers = new ArrayList<>();
        this.onDeleteUserListener = onDeleteUserListener;
        this.onClickUser = onClickUser;
    }

    public void setUsers(List<User> users) {
        this.teachers = new ArrayList<>();
        this.users = users;
        notifyDataSetChanged();
    }

    public void setTeachers(List<Teacher> teachers) {
        this.users = new ArrayList<>();
        this.teachers = teachers;
        notifyDataSetChanged();
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    class UserViewHolder extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView userView;
        View deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profile_image);
            userView = itemView.findViewById(R.id.userID);
            deleteButton = itemView.findViewById(R.id.deleteUser);

            imageView.setOnClickListener(view -> {
                if(onClickUser != null){
                    onClickUser.viewProfile(getAdapterPosition());
                }
            });

            deleteButton.setOnClickListener(view -> {
                if(onDeleteUserListener!=null){
                    onDeleteUserListener.OnDelete(getAdapterPosition());
                }
            });
        }

        public void bindUser(User user){
            userView.setText(user.getName());
        }

        public void bindTeacher(Teacher teacher){
            userView.setText(teacher.getName());
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UserViewHolder holder, int position) {
        if(users.size()==0){
            holder.bindTeacher(teachers.get(position));
        } else {
            holder.bindUser(users.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(users.size()==0) {
            return teachers.size();
        } else {
            return users.size();
        }
    }
}
