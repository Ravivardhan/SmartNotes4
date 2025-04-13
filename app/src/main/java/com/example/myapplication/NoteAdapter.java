package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    Context context;
    List<note> note_list;

    public NoteAdapter(Context context, List<note> note_list) {
        this.context = context;
        this.note_list = note_list;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.note_name.setText(note_list.get(position).note_name);
    }

    @Override
    public int getItemCount() {
        return note_list.size();
    }

    public void updateList(List<note> newList) {
        if (newList != null && !newList.isEmpty()) {
            this.note_list = newList;
            notifyDataSetChanged(); // This ensures the adapter reflects the new data.
        } else {
            // Log message if the list is null or empty, for debugging
            System.out.println("New list is empty or null, nothing to update.");
        }
    }
}