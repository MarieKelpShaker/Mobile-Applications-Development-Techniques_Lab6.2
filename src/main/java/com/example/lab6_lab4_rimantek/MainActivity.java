package com.example.lab6_lab4_rimantek;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView notesListView;
    private ArrayList<String> notesList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar (so the menu appears)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        notesListView = findViewById(R.id.notesListView);
        notesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notesList);
        notesListView.setAdapter(adapter);

        // Show message when there are no notes
        TextView emptyView = findViewById(R.id.emptyView);
        notesListView.setEmptyView(emptyView);

        // Optional: tap note to view it
        notesListView.setOnItemClickListener((parent, view, position, id) -> {
            String title = notesList.get(position);
            Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
            intent.putExtra("title", title);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadNotes();
        setTitle(getString(R.string.app_name) + " • " + StorageProvider.currentName(this));
    }

    private void reloadNotes() {
        NoteStorage storage = StorageProvider.get(this);
        List<String> titles = storage.listNoteTitles(this);
        notesList.clear();
        notesList.addAll(titles);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            return true;
        } else if (id == R.id.menu_delete) {
            startActivity(new Intent(MainActivity.this, DeleteNoteActivity.class));
            return true;
        } else if (id == R.id.menu_switch_storage) {
            StorageProvider.toggle(this);
            String now = StorageProvider.currentName(this);
            Toast.makeText(this, getString(R.string.switched_to, now), Toast.LENGTH_SHORT).show();
            reloadNotes();
            setTitle(getString(R.string.app_name) + " • " + now);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
