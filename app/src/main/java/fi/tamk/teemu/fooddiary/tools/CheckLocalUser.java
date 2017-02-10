package fi.tamk.teemu.fooddiary.tools;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * If user uses application for the first time, user must give a username. Username is used
 * for identification at backend.
 *
 * Username is saved to private .txt file for later use.
 *
 * @author Teemu Kaunisto <teemu.kaunisto@cs.tamk.fi> on 11.5.2016
 * @version 2.0
 * @since 1.8
 */
public class CheckLocalUser {

    /** pointer to MainActivity */
    Activity activity;
    /** name of the local file */
    static String FILENAME = "USERNAME.txt";

    /**
     *  Sets pointer to MainActivity
     *
     * @param activity pointer to MainActivity
     */
    public CheckLocalUser(Activity activity) {
        this.activity = activity;
    }

    /**
     * Saves username to local .txt file.
     *
     * @param username name of the user
     */
    public void saveUser(String username) {

        FileOutputStream outputStream = null;

        try {
            outputStream = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(outputStream != null) {
            try {
                outputStream.write(username.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * Gets user from local .txt file
     *
     * @return name of the user or "" if user is not found
     */
    public String getUser() {

        FileInputStream inputStream = null;

        try {
            inputStream = activity.openFileInput(FILENAME);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            String name = bufferedReader.readLine();
            return name;

        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            try {
                if (inputStream != null)
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }
}
