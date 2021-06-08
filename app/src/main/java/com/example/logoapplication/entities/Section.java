package com.example.logoapplication.entities;

import org.bson.types.ObjectId;


public class Section {
    private ObjectId id;
    private String name;
    private String description;
    private ObjectId id_main_section;
    private String mark;
    private boolean isEnd;

    public Section(ObjectId id, String name, String description, ObjectId id_main_section, String mark, boolean isEnd) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.id_main_section = id_main_section;
        this.mark = mark;
        this.isEnd = isEnd;
    }

    public Section() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getId_main_section() {
        return id_main_section;
    }

    public void setId_main_section(ObjectId id_main_section) {
        this.id_main_section = id_main_section;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public boolean getEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "Section{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id_subsection=" + id_main_section +
                ", mark='" + mark + '\'' +
                '}';
    }
}
