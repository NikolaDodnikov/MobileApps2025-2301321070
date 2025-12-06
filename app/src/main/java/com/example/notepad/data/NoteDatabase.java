package com.example.notepad.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {


    public abstract NoteDao noteDao();


    private static volatile NoteDatabase INSTANCE;


    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static NoteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    NoteDatabase.class, "note_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}