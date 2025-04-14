package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@Database(entities = {Subject.class,note.class,NoteImage.class},version =3)
public abstract class AppDatabase extends RoomDatabase{
    public abstract SubjectDao subjectDao();
    public abstract noteDao noteDao();
    public abstract NoteImageDao noteImageDao();


}
