package com.libraryapp.Utilities;

public class InputFieldValidator {

    public static boolean loginFieldsAreEmpty(String username, String password){
        return fieldIsEmpty(username) || fieldIsEmpty(password);
    }

    public static boolean signupFieldsAreEmpty(String username, String password, String fullName, String phoneNumber, String email){
        return fieldIsEmpty(username) || fieldIsEmpty(password) || fieldIsEmpty(fullName) || fieldIsEmpty(phoneNumber) || fieldIsEmpty(email);
    }

    public static boolean fieldIsEmpty(String text){
        return text.trim().isEmpty();
    }
}
