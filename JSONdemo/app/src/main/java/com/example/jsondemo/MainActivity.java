package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

           try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

               InputStream in = urlConnection.getInputStream();

               InputStreamReader reader = new InputStreamReader(in);

               int data = reader.read();

               while (data != -1){
                   char current = (char) data;

                   result += current;

                   data = reader.read();
               }

               return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e){
               e.printStackTrace();
           }
            return null;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherinfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherinfo);

                for (int i = 0; i<arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
                Log.i("weather", weatherinfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Json", result);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
    }
}
