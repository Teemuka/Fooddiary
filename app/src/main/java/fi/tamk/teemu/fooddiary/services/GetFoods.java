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
import java.util.ArrayList;

import fi.tamk.teemu.fooddiary.models.UserFoodItem;

/**
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 26.4.2016
 * @version 2.0
 * @since 1.8
 */
public class GetFoods extends Service {

    /** selected date */
    private String date;
    /** current user */
    private String username;
    /** temp array for breakfast foods */
    private ArrayList<UserFoodItem> tempBreakfast;
    /** temp array for snack foods */
    private ArrayList<UserFoodItem> tempSnack;
    /** temp array for lunch foods */
    private ArrayList<UserFoodItem> tempLunch;
    /** temp array for meal foods */
    private ArrayList<UserFoodItem> tempMeal;
    /** temp array for supper foods */
    private ArrayList<UserFoodItem> tempSupper;

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

        tempBreakfast = new ArrayList<>();
        tempSnack = new ArrayList<>();
        tempLunch = new ArrayList<>();
        tempMeal = new ArrayList<>();
        tempSupper = new ArrayList<>();

        try {
            date = intent.getStringExtra("date");
            username = intent.getStringExtra("user");

        } catch (NullPointerException e) {

            Toast.makeText(GetFoods.this, "Käyttäjää ei löydetty", Toast.LENGTH_SHORT).show();
            GetFoods.this.stopSelf();
        }

        URL url = null;

        try {
            url = new URL("http://myapp-tkbackend.rhcloud.com/getfoods?date="+date+"&user="+username);
        } catch(MalformedURLException e) {

            Toast.makeText(GetFoods.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
            GetFoods.this.stopSelf();
        }

        new FetchURL().execute(url);

        return START_STICKY;
    }

    /**
     * Inner class for establishing connection to backend.
     *
     * This Service takes care of fetching food data from backend.
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

                Toast.makeText(GetFoods.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                GetFoods.this.stopSelf();
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
                Toast.makeText(GetFoods.this, "Yhteys epäonnistui", Toast.LENGTH_SHORT).show();
                GetFoods.this.stopSelf();
            }

            try {

                final JSONArray jsonArray = new JSONArray(response);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for (int i = 0; i < jsonArray.length(); i++) {

                            try {

                                String name = jsonArray.getJSONObject(i).getString("name");
                                int prot = jsonArray.getJSONObject(i).getInt("prot");
                                int fat = jsonArray.getJSONObject(i).getInt("fat");
                                int carb = jsonArray.getJSONObject(i).getInt("carb");
                                int quantity = jsonArray.getJSONObject(i).getInt("amount");
                                int cal = jsonArray.getJSONObject(i).getInt("cal");

                                if (jsonArray.getJSONObject(i).getInt("meal") == 0) {
                                    tempBreakfast.add(new UserFoodItem(name, quantity,prot,carb,fat, cal));
                                } else if (jsonArray.getJSONObject(i).getInt("meal") == 1) {
                                    tempSnack.add(new UserFoodItem(name, quantity,prot,carb,fat, cal));
                                } else if (jsonArray.getJSONObject(i).getInt("meal") == 2) {
                                    tempLunch.add(new UserFoodItem(name, quantity,prot,carb,fat,cal));
                                } else if (jsonArray.getJSONObject(i).getInt("meal") == 3) {
                                    tempMeal.add(new UserFoodItem(name, quantity,prot,carb,fat,cal));
                                } else if (jsonArray.getJSONObject(i).getInt("meal") == 4) {
                                    tempSupper.add(new UserFoodItem(name, quantity,prot,carb,fat, cal));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Intent intent1 = new Intent("getfoods");
                        intent1.putParcelableArrayListExtra("breakfast", tempBreakfast);
                        intent1.putParcelableArrayListExtra("snack", tempSnack);
                        intent1.putParcelableArrayListExtra("lunch", tempLunch);
                        intent1.putParcelableArrayListExtra("meal", tempMeal);
                        intent1.putParcelableArrayListExtra("supper", tempSupper);

                        getApplicationContext().sendBroadcast(intent1);

                    }
                }).start();

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
            GetFoods.this.stopSelf();
        }
    }
}
