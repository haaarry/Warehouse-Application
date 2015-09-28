package com.sky.pinkvelocity.warehouse.app;

import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hac10 on 28/09/15.
 */
public class RetrieveHTTPDataAsync extends AsyncTask<String, Void, String> {

    private OnRetrieveHttpData listener;


    @Override
    protected String doInBackground(String...urls) {
        String responseData = null;
        if (urls.length > 0)
        {
            responseData = getHTTPData (urls[0]);
        }

        return responseData;
    }

    public RetrieveHTTPDataAsync (OnRetrieveHttpData listener){
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(String httpData){
        listener.onTaskCompleted(httpData);
    }

    public String getHTTPData(String url){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            }
        }
        catch (Exception e){}
        String dataString =  builder.toString();

        return dataString;



    }
}
