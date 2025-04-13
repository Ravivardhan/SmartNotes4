package com.example.myapplication.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class Subject_view_holder extends RecyclerView.ViewHolder {
    TextView subject_name;
    public Subject_view_holder(@NonNull View itemView) {
        super(itemView);
        subject_name=itemView.findViewById(R.id.subject_name);
    }
}
