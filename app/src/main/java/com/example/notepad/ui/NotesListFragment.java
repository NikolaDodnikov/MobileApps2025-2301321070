package com.example.notepad.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.adapter.NoteAdapter;
import com.example.notepad.viewmodel.NoteViewModel;


public class NotesListFragment extends Fragment implements SearchView.OnQueryTextListener {

    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        navController = Navigation.findNavController(view);


        adapter = new NoteAdapter(note -> {
            Bundle bundle = new Bundle();
            bundle.putInt("noteId", note.getId());
            navController.navigate(R.id.action_notesListFragment_to_noteDetailFragment, bundle);
        });
        recyclerView.setAdapter(adapter);


        loadAllNotes();


        view.findViewById(R.id.fabAddNote).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("noteId", -1);
            navController.navigate(R.id.action_notesListFragment_to_noteDetailFragment, bundle);
        });
    }


    private void loadAllNotes() {
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), adapter::submitList);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();

        if (searchView != null) {

            searchView.setOnQueryTextListener(this);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.isEmpty()) {
            loadAllNotes();
        } else {

            noteViewModel.searchNotes(newText).observe(getViewLifecycleOwner(), adapter::submitList);
        }
        return true;
    }
}