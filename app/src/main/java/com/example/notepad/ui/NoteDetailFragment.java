package com.example.notepad.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.notepad.R;
import com.example.notepad.data.Note;
import com.example.notepad.viewmodel.NoteViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class NoteDetailFragment extends Fragment {

    private TextInputEditText editTextTitle;
    private TextInputEditText editTextContent;
    private NoteViewModel noteViewModel;
    private int currentNoteId = -1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.editTextTitle);
        editTextContent = view.findViewById(R.id.editTextContent);


        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        if (getArguments() != null) {
            currentNoteId = getArguments().getInt("noteId", -1);
        }


        if (currentNoteId != -1) {
            noteViewModel.getNoteById(currentNoteId).observe(getViewLifecycleOwner(), note -> {
                if (note != null) {
                    editTextTitle.setText(note.getTitle());
                    editTextContent.setText(note.getContent());
                }
            });
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);


        MenuItem deleteItem = menu.findItem(R.id.action_delete);
        if (deleteItem != null) {

            deleteItem.setVisible(currentNoteId != -1);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(requireView());

        int itemId = item.getItemId();

        if (itemId == R.id.action_save) {
            saveNote();
            return true;
        } else if (itemId == R.id.action_delete) {
            deleteNote();
            navController.popBackStack(); // Връщане към списъка след изтриване
            return true;
        } else if (itemId == android.R.id.home) {
            // Обработка на стрелката 'Назад' в ActionBar
            navController.popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // --- CRUD Методи ---

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), "Не може да запазите празна бележка.", Toast.LENGTH_SHORT).show();
            return;
        }


        Note note = new Note(title, content);

        if (currentNoteId == -1) {

            noteViewModel.insert(note);
            Toast.makeText(getContext(), "Бележката е създадена!", Toast.LENGTH_SHORT).show();
        } else {

            note.setId(currentNoteId); // Трябва да запазим старото ID за обновяване
            noteViewModel.update(note);
            Toast.makeText(getContext(), "Бележката е обновена!", Toast.LENGTH_SHORT).show();
        }


        Navigation.findNavController(requireView()).popBackStack();
    }

    private void deleteNote() {
        if (currentNoteId != -1) {
            Note note = new Note("", ""); // Създаваме обект само с ID за изтриване
            note.setId(currentNoteId);
            noteViewModel.delete(note);
            Toast.makeText(getContext(), "Бележката е изтрита.", Toast.LENGTH_SHORT).show();
        }
    }
}