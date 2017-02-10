package fi.tamk.teemu.fooddiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 26.4.2016
 * @version 2.0
 * @since 1.8
 */
public class AddFood extends Service {

    /** name of food */
    private String name;
    /** amount of food */
    private int amount;
    /** proteins of food */
    private int prot;
    /** carbs of food */
    private int carb;
    /** fats of food */
    private int fat;
    /** calories of food */
    private int cal;
    /** selected date */
    private String date;
    /** selected meal */
    private int meal;
    /** selected username */
    private String username;

    /**
     * Binds Service to components such as Activity
     *
     * @param intent abstract description of an operation to be performed
     * @return null
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Starts this Service and along that starts establishing the connection to backend.
     *
     * Shows message to user if there is problem with input or url.
     *
     * @param intent  abstract description of an operation to be performed
     * @param flags flags
     * @param startId unique integer representing this specific request to start
     * @return START_STICKY service runs in background
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            name = intent.getStringExtra("name");
            amount = intent.getIntExtra("amount", 0);
            prot = intent.getIntExtra("prot", 0);
            carb = intent.getIntExtra("carb", 0);
            fat = intent.getIntExtra("fat", 0);
            date = intent.getStringExtra("date");
            meal = intent.getIntExtra("meal", 0);
            cal = intent.getIntExtra("cal", 0);
            username = intent.getStringExtra("user");

        } catch (NullPointerException e) {

            Toast.makeText(AddFood.this, "Syötteessä virhe", Toast.LENGTH_SHORT).show();
            AddFood.this.stopSelf();
        }

        URL url = null;

        try {
            url = new URL("http://myapp-tkbackend.rhcloud.com/addfood?name="
                    +name+"&amount="+amount+"&prot="+prot+
                    "&carb="+carb+"&fat="+fat+"&cal="+cal+"&date="+
                    date+"&meal="+meal+"&user="+username);
        } catch(MalformedURLException e) {

            Toast.makeText(AddFood.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
            AddFood.this.stopSelf();
        }

        new FetchURL().execute(url);

        return START_STICKY;
    }

    /**
     * Inner class for establishing connection to backend.
     *
     * This Service takes care of sending food data to backend.
     *
     * If problem occurs with connection, Service stops itself and shows user a message.
     */
    class FetchURL extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {

            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
            } catch (IOException e) {

                Toast.makeText(AddFood.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                AddFood.this.stopSelf();
            }
            String response ="";

            if (urlConnection != null) {

                try {

                    BufferedReader in = new BufferedReader(new InputStreamReader
                            (urlConnection.getInputStream()));

                    String line;
                    while ((line = in.readLine()) != null) {

                        response += line;
                    }
                    in.close();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    urlConnection.disconnect();
                }
            } else {

                Toast.makeText(AddFood.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                AddFood.this.stopSelf();
            }

            return response;
        }

        /**
         * Stops the service when connection is doned
         *
         * @param response from backend
         */
        @Override
        protected void onPostExecute(String response) {
            AddFood.this.stopSelf();
        }
    }
}
