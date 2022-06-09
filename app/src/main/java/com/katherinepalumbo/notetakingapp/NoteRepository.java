package com.katherinepalumbo.notetakingapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository
{

    private NoteDAO noteDao;
    private LiveData<List<Note>> notes;

    public NoteRepository(Application application)
    {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.noteDAO();
        notes = noteDao.getAllNotes();
    }

    public void insert(Note note)
    {
        new InsertNoteAsyncTask(noteDao).execute(note); //background task has been prepared
    }

    public void update(Note note)
    {
        new UpdateNoteAsyncTask(noteDao).execute(note); //background task has been prepared
    }

    public void delete(Note note)
    {
        new DeleteNoteAsyncTask(noteDao).execute(note); //background task has been prepared
    }

    //this method will return the note array we created above
    //Room database automatically performs the necessary operations for live data in the background
    //So, we do not need to deal with this part
    //For other database operations (delete, update, insert), we have to create background trades by ourselves
    //Now, the room database does not allow us to do database operations with the main thread
    //if we try to do it with the main thread, the app will crash and close\
    //"Cannot access database on main thread since it may potentially lock the UI for a long period of time"
    //To avoid this, .allowMainThreadQueries() in the NoteDatabase.jav class (see notes)
    public LiveData<List<Note>> getAllNotes()
    {
        return notes;
    }

    //create an asynchronous task for insert operation
    //3 parameters:
    //  1. we need to add a particular method when we use the async task class: doInBackground method
    //  2. parameter for onProgressUpdate method (used to track the transaction);
    //      but we won't use it here, hence, void.
    //  3. parameter return type of doInBackground
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDAO noteDao;

        private InsertNoteAsyncTask(NoteDAO noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);   //assigned element in the 0th index of Notes array
            return null;
        }

    }

    //Create an asynchronous task for update operation
    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDAO noteDao;

        private UpdateNoteAsyncTask(NoteDAO noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);   //assigned element in the 0th index of Notes array
            return null;
        }

    }

    //Create an asynchronous task for delete operation
    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>
    {
        private NoteDAO noteDao;

        private DeleteNoteAsyncTask(NoteDAO noteDao)
        {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);   //assigned element in the 0th index of Notes array
            return null;
        }

    }

}
