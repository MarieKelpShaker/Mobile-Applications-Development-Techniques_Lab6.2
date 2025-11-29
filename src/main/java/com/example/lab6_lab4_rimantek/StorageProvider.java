package com.example.lab6_lab4_rimantek;

import android.content.Context;
import android.content.SharedPreferences;

public final class StorageProvider {
    private StorageProvider() {}

    private static final String SETTINGS = "settings";
    private static final String KEY_BACKEND = "storage_backend";
    public static final String BACKEND_FILES = "files";
    public static final String BACKEND_PREFS = "prefs";

    private static String currentBackend(Context ctx) {
        return ctx.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)
                .getString(KEY_BACKEND, BACKEND_FILES);
    }

    public static com.example.lab6_lab4_rimantek.NoteStorage get(Context ctx) {
        String b = currentBackend(ctx);
        return BACKEND_PREFS.equals(b) ? new com.example.lab6_lab4_rimantek.SharedPrefsNoteStorage() : new com.example.lab6_lab4_rimantek.FileNoteStorage();
    }

    public static void toggle(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        String b = currentBackend(ctx);
        String nb = BACKEND_PREFS.equals(b) ? BACKEND_FILES : BACKEND_PREFS;
        sp.edit().putString(KEY_BACKEND, nb).apply();
    }

    public static String currentName(Context ctx) {
        // now resolves to a string resource through the storage impl
        return get(ctx).name(ctx);
    }
}
