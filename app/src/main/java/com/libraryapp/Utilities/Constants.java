package com.libraryapp.Utilities;

public class Constants {
    public final static String ADDRESS = "http:192.168.32.135:8080/rest";//192.168.66.253
    //public final static String ADDRESS = "http:192.168.66.253:8080/rest";
    public final static String LOGIN_URL = ADDRESS + "/login";
    public final static String SIGNUP_URL = ADDRESS + "/signup";
    public final static String GET_ALL_BOOKS_URL = ADDRESS + "/allBooks";
    public final static String GET_USER_URL = ADDRESS + "/getUser/";
    public final static String UPDATE_USER_URL = ADDRESS + "/updateUser/";
    public final static String DELETE_USER_URL = ADDRESS + "/deleteUser/";
    public final static String CREATE_ORDER_URL = ADDRESS + "/createOrder";
    public final static String GET_USER_ORDERS_URL = ADDRESS + "/getOrdersByUser/";
}
