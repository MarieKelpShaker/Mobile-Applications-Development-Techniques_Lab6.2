package com.example.lab6_lab4_rimantek;

import android.content.Context;
import java.util.List;

public interface NoteStorage {
    List<String> listNoteTitles(Context ctx);
    boolean saveNote(Context ctx, String title, String content);
    boolean deleteNote(Context ctx, String title);
    String loadNote(Context ctx, String title);
    String name(Context ctx); // ← changed: use resources instead of hard-coded text
}
