package com.example.myapplication;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(entity = Subject.class,parentColumns = "id",childColumns = "subjectID",onDelete = ForeignKey.CASCADE),
@ForeignKey(entity = note.class,parentColumns = "id",childColumns = "noteID",onDelete = ForeignKey.CASCADE)})
public class NoteImage {
    @PrimaryKey(autoGenerate = true)
    public int imageid;

    public int subjectID;
    public int noteID;
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] imageData;

    public NoteImage(int subjectID, int noteID, byte[] imageData)
    {
        this.subjectID=subjectID;
        this.noteID=noteID;
        this.imageData=imageData;
    }
}
