package com.example.myapplication;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteImageDao {
    @Insert
    void insert(NoteImage noteImage);
    @Query("SELECT * FROM NoteImage WHERE subjectID=:subjectid AND noteID=:noteid")
    List<NoteImage> getImagesForNote(int subjectid,int noteid);


}
