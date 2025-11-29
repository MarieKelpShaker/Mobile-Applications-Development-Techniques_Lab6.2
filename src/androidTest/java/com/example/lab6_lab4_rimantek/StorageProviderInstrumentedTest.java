package com.example.lab6_lab4_rimantek;

import static org.junit.Assert.*;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StorageProviderInstrumentedTest {

    private static final String SETTINGS = "settings";

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Reset settings so tests are deterministic
        SharedPreferences sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

    @Test
    public void get_returnsFileStorageByDefault() {
        NoteStorage storage = StorageProvider.get(context);
        assertTrue(storage instanceof FileNoteStorage);
        assertEquals(context.getString(R.string.backend_files), StorageProvider.currentName(context));
    }

    @Test
    public void toggle_switchesBetweenFilesAndPrefs() {
        // Default should be Files
        NoteStorage first = StorageProvider.get(context);
        String firstName = StorageProvider.currentName(context);

        StorageProvider.toggle(context);

        NoteStorage second = StorageProvider.get(context);
        String secondName = StorageProvider.currentName(context);

        assertNotEquals(first.getClass(), second.getClass());
        assertNotEquals(firstName, secondName);

        // Toggle back
        StorageProvider.toggle(context);
        NoteStorage third = StorageProvider.get(context);
        String thirdName = StorageProvider.currentName(context);

        assertEquals(first.getClass(), third.getClass());
        assertEquals(firstName, thirdName);
    }
}
