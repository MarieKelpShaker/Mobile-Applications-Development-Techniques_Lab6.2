package com.example.lab6_lab4_rimantek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {
    private EditText noteTitleEditText;
    private EditText noteContentEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make sure your layout file is named activity_add_note.xml
        setContentView(R.layout.add_note_activity);

        noteTitleEditText = findViewById(R.id.noteTitleEditText);
        noteContentEditText = findViewById(R.id.noteContentEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> saveNote());
        setTitle(getString(R.string.add_note_title, StorageProvider.currentName(this)));
    }

    private void saveNote() {
        String title = noteTitleEditText.getText().toString().trim();
        String content = noteContentEditText.getText().toString().trim();

        // Per-field validation + Toasts
        if (title.isEmpty()) {
            noteTitleEditText.setError(getString(R.string.empty_title_warning));
            noteTitleEditText.requestFocus();
            Toast.makeText(this, getString(R.string.empty_title_warning), Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.isEmpty()) {
            noteContentEditText.setError(getString(R.string.empty_content_warning));
            noteContentEditText.requestFocus();
            Toast.makeText(this, getString(R.string.empty_content_warning), Toast.LENGTH_SHORT).show();
            return;
        }

        boolean ok = StorageProvider.get(this).saveNote(this, title, content);
        if (ok) {
            Toast.makeText(this, getString(R.string.note_saved_message), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, getString(R.string.save_error_message), Toast.LENGTH_SHORT).show();
        }
    }
}
