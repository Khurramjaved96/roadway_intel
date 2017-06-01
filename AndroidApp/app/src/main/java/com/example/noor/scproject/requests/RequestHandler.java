package com.example.noor.scproject.requests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by noor on 5/30/17.
 */

public class RequestHandler {

    /**
     *
     * This function establishes HttpURLconnection and sets values of its set up parameters
     *
     * @param url url of server we are establising connection with
     * @return HttpURLconnection instance
     */
    public HttpURLConnection getHttpConnection(URL url){
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(20000);   //timeout value when opening communication link
            connection.setRequestMethod("POST");   //request method
            connection.setReadTimeout(20000);   //timeout value to be used in milliseconds
            connection.setDoInput(true);   //set to true to use URL connection for input
            connection.setDoOutput(true);  //set to true to use URL connection for output
            return connection;
        }
        catch(Exception e){
            return null;
        }


    }

    /**
     *
     * @param requestURL   URL to which post request will be sent
     * @param postDataParams   Post request parameters
     * @return response string
     */
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams){
        URL url;
        String response = "";
        try{
            url = new URL(requestURL);
            HttpURLConnection connection = getHttpConnection(url);  //get http connection
            OutputStream outputStream = connection.getOutputStream();    //Returns an output stream that writes to this connection
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            bufferedWriter.write(postDataParams.get("image"));   //write image
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            int rc = connection.getResponseCode();   //get request response code
            if(rc == HttpURLConnection.HTTP_OK){
                //if response code is 200
                //get input stream that reads the response from connection and read it using buffered writer
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = bufferedReader.readLine();
            }
            else{
                response = "Error";
            }



        }
        catch(Exception e){

        }
        return response;
    }
}
