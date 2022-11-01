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

public class SignUpActivityTests {
    @Rule
    public ActivityScenarioRule<SignUpActivity> signupActivityScenario = new ActivityScenarioRule<>(SignUpActivity.class);

    @Test
    public void SignUpActivity_RendersCorrectly() {
        onView(withId(R.id.signupUsernameField)).check(matches(isDisplayed()));
        onView(withId(R.id.signupPasswordField)).check(matches(isDisplayed()));
        onView(withId(R.id.signupFullNameField)).check(matches(isDisplayed()));
        onView(withId(R.id.signupPhoneField)).check(matches(isDisplayed()));
        onView(withId(R.id.signupEmailField)).check(matches(isDisplayed()));
        onView(withId(R.id.signupConfirmButton)).check(matches(isDisplayed()));
        onView(withId(R.id.signupCancelButton)).check(matches(isDisplayed()));
    }

    @Test
    public void SignUpActivity_ElementsWorking() {
        onView(withId(R.id.signupUsernameField)).perform(typeText("ignas"));
        onView(withId(R.id.signupPasswordField)).perform(typeText("pw"));
        onView(withId(R.id.signupFullNameField)).perform(typeText("Ignas K"));
        onView(withId(R.id.signupPhoneField)).perform(typeText("866655465454"));
        onView(withId(R.id.signupEmailField)).perform(typeText("ignas@gmail.com"));
        onView(withId(R.id.signupConfirmButton)).perform(click());
        onView(withId(R.id.signupCancelButton)).perform(click());
    }
}
