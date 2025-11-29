package com.example.lab6_lab4_rimantek;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SharedPrefsNoteStorage implements com.example.lab6_lab4_rimantek.NoteStorage {

    private static final String PREFS = "notes_prefs";
    private static final String KEY_TITLES = "titles";
    private static final String NOTE_PREFIX = "note_";

    private SharedPreferences sp(Context ctx) {
        return ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    @Override
    public List<String> listNoteTitles(Context ctx) {
        Set<String> titles = sp(ctx).getStringSet(KEY_TITLES, new TreeSet<>(String::compareToIgnoreCase));
        return new ArrayList<>(titles);
    }

    @Override
    public boolean saveNote(Context ctx, String title, String content) {
        SharedPreferences sp = sp(ctx);
        Set<String> titles = new TreeSet<>(String::compareToIgnoreCase);
        titles.addAll(sp.getStringSet(KEY_TITLES, new TreeSet<>()));
        titles.add(title);
        return sp.edit()
                .putStringSet(KEY_TITLES, titles)
                .putString(NOTE_PREFIX + title, content)
                .commit();
    }

    @Override
    public boolean deleteNote(Context ctx, String title) {
        SharedPreferences sp = sp(ctx);
        Set<String> titles = new TreeSet<>(String::compareToIgnoreCase);
        titles.addAll(sp.getStringSet(KEY_TITLES, new TreeSet<>()));
        if (!titles.remove(title)) return false;
        return sp.edit()
                .putStringSet(KEY_TITLES, titles)
                .remove(NOTE_PREFIX + title)
                .commit();
    }

    @Override
    public String loadNote(Context ctx, String title) {
        return sp(ctx).getString(NOTE_PREFIX + title, null);
    }

    @Override
    public String name(Context ctx) {
        return ctx.getString(R.string.backend_prefs); // ← from resources
    }
}
