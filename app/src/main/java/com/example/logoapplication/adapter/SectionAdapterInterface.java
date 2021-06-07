package com.example.logoapplication.adapter;

import com.example.logoapplication.entities.Section;

import java.util.List;

public interface SectionAdapterInterface {
    Section getSection(int position);
    void setSections(List<Section> sections);
}
