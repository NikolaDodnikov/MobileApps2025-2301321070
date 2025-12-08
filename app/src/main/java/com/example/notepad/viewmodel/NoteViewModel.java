package com.example.notepad.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.notepad.data.Note;
import com.example.notepad.data.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private final NoteRepository repository;
    private final LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }


    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }


    public void delete(Note note) {
        repository.delete(note);
    }


    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }



    public LiveData<Note> getNoteById(int id) {
        return repository.getNoteById(id);
    }
}