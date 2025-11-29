package com.example.lab6_lab4_rimantek;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SharedPrefsNoteStorageInstrumentedTest {

    private static final String PREFS = "notes_prefs";

    private Context context;
    private SharedPrefsNoteStorage storage;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        storage = new SharedPrefsNoteStorage();

        // Clear note shared preferences before each test
        SharedPreferences sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    @Test
    public void saveAndLoad_roundTrip() {
        boolean saved = storage.saveNote(context, "title1", "content1");
        assertTrue(saved);

        String loaded = storage.loadNote(context, "title1");
        assertEquals("content1", loaded);
    }

    @Test
    public void listNoteTitles_returnsAllSavedTitles() {
        storage.saveNote(context, "titleA", "A");
        storage.saveNote(context, "titleB", "B");

        List<String> titles = storage.listNoteTitles(context);

        // Order is TreeSet with case-insensitive compare; both titles must be present
        assertTrue(titles.contains("titleA"));
        assertTrue(titles.contains("titleB"));
        assertEquals(2, titles.size());
    }

    @Test
    public void deleteNote_removesTitleAndContent() {
        storage.saveNote(context, "todelete", "something");
        assertNotNull(storage.loadNote(context, "todelete"));

        boolean deleted = storage.deleteNote(context, "todelete");
        assertTrue(deleted);

        assertNull(storage.loadNote(context, "todelete"));
        assertFalse(storage.listNoteTitles(context).contains("todelete"));
    }

    @Test
    public void name_usesPrefsStringResource() {
        String name = storage.name(context);
        assertEquals(context.getString(R.string.backend_prefs), name);
    }
}
