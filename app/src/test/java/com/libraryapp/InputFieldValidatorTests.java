package com.libraryapp;

import com.libraryapp.Utilities.InputFieldValidator;

import org.junit.Assert;
import org.junit.Test;

public class InputFieldValidatorTests {
    @Test
    public void InputFieldValidator_loginFieldsAreEmpty_ShouldReturnTrueWhenPassedAnEmptyString() {
        Assert.assertTrue(InputFieldValidator.loginFieldsAreEmpty("", ""));
    }

    @Test
    public void InputFieldValidator_loginFieldsAreEmpty_ShouldReturnTrueWhenPassedStringWithSpace() {
        Assert.assertTrue(InputFieldValidator.loginFieldsAreEmpty("      ", "dsa"));
    }

    @Test
    public void InputFieldValidator_loginFieldsAreEmpty_ShouldReturnFalseWhenPassedAnEmptyString() {
        Assert.assertFalse(InputFieldValidator.loginFieldsAreEmpty("user", "user"));
    }

    @Test
    public void InputFieldValidator_signupFieldsAreEmpty_ShouldReturnTrueWhenPassedTextString() {
        Assert.assertTrue(InputFieldValidator.signupFieldsAreEmpty("", "", "", "", ""));
    }

    @Test
    public void InputFieldValidator_signupFieldsAreEmpty_ShouldReturnTrueWhenPassedAnEmptyString() {
        Assert.assertTrue(InputFieldValidator.signupFieldsAreEmpty("  ", "  ", "  ", "    ", " "));
    }

    @Test
    public void InputFieldValidator_signupFieldsAreEmpty_ShouldReturnFalseWhenPassedTextString() {
        Assert.assertFalse(InputFieldValidator.signupFieldsAreEmpty("user", "pw", "Ignas K", "1111", "ignas@gmail.com"));
    }
}
