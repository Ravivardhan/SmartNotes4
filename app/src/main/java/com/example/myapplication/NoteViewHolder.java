package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder{
    TextView note_name;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        note_name=itemView.findViewById(R.id.notes_name);
    }
}
