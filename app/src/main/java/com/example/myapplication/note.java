    package com.example.myapplication;
    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.ForeignKey;
    import androidx.room.PrimaryKey;

    @Entity(foreignKeys = @ForeignKey(entity = Subject.class
    ,parentColumns = "id",childColumns = "subject_id",onDelete = ForeignKey.CASCADE))
    public class note {
        @PrimaryKey(autoGenerate = true)
        public int id;
        @ColumnInfo(name = "note_name")
        public String note_name;
        @ColumnInfo(name="subject_id")
        public int subject_id;

        public note(int subject_id,String note_name)
        {
            this.subject_id=subject_id;
            this.note_name=note_name;
        }


    }
