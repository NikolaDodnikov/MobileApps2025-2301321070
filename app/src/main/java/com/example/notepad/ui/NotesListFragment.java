package com.example.notepad.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.viewmodel.NoteViewModel;

public class NotesListFragment extends Fragment {

    private NoteViewModel noteViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        NavController navController = Navigation.findNavController(view);


        NoteAdapter adapter = new NoteAdapter(note -> {

            Bundle bundle = new Bundle();
            bundle.putInt("noteId", note.getId());

            navController.navigate(R.id.action_notesListFragment_to_noteDetailFragment, bundle);
        });
        recyclerView.setAdapter(adapter);


        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), adapter::submitList);


        view.findViewById(R.id.fabAddNote).setOnClickListener(v -> {

            Bundle bundle = new Bundle();
            bundle.putInt("noteId", -1);
            navController.navigate(R.id.action_notesListFragment_to_noteDetailFragment, bundle);
        });
    }
}