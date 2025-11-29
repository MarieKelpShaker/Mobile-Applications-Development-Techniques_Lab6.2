package com.example.lab6_lab4_rimantek;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileNoteStorage implements com.example.lab6_lab4_rimantek.NoteStorage {

    private static final String EXT = ".txt";

    @Override
    public List<String> listNoteTitles(Context ctx) {
        File dir = ctx.getFilesDir();
        File[] files = dir.listFiles((d, name) -> name.endsWith(EXT));
        List<String> titles = new ArrayList<>();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName, String::compareToIgnoreCase));
            for (File f : files) {
                String n = f.getName();
                titles.add(n.substring(0, n.length() - EXT.length()));
            }
        }
        return titles;
    }

    @Override
    public boolean saveNote(Context ctx, String title, String content) {
        try (FileOutputStream fos = ctx.openFileOutput(title + EXT, Context.MODE_PRIVATE)) {
            fos.write(content.getBytes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteNote(Context ctx, String title) {
        return ctx.deleteFile(title + EXT);
    }

    @Override
    public String loadNote(Context ctx, String title) {
        try {
            return new String(ctx.openFileInput(title + EXT).readAllBytes());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String name(Context ctx) {
        return ctx.getString(R.string.backend_files); // ← from resources
    }
}
