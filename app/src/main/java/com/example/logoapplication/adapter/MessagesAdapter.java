package com.example.logoapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.logoapplication.MyApplication;
import com.example.logoapplication.R;
import com.example.logoapplication.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>{
    private List<Message> messages;
    public static final int MSG_RIGHT = 1;
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT_DOWNLOAD = 2;
    public static final int MSG_LEFT_DOWNLOAD = 3;
    private OnCLickMessage onCLickMessage;

    public interface OnCLickMessage{
        void downloadFile(int position);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
        }

        public void bind(Message message){
            this.message.setText(message.getText());
            if(message.getAttachment()!=null&&!message.getAttachment().isEmpty()){
                itemView.setOnClickListener(view -> {
                    onCLickMessage.downloadFile(getAdapterPosition());
                });
            }
        }
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
        } else if(viewType == MSG_LEFT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
        } else if(viewType == MSG_RIGHT_DOWNLOAD){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_right, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_left, parent, false);
        }
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessageViewHolder holder, int position) {
        holder.bind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public MessagesAdapter(OnCLickMessage message) {
        this.onCLickMessage = message;
        messages = new ArrayList<>();
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public Message getMessage(int position){
        return messages.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(MyApplication.getInstance().user!=null){
            if(messages.get(position).getFrom().equals(MyApplication.getInstance().user.getId())){
                if(messages.get(position).getAttachment()!=null&&!messages.get(position).getAttachment().isEmpty()){
                    return MSG_RIGHT;
                }else {
                    return MSG_RIGHT_DOWNLOAD;
                }
            }else{
                if(messages.get(position).getAttachment()!=null&&!messages.get(position).getAttachment().isEmpty()){
                    return MSG_LEFT_DOWNLOAD;
                }else {
                    return MSG_LEFT;
                }
            }
        }else{
            if(messages.get(position).getFrom().equals(MyApplication.getInstance().teacher.getId())){
                if(messages.get(position).getAttachment()!=null&&!messages.get(position).getAttachment().isEmpty()){
                    return MSG_RIGHT;
                }else {
                    return MSG_RIGHT_DOWNLOAD;
                }
            }else{
                if(messages.get(position).getAttachment()!=null&&!messages.get(position).getAttachment().isEmpty()){
                    return MSG_LEFT_DOWNLOAD;
                }else {
                    return MSG_LEFT;
                }
            }
        }
    }
}
