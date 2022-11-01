package com.libraryapp.ActivityControllers;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import com.libraryapp.R;

import org.junit.Rule;
import org.junit.Test;

public class LoginActivityTests {
    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityScenario = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void LoginActivity_RendersCorrectly() {
        onView(withId(R.id.usernameField)).check(matches(isDisplayed()));
        onView(withId(R.id.passwordField)).check(matches(isDisplayed()));
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        onView(withId(R.id.loginToSignupButton)).check(matches(isDisplayed()));
    }

    @Test
    public void LoginActivity_ElementsWorking() {
        onView(withId(R.id.usernameField)).perform(typeText("ignas"));
        onView(withId(R.id.passwordField)).perform(typeText("asd"));
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.loginToSignupButton)).perform(click());
    }
}
