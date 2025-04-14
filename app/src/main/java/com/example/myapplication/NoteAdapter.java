package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    Context context;
    AppDatabase db;
    List<note> note_list;

    public NoteAdapter(Context context, List<note> note_list,AppDatabase db) {
        this.context = context;
        this.note_list = note_list;
        this.db=db;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.note_name.setText(note_list.get(position).note_name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, pdf_viewer.class);
                i.putExtra("subject_id",note_list.get(holder.getAdapterPosition()).subject_id);
                i.putExtra("note_id",note_list.get(holder.getAdapterPosition()).id);
                context.startActivity(i);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete Notes")
                        .setMessage("Are u sure u want to delete this?")
                        .setNegativeButton("cance",null).
                        setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.noteDao().delete(note_list.get(holder.getAdapterPosition()));
                                note_list.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        }).show();


                return true;
            }
        });
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