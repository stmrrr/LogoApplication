package com.example.logoapplication.entities;

import org.bson.types.ObjectId;

public class Message {
    private ObjectId id;
    private ObjectId chat_id;
    private String text;
    private ObjectId from;
    private ObjectId to;
    private String attachment;

    public Message(ObjectId id, ObjectId chat_id, String text, ObjectId from, ObjectId to, String attachment) {
        this.id = id;
        this.chat_id = chat_id;
        this.text = text;
        this.from = from;
        this.to = to;
        this.attachment = attachment;
    }

    public Message() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getChat_id() {
        return chat_id;
    }

    public void setChat_id(ObjectId chat_id) {
        this.chat_id = chat_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ObjectId getFrom() {
        return from;
    }

    public void setFrom(ObjectId from) {
        this.from = from;
    }

    public ObjectId getTo() {
        return to;
    }

    public void setTo(ObjectId to) {
        this.to = to;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", chat_id=" + chat_id +
                ", text='" + text + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
