package com.example.myapplication;

import static java.security.AccessController.getContext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Note_Activity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<note> notesForSubject;
    FloatingActionButton floatingActionButton;
    AppDatabase db;
    NoteAdapter noteAdapter; // Make adapter a class member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        int subjectId = getIntent().getIntExtra("subject_id", -1);

        db = Room.databaseBuilder(this, AppDatabase.class, "notes-latest")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigrationFrom(1)
                .build();

        notesForSubject = db.noteDao().getNotesForSubject(subjectId);

        floatingActionButton = findViewById(R.id.floatingButton_2);
        recyclerView = findViewById(R.id.notes_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter as class member
        noteAdapter = new NoteAdapter(this, notesForSubject);
        recyclerView.setAdapter(noteAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog dialog = new BottomSheetDialog(Note_Activity.this);
                View upload_sheet = getLayoutInflater().inflate(R.layout.file_upload_sheet, null);
                dialog.setContentView(upload_sheet);
                dialog.show();

                EditText upload_note_name = upload_sheet.findViewById(R.id.upload_note_name);
                Button upload_note_btn = upload_sheet.findViewById(R.id.upload_note_btn);

                upload_note_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String n_name = upload_note_name.getText().toString();
                        if((n_name != null && !n_name.isEmpty()) && n_name.matches("(?i)[a-z0-9\\s]{2,}")) {
                            // Insert new note
                            db.noteDao().insert(new note(subjectId, n_name));

                            // Update the list directly from database
                            List<note> updatedList = db.noteDao().getNotesForSubject(subjectId);
                            notesForSubject.clear();
                            notesForSubject.addAll(updatedList);

                            // Notify adapter of data change
                            noteAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(Note_Activity.this, "Enter valid name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // Window insets listener at the end
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}