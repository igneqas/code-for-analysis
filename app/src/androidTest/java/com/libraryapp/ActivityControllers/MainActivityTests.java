package com.libraryapp.ActivityControllers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.libraryapp.R;

import org.junit.Rule;
import org.junit.Test;

public class MainActivityTests {
    @Rule
    public ActivityScenarioRule<MainActivity> mainActivityScenario = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void MainActivity_RendersCorrectly() {
        onView(withId(R.id.booksList)).check(matches(isDisplayed()));
    }
}
