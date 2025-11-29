package com.example.lab6_lab4_rimantek;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note_activity);

        TextView titleView = findViewById(R.id.viewNoteTitle);
        TextView contentView = findViewById(R.id.viewNoteContent);

        String title = getIntent().getStringExtra("title");
        titleView.setText(title);

        String content = StorageProvider.get(this).loadNote(this, title);
        contentView.setText(content != null ? content : "(empty)");
    }
}