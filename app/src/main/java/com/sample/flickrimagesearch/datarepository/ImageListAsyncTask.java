package com.sample.flickrimagesearch.datarepository;

import android.os.AsyncTask;

import com.sample.flickrimagesearch.contracts.ApiCallBack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageListAsyncTask extends AsyncTask<String, Void, String> {

   ApiCallBack apiCallBack;

    public ImageListAsyncTask(ApiCallBack apiCallBack) {
        this.apiCallBack = apiCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        apiCallBack.onRequest();
    }

    @Override
    protected String doInBackground(String... strings) {

        try {

            URL obj = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + strings[0]);
            System.out.println("Response Code : " + responseCode);

            if (responseCode != 200)
                apiCallBack.onFail();
            else {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null)
            apiCallBack.onResponse(response);
    }
}
