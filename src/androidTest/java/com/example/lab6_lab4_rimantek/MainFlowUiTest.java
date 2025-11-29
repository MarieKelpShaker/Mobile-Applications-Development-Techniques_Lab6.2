package com.example.lab6_lab4_rimantek;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.Visibility;

import static org.hamcrest.Matchers.is;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class MainFlowUiTest {

    private static final String SETTINGS = "settings";
    private static final String PREFS = "notes_prefs";

    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Clear storage backends so each test starts fresh
        // 1) Files
        for (String name : context.fileList()) {
            if (name.endsWith(".txt")) {
                context.deleteFile(name);
            }
        }
        // 2) SharedPrefs-backed notes
        SharedPreferences spNotes = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        spNotes.edit().clear().commit();

        // 3) StorageProvider settings (so default backend is Files)
        SharedPreferences spSettings = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        spSettings.edit().clear().commit();
    }

    @Test
    public void mainActivity_showsEmptyViewWhenNoNotes() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            // Because ListView is empty, its emptyView TextView should be visible
            onView(withId(R.id.emptyView))
                    .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
        }
    }

    @Test
    public void addNote_showsInList_andCanBeOpened() {
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            // Open "Add Note" from overflow menu
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
            onView(withText(R.string.menu_add_note)).perform(click());

            // Fill in title and content
            String title = "Test note";
            String content = "This is the body.";

            onView(withId(R.id.noteTitleEditText))
                    .perform(typeText(title), closeSoftKeyboard());
            onView(withId(R.id.noteContentEditText))
                    .perform(typeText(content), closeSoftKeyboard());

            // Save
            onView(withId(R.id.saveButton)).perform(click());

            // We should be back in MainActivity, note appears in ListView
            onView(withId(R.id.emptyView))
                    .check(matches(withEffectiveVisibility(Visibility.GONE)));

            // Click the note in the ListView to open ViewNoteActivity
            onData(is(title))
                    .inAdapterView(withId(R.id.notesListView))
                    .perform(click());

            // Check that ViewNoteActivity displays the correct title and content
            onView(withId(R.id.viewNoteTitle))
                    .check(matches(withText(title)));
            onView(withId(R.id.viewNoteContent))
                    .check(matches(withText(content)));
        }
    }

    @Test
    public void deleteNote_removesFromMainList() {
        // Pre-populate a note using the storage API
        NoteStorage storage = StorageProvider.get(context);
        String title = "ToDelete";
        String content = "Some content";
        storage.saveNote(context, title, content);

        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {

            // Note is visible in MainActivity list
            onData(is(title))
                    .inAdapterView(withId(R.id.notesListView))
                    .check(matches(isDisplayed()));

            // Open DeleteNoteActivity via menu
            openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
            onView(withText(R.string.menu_delete_note)).perform(click());

            // Select the note in the delete list
            onData(is(title))
                    .inAdapterView(withId(R.id.deleteListView))
                    .perform(click());

            // Click delete button
            onView(withId(R.id.deleteButton)).perform(click());

            // Go back to MainActivity
            androidx.test.espresso.Espresso.pressBack();

            // Now the list should be empty, and emptyView should be visible
            onView(withId(R.id.emptyView))
                    .check(matches(withEffectiveVisibility(Visibility.VISIBLE)));
        }
    }
}
