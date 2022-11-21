package com.libraryapp.Utilities;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class RESTController {

    public static final String IOERROR = "Error when sending request";
    public static final String RESPONSE_ERROR = "Error response";

    public static String sendGet(String url) {
        try {
            URL formattedUrl = new URL(url);
            return sendGenericRequest(formattedUrl, "GET");
        } catch (MalformedURLException e){
            Log.e("URL", "Error when forming URL");
            return "";
        }
    }

    public static String sendGet(String url, String id) throws IOException {
        try {
            URL formattedUrl = new URL(url + id);
            return sendGenericRequest(formattedUrl, "GET");
        } catch (MalformedURLException e){
            Log.e("URL", "Error when forming URL");
            return "";
        }
    }

    public static String sendPost(String url, String postDataParams) throws IOException {
        try {
            URL formattedUrl = new URL(url + postDataParams);
            return sendGenericRequest(formattedUrl,"POST");
        } catch (MalformedURLException e){
            Log.e("URL", "Error when forming URL");
            return "";
        }
    }

    private static String sendGenericRequest(URL requestUrl, String requestType) {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
            setHttpConnectionParameters(httpURLConnection, requestType);
            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = inputReader.readLine()) != null) {
                    response.append(inputLine);
                }
                inputReader.close();
                return response.toString();
            }
            return RESPONSE_ERROR;
        } catch (Exception e) {
            return IOERROR;
        }
    }


    private static void setHttpConnectionParameters(HttpURLConnection httpURLConnection, String type) throws ProtocolException {
        httpURLConnection.setRequestMethod(type);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept","application/json");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setDoOutput(true);
    }
}
