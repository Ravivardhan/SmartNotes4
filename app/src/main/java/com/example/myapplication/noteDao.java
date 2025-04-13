package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface noteDao {
    @Insert
    public void insert(note note);
    @Delete
    public void delete(note note);
@Query("select * from note where subject_id=:subject_id")
    List<note> getNotesForSubject(int subject_id);
}
