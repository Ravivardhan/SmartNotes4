package com.example.myapplication;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Subject.class,note.class},version =2)
public abstract class AppDatabase extends RoomDatabase{
    public abstract SubjectDao subjectDao();
    public abstract noteDao noteDao();
}
