package com.example.myapplication;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface SubjectDao {
    @Insert
    void insert(Subject subject);
    @Delete
    void delete(Subject subject);
    @Query("SELECT * FROM Subject")
    List<Subject> getAllSubjects();


}
