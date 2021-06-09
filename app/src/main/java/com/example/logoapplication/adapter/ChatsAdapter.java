package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.crud.TeacherCRUD;
import com.example.logoapplication.crud.UserCRUD;
import com.example.logoapplication.entities.Chat;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.Teacher;
import com.example.logoapplication.entities.User;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    private List<Chat> chats;

    private final OnChatClickListener onChatClickListener;

    public interface OnChatClickListener{
        void onClick(int position);
    }

    class ChatViewHolder extends RecyclerView.ViewHolder{
        TextView userId;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.userID);

            itemView.setOnClickListener(v -> {
                if(onChatClickListener != null){
                    onChatClickListener.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(Chat chat){
            if(MyApplication.getInstance().user!=null){
                ObjectId teacherId = chat.getId_teacher();
                TeacherCRUD teacherCRUD = new TeacherCRUD((TeacherCRUD.TeacherLoginListener) teacher -> userId.setText(teacher.getName()));
                teacherCRUD.loginTeacher(new Document("_id", teacherId));
            } else {
                ObjectId user = chat.getId_user();
                UserCRUD userCRUD = new UserCRUD((UserCRUD.UserLoginListener) user1 -> userId.setText(user1.getName()));
                userCRUD.loginUser(new Document("_id", user));
            }
        }
    }

    public ChatsAdapter(OnChatClickListener onChatClickListener) {
        this.onChatClickListener = onChatClickListener;
        chats = new ArrayList<>();
    }

    public void setChats(List<Chat> chats){
        this.chats = chats;
        notifyDataSetChanged();
    }

    public Chat getChat(int position){
        return chats.get(position);
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsAdapter.ChatViewHolder holder, int position) {
        holder.bind(chats.get(position));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
