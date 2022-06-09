package com.katherinepalumbo.notetakingapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

//annotate as a database
@Database(entities = {Note.class}, version = 1)     //version is SQLite database version
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;       //static so it can be accessed anywhere in the app
    public abstract NoteDAO noteDAO();          //Room database will create body of method
    public static synchronized NoteDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext()
            ,NoteDatabase.class, "note_database").fallbackToDestructiveMigration().addCallback(roomCallback).build();
        }
        return instance;
    }


    //override the on create method so that the operations we determine will get done
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new PopulateDbAsyncTask(instance).execute();
        }
    };

    //create async task
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private NoteDAO noteDao;

        private PopulateDbAsyncTask(NoteDatabase database)
        {
            noteDao = database.noteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //Adding some default data to the database
            noteDao.insert(new Note("Tiltle 1", "Description 1"));
            noteDao.insert(new Note("Tiltle 2", "Description 2"));
            noteDao.insert(new Note("Tiltle 3", "Description 3"));
            noteDao.insert(new Note("Tiltle 4", "Description 4"));
            noteDao.insert(new Note("Tiltle 5", "Description 5"));


            return null;
        }
    }
}
