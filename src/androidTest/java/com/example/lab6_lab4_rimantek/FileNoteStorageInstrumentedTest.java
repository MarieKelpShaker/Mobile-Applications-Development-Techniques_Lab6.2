package com.example.lab6_lab4_rimantek;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class FileNoteStorageInstrumentedTest {

    private Context context;
    private FileNoteStorage storage;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        storage = new FileNoteStorage();

        // Clean up any .txt files created by previous runs
        for (String name : context.fileList()) {
            if (name.endsWith(".txt")) {
                context.deleteFile(name);
            }
        }
    }

    @Test
    public void saveAndLoad_roundTrip() {
        boolean saved = storage.saveNote(context, "title1", "content1");
        assertTrue(saved);

        String loaded = storage.loadNote(context, "title1");
        assertEquals("content1", loaded);
    }

    @Test
    public void listNoteTitles_returnsSortedTitles() {
        storage.saveNote(context, "b_title", "1");
        storage.saveNote(context, "a_title", "2");

        List<String> titles = storage.listNoteTitles(context);

        assertEquals(Arrays.asList("a_title", "b_title"), titles);
    }

    @Test
    public void deleteNote_removesFile() {
        storage.saveNote(context, "todelete", "something");
        assertNotNull(storage.loadNote(context, "todelete"));

        boolean deleted = storage.deleteNote(context, "todelete");
        assertTrue(deleted);
        assertNull(storage.loadNote(context, "todelete"));
    }

    @Test
    public void name_usesFilesStringResource() {
        String name = storage.name(context);
        assertEquals(context.getString(R.string.backend_files), name);
    }
}
