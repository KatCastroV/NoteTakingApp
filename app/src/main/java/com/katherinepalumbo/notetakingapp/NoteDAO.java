package com.katherinepalumbo.notetakingapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);

    //list all notes in note table and list the notes from small to large when compared to the ID
    //ID will be in sequence (one, two, three, etc.)
    @Query("SELECT * FROM note_table ORDER BY id ASC")
    //Adding the live data architectural component
    //it will observe the live data database and reflect it to recycler view if there is a change
    LiveData<List<Note>> getAllNotes();
}
