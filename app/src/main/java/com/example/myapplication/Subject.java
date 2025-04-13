package com.example.myapplication;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Subject {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    public Subject(String name)
    {
        this.name=name;
    }
    public String getSubject_name()
    {
        return name;
    }
    public int getSubject_id()
    {
        return id;
    }
}
