package com.noela.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView resultTextView;









    public void findWeather(View view) {

        Log.i("City name", cityName.getText().toString());

        InputMethodManager mng = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mng.hideSoftInputFromWindow(cityName.getWindowToken(), 0);  // we use this when we want to hide keyboard;


        try {
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");


            DownloadTask task = new DownloadTask();
            //*task.execute("http://api.openweathermap.org/data/2.5/weather?appId=69a5b382d50dcb5aa888f4f83cc1bf1c&q=" + encodedCityName);
            task.execute("httP://api.openweathermap.org/data/2.5/weather?&units=metric&appId=69a5b382d50dcb5aa888f4f83cc1bf1c&q=" + encodedCityName);


        } catch (Exception e) {

           

            Message msg = handler.obtainMessage();
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }



    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String...urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls [0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }
                return result;

            } catch (Exception e) {


                Message msg = handler.obtainMessage();
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);



            try {


                String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("main");

                String weatherDetail = jsonObject.getString("weather");


                Log.i("Weather content", weatherInfo);

                Log.i("Weather details:", weatherDetail);


                JSONArray array = new JSONArray(weatherDetail);

                String main = "";

                String description ="";

                for(int i = 0; i < array.length(); i++) {

                    JSONObject jsonO = array.getJSONObject(i);

                    main = jsonO.getString("main");
                    description = jsonO.getString("description");

                }


                    JSONObject jsonPart =  new JSONObject(weatherInfo);

                    String temp = "";

                    String tempMin= "";
                    String tempMax= "";



                    temp = jsonPart.getString("temp");
                    tempMin = jsonPart.getString("temp_min");
                    tempMax  = jsonPart.getString("temp_max");

                    char celzius = 0x00B0;




                    if(temp != "" && tempMin != "" && tempMax != "" && main != "" && description != "") {

                        message +=  "temp :  " + temp + celzius + "\r\n" + "tempMin:  " +  tempMin + celzius + "\r\n" +   "tempMax: "  + tempMax + celzius + "\r\n " + "\r\n" + "main: "

                        + main + "\r\n" + "description: "  + description + "\r\n";


                    }




                if (message != " ") {

                 resultTextView.setText(message);


                } else {



                    Message msg = handler.obtainMessage();
                    msg.arg1 = 1;
                    handler.sendMessage(msg);

                }

            } catch (Exception e) {



                Message msg = handler.obtainMessage();
                msg.arg1 = 1;
                handler.sendMessage(msg);
            }



        }
    }


    private final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.arg1 == 1)
                Toast.makeText(getApplicationContext(),"Could not found weather", Toast.LENGTH_LONG).show();
        }
    };










}



