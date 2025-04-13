package com.example.myapplication.ui.home;

import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AppDatabase;
import com.example.myapplication.Note_Activity;
import com.example.myapplication.R;
import com.example.myapplication.Subject;

import java.util.ArrayList;
import java.util.List;

public class Subject_adapter extends RecyclerView.Adapter<Subject_view_holder> {
    Context context;

    AppDatabase db;
    List<Subject> subject_list;

    public Subject_adapter(Context context, List<Subject> subject_list,AppDatabase db) {
        this.context = context;
        this.subject_list = subject_list;
        this.db=db;
    }

    @NonNull
    @Override
    public Subject_view_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Subject_view_holder(LayoutInflater.from(context).inflate(R.layout.subject_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Subject_view_holder holder, int position) {


        holder.subject_name.setText(subject_list.get(position).getSubject_name());
        //on single click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Note_Activity.class);
                i.putExtra("subject_id",subject_list.get(holder.getAdapterPosition()).getSubject_id());
                context.startActivity(i);
            }
        });
        //on long press
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            new AlertDialog.Builder(context).setTitle("Delete Subject")
                    .setMessage("Are u sure u want to delete this?")
                    .setNegativeButton("cance",null).
                    setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.subjectDao().delete(subject_list.get(holder.getAdapterPosition()));
                            subject_list.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    }).show();


                return true;
            }
        });

    }
    public void updateList(List<Subject> newList) {
        this.subject_list = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return subject_list.size();
    }
}
