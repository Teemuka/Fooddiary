package fi.tamk.teemu.fooddiary;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;

import fi.tamk.teemu.fooddiary.adapters.FoodDiaryArrayAdapter;
import fi.tamk.teemu.fooddiary.dialogs.AddFoodDialogFragment;
import fi.tamk.teemu.fooddiary.dialogs.DatePickerFragment;
import fi.tamk.teemu.fooddiary.dialogs.UserNameDialogFragment;
import fi.tamk.teemu.fooddiary.interfaces.MyDialogListener;
import fi.tamk.teemu.fooddiary.models.GeneralFoodItem;
import fi.tamk.teemu.fooddiary.models.FoodStorage;
import fi.tamk.teemu.fooddiary.models.MealSet;
import fi.tamk.teemu.fooddiary.models.UserFoodItem;
import fi.tamk.teemu.fooddiary.services.AddFood;
import fi.tamk.teemu.fooddiary.services.GetFoods;
import fi.tamk.teemu.fooddiary.services.GetUser;
import fi.tamk.teemu.fooddiary.tools.CheckLocalUser;
import fi.tamk.teemu.fooddiary.tools.MyJSONParser;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        MyDialogListener {

    /** Pager adapter for easy usability */
    PagerAdapter pagerAdapter;
    /** ViewPager needed for pager */
    ViewPager viewPager;
    DatePickerFragment datepicker;
    /** holds all food items */
    FoodStorage storage;
    /** holds names of mealsets */
    static String[] MEAL_TYPES;

    /** add food button */
    FloatingActionButton fab;
    /** meal sets */
    MealSet breakfast;
    MealSet snack;
    MealSet lunch;
    MealSet meal;
    MealSet supper;
    /** custom JSONparser */
    MyJSONParser jsonParser;
    /** for checking existing users */
    CheckLocalUser checkLocalUser;

    /** service intents */
    Intent addFood;
    Intent getFoods;
    Intent getUser;

    /** hashmap for meal sets */
    HashMap<Integer, MealSet> mealSets;

    /** variables for dates */
    private int newDay;
    private int newMonth;
    private int newYear;

    /** user's name */
    private String username;

    /**
     * Called first when application is started up.
     *
     * Main task for onCreate is to instantiate variables, but also it starts thread which parses
     * JSON data. onCreate also fires up the UserNameDialogFragment if username is not defined
     * (username is not saved locally).
     *
     * Floating action button is instantiated with onClickListener which fires up
     * AddDialogFoodFragment
     *
     * @see UserNameDialogFragment
     * @see AddFoodDialogFragment
     *
     * @param savedInstanceState A mapping from String values to various Parcelable types
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFood = new Intent(this, AddFood.class);
        getFoods = new Intent(this, GetFoods.class);
        getUser = new Intent(this, GetUser.class);

        mealSets = new HashMap<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MEAL_TYPES = getResources().getStringArray(R.array.mealTypes);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        storage = new FoodStorage();

        breakfast = new MealSet();
        snack = new MealSet();
        lunch = new MealSet();
        meal = new MealSet();
        supper = new MealSet();

        mealSets.put(0, breakfast);
        mealSets.put(1, snack);
        mealSets.put(2, lunch);
        mealSets.put(3, meal);
        mealSets.put(4, supper);

        jsonParser = new MyJSONParser(storage, MainActivity.this);
        new Thread(jsonParser).start();

        checkLocalUser = new CheckLocalUser(MainActivity.this);

        if ((username = checkLocalUser.getUser()).equals("")) {

            UserNameDialogFragment userNameDialogFragment = UserNameDialogFragment.newInstance();
            userNameDialogFragment.show(getFragmentManager(), "newUserDialog");

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment myDialogFragment = AddFoodDialogFragment
                        .newInstance(storage.getAllFoodItems());
                myDialogFragment.show(getFragmentManager(), "newFoodDialog");

            }
        });
    }

    /**
     * BroadcastReceiver for receiving info if username is already taken.
     *
     * Depending on result either saves the username or asks for new one.
     */
    private BroadcastReceiver getUserBroadcast = new BroadcastReceiver() {

        /**
         *
         *
         * @param context The Context in which the receiver is running.
         * @param intent The Intent being received.
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getBooleanExtra("userexist", false)) {

                Toast.makeText(MainActivity.this, "Tunnus " + username + " on varattu",
                        Toast.LENGTH_SHORT).show();
                UserNameDialogFragment userNameDialogFragment = UserNameDialogFragment.newInstance();
                userNameDialogFragment.show(getFragmentManager(), "newUserDialog");
            } else {

                Toast.makeText(MainActivity.this, "Tervetuloa " + username, Toast.LENGTH_SHORT)
                        .show();

                checkLocalUser.saveUser(username);
            }
        }
    };

    /**
     * BroadcastReceiver for receiving foods from the database.
     *
     * After data has been acquired, the local UserFoodItem arrays are refreshed with the data.
     * Because refreshing old array adapters never showed the new data upon resume, a new adapter
     * is set after data fetch.
     */
    private BroadcastReceiver getFoodsBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctxt, Intent i) {
            // do stuff to the UI

            breakfast.refreshData(i.<UserFoodItem>getParcelableArrayListExtra("breakfast"));
            snack.refreshData(i.<UserFoodItem>getParcelableArrayListExtra("snack"));
            lunch.refreshData(i.<UserFoodItem>getParcelableArrayListExtra("lunch"));
            meal.refreshData(i.<UserFoodItem>getParcelableArrayListExtra("meal"));
            supper.refreshData(i.<UserFoodItem>getParcelableArrayListExtra("supper"));

            for (int j = 0; j < 5; j++) {

                FoodListFragment fragment = (FoodListFragment) getSupportFragmentManager()
                        .findFragmentByTag("android:switcher:" + viewPager.getId() + ":" + j);

                if (fragment != null) {

                    fragment.setListAdapter(new FoodDiaryArrayAdapter
                            (MainActivity.this, mealSets.get(j).getFoods()));
                    fragment.updateView(mealSets.get(j));
                }
            }
        }
    };

    /**
     * App is resumed when for example home button is pressed and then app is opened again via
     * "recently used apps" button
     *
     * Broadcast receivers must be registered in onResume, because it should be unregistered in
     * onPause for preventing unnecessary system overhead.
     *
     * onResume also sets date to current date and checks for username.
     */
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(getFoodsBroadcast, new IntentFilter("getfoods"));
        registerReceiver(getUserBroadcast, new IntentFilter("userexist"));

        final Calendar c = Calendar.getInstance();
        newYear = c.get(Calendar.YEAR);
        newMonth= c.get(Calendar.MONTH);
        newDay = c.get(Calendar.DAY_OF_MONTH);

        TextView tv = (TextView) findViewById(R.id.date);
        tv.setText(newDay + "." + (newMonth + 1) + "." + newYear);

        if(!username.equals("")) {

            getFoods();
        } else {

            username = checkLocalUser.getUser();
            getFoods();
        }
    }

    /**
     * Unregisters broadcastReceivers
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getFoodsBroadcast);
        unregisterReceiver(getUserBroadcast);
    }

    /**
     * Sets the date to view.
     *
     * @param view view of date picker fragment
     * @param year year
     * @param monthOfYear month
     * @param dayOfMonth day
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        newDay = dayOfMonth;
        newMonth = monthOfYear;
        newYear = year;

        Calendar tempDate = Calendar.getInstance();
        tempDate.set(newYear, newMonth, newDay);

        TextView tv = (TextView) findViewById(R.id.date);
        tv.setText(newDay + "." + (newMonth + 1) + "." + newYear);

        getFoods();

    }

    /**
     * Inner class implementing pager
     */
    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Creates instances of FoodListFragments with correct meal sets
         *
         * @param position position of pager
         * @return FoodListFragment
         */
        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return FoodListFragment.newInstance(position, breakfast);
                case 1:
                    return FoodListFragment.newInstance(position, snack);
                case 2:
                    return FoodListFragment.newInstance(position, lunch);
                case 3:
                    return FoodListFragment.newInstance(position, meal);
                case 4:
                    return FoodListFragment.newInstance(position, supper);
                default:
                    return null;
            }
        }

        /**
         *
         *
         * @return count of pages
         */
        @Override
        public int getCount() {
            return 5;
        }
    }

    /**
     * Takes care of starting service with correct data to add food to database.
     *
     * This method is called when user presses the positive answer button on AddFoodDialogFragment.
     *
     * Also updates the view with recently added food immediately, so the result can be seen right
     * away
     *
     * @param selectedFood user selected food
     * @param foodAmount amount of food
     */
    @Override
    public void onPositiveAddFoodAnswer(GeneralFoodItem selectedFood, int foodAmount) {

        String foodName = selectedFood.getName();

        UserFoodItem tempFood = new UserFoodItem(selectedFood, foodAmount);
        MealSet tempMealSet = mealSets.get(viewPager.getCurrentItem());

        if(!username.equals("")) {

            addFood.putExtra("name", foodName);
            addFood.putExtra("amount", foodAmount);
            addFood.putExtra("prot", tempFood.getProt());
            addFood.putExtra("carb", tempFood.getCarb());
            addFood.putExtra("fat", tempFood.getFat());
            addFood.putExtra("cal", tempFood.getCal());
            addFood.putExtra("date", String.valueOf(newDay) +
                    String.valueOf(newMonth) +
                    String.valueOf(newYear));
            addFood.putExtra("meal", viewPager.getCurrentItem());
            addFood.putExtra("user", username);

            startService(addFood);

            tempMealSet.addFood(tempFood);
            tempMealSet.setTotalProt(tempFood.getProt());
            tempMealSet.setTotalCarb(tempFood.getCarb());
            tempMealSet.setTotalFat(tempFood.getFat());
            tempMealSet.setTotalCal(tempFood.getCal());

            FoodListFragment fragment = (FoodListFragment) getSupportFragmentManager()
                    .findFragmentByTag("android:switcher:" + viewPager.getId() + ":"
                            + viewPager.getCurrentItem());

            if (fragment != null) {
                fragment.updateView(tempMealSet);
            }

        } else {

            Toast.makeText(MainActivity.this, "Käyttäjätunnusta ei ole luotu",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Starts service with the username input.
     *
     * @see GetUser
     *
     * @param username name of the new user
     */
    @Override
    public void onPositiveAddUserAnswer(String username) {

        this.username = username;

        getUser.putExtra("user", username);
        startService(getUser);
    }

    /**
     * Opens DatePickerFragment
     *
     * @param view view of TextView where selected date is shown
     */
    public void setDate(View view) {

        Calendar c = Calendar.getInstance();
        c.set(newYear, newMonth, newDay);

        datepicker = DatePickerFragment.newInstance("Valitse päivä", c);
        datepicker.show(getFragmentManager(), "dateDialog");
    }

    /**
     * Starts service to fetch all user's foods from database.
     */
    private void getFoods() {

        getFoods.putExtra("date", String.valueOf(newDay) +
                String.valueOf(newMonth) +
                String.valueOf(newYear));
        getFoods.putExtra("user", this.username);

        startService(getFoods);
    }
}
