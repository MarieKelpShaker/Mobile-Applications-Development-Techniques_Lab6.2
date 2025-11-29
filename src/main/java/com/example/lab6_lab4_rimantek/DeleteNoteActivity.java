package com.example.lab6_lab4_rimantek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {
    private ListView deleteListView;
    private Button deleteButton;

    private final ArrayList<String> noteTitles = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private int selectedPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_note_activity);

        deleteListView = findViewById(R.id.deleteListView);
        deleteButton = findViewById(R.id.deleteButton);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                noteTitles);
        deleteListView.setAdapter(adapter);
        deleteListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        deleteListView.setOnItemClickListener((parent, view, position, id) -> selectedPosition = position);
        deleteButton.setOnClickListener(v -> deleteSelectedNote());

        setTitle(getString(R.string.delete_note_title) + " • " + StorageProvider.currentName(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }

    private void loadNotes() {
        noteTitles.clear();
        noteTitles.addAll(StorageProvider.get(this).listNoteTitles(this));
        adapter.notifyDataSetChanged();

        selectedPosition = ListView.INVALID_POSITION;

        if (noteTitles.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_notes_found), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteSelectedNote() {
        if (selectedPosition == ListView.INVALID_POSITION) {
            Toast.makeText(this, getString(R.string.select_note_warning), Toast.LENGTH_SHORT).show();
            return;
        }

        String title = noteTitles.get(selectedPosition);
        boolean deleted = StorageProvider.get(this).deleteNote(this, title);

        if (deleted) {
            Toast.makeText(this, getString(R.string.note_deleted_message), Toast.LENGTH_SHORT).show();
            loadNotes();
        } else {
            Toast.makeText(this, getString(R.string.delete_error_message), Toast.LENGTH_SHORT).show();
        }
    }
}
