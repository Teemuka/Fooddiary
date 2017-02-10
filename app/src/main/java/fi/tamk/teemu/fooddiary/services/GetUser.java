package fi.tamk.teemu.fooddiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 15.5.2016
 * @version 2.0
 * @since 1.8
 */
public class GetUser extends Service {

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
     * Shows message to user if there is problem with url.
     *
     * @param intent  abstract description of an operation to be performed
     * @param flags flags
     * @param startId unique integer representing this specific request to start
     * @return START_STICKY service runs in background
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            username = intent.getStringExtra("user");

        } catch (NullPointerException e) {

            GetUser.this.stopSelf();
        }

        URL url = null;

        try {
            url = new URL("http://myapp-tkbackend.rhcloud.com/getuser?user="+username);
        } catch(MalformedURLException e) {

            Toast.makeText(GetUser.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
            GetUser.this.stopSelf();
        }

        new FetchURL().execute(url);

        return START_STICKY;
    }

    /**
     * Inner class for establishing connection to backend.
     *
     * This Service takes care of checking is user already exists.
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

                Toast.makeText(GetUser.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                GetUser.this.stopSelf();
            }
            String response ="";

            if (urlConnection != null) {

                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

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
                Toast.makeText(GetUser.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                GetUser.this.stopSelf();
            }

            try {

                final JSONArray jsonArray = new JSONArray(response);

                Intent intent = new Intent("userexist");

                if (jsonArray.length() != 0) {
                    intent.putExtra("userexist", true);
                } else {
                    intent.putExtra("userexist", false);
                }

                getApplicationContext().sendBroadcast(intent);

            } catch (JSONException e) {
                e.printStackTrace();
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
            GetUser.this.stopSelf();

        }
    }
}
