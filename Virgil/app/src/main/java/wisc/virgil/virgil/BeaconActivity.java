package wisc.virgil.virgil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Summer on 3/28/2016.
 */
public class BeaconActivity extends AppCompatActivity {

    // Control
    private Button buttonBeacon;
    VirgilAPI api;
    private DrawerLayout drawerLayout;
    private FrameLayout frame;
    private ArrayList<Integer> beaconList;

    // Beacon Ranging
    private BeaconManager beaconManager;
    private Region region;

    //@param: currMajor, currMinor:
    //  Represent most recently detected Beacon which is still the closest one in range.
    //  Compared with incoming major and minor values to detect a new nearestBeacon when in range.
    //@param: jsonAPIReturn:
    // Testing at Fragment implies varying delay in Fragment json string instantiation
    // depending on signal strength. ( NULL -> JSON but now "-1" -> JSON (in actual
    // implementation not allowing it to be nullable )
    //
    // reset to -1 before each new API call
    // used at Beacon Fragment to stall UI implementation at
    // Fragment until JSON properly instantiated.
    public static String currMajor = "-1";
    public static String currMinor = "-1";
    public String jsonAPIReturn    = "-1";  // DO NOT CHANGE DEFAULT VALUE


    // Beacon Async task (API call)
    private class BeaconsAsyncTask extends AsyncTask<String, String, String> {

        private final String PATH_OF_API = "http://52.24.10.104/Virgil_Backend/index.php/";
        private final String GET_MUSEUM_PATH = "getEntireMuseum/";
        private final String GET_ALL_MUSEUMS_PATH = "getAllMuseums/";
        private final String GET_EVENTS_PATH = "events/getEventsForMuseum/";
        private final String GET_MUSEUM = "getMuseum";
        private final String GET_ALL_MUSEUMS = "getAllMuseums";
        private final String GET_EVENTS = "getEventsForMuseum";
        private final String DEFAULT_VAL = "-1";

        public BeaconsAsyncTask(){
            super();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
        */

        }

        @Override
        // Beacon:
        // params[0] = major
        // params[1] = minor
        protected String doInBackground(String... params) {
            Log.d("START", "start of call");
            String major = params[0];
            String minor = params[1];
            String subPath = "beacons/getContentForBeacon/" + major + "/" + minor;
            String jsonTemp = this.getJSONString(subPath);
            Log.d("END", "end of call");
            return jsonTemp;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("START POST", "starting transfer json String => Beacon Fragment");
            jsonAPIReturn = result;

            // Quick sanity Test:
            // String toasty = "jsonAPIReturn: " + jsonAPIReturn;
            // Toast.makeText(getApplicationContext(), toasty,
            //       Toast.LENGTH_SHORT).show();

            //TODO: pass to fragment

            Log.d("done", "finished transfer ");


            // check needed every time or debugging remains?
            /*
            try {
                JSONObject response = new JSONObject(result);
                JSONArray arrOfBeaconContent = response.getJSONArray("beaconContent");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            */

            //TODO: at frag
            //JSONObject JSONObject = new JSONObject(result);
        }

        // Beacon subpath syntax:
        // beacons/getContentForBeacon/<String major>/<String minor>"
        private String getJSONString(String subPath) {
            String returnString = "";

            try {
                URL url = new URL(PATH_OF_API+subPath);

                // read text returned by server
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    returnString += line;
                }
                in.close();

            }
            catch (MalformedURLException e) {
                System.out.println("Malformed URL: " + e.getMessage());
            }
            catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
            }

            return returnString;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_beacon);
        setSupportActionBar(toolbar);

        frame = (FrameLayout) findViewById(R.id.fgt_beacon);
        frame.setVisibility(View.INVISIBLE);

        beaconList = addDrawable();

        api = (VirgilAPI) getIntent().getSerializableExtra("API");
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_beacon);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_beacon);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        buttonBeacon = (Button) findViewById(R.id.btn_beacon);
        buttonBeacon.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = MotionEventCompat.getActionMasked(motionEvent);
                buttonBeacon.setBackgroundResource(R.drawable.beacon_dark);
                int random = 0;
                Random rand = new Random();

                //if there is content then load in the fragment
                switch (action) {
                    case (MotionEvent.ACTION_UP):

                        random = rand.nextInt(5) + 0;
                        buttonBeacon.setBackgroundResource(beaconList.get(random));

                        frame.setVisibility(View.VISIBLE);
                        populateFragment();
                        return true;
                    default:
                        return true;
                }
            }
        });

        // Beacon Ranging
        beaconManager = new BeaconManager(this);

        // Ranging Listener
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if(!list.isEmpty()){
                    String majorTemp;
                    String minorTemp;

                    Beacon nearestBeacon = list.get(0);
                    majorTemp = Integer.toString(nearestBeacon.getMajor());
                    minorTemp = Integer.toString(nearestBeacon.getMinor());

                    if(currMajor.equals("-1")  && currMinor.equals("-1")) {
                        currMajor = majorTemp;
                        currMinor = minorTemp;
                        jsonAPIReturn = "-1"; //See comment at declaration
                        new BeaconsAsyncTask().execute(majorTemp, minorTemp);
                    } else if(!currMajor.equals(majorTemp) || !currMinor.equals(minorTemp)) {
                        currMajor = majorTemp;
                        currMinor = minorTemp;
                        jsonAPIReturn = "-1"; //See comment at declaration
                        new BeaconsAsyncTask().execute(majorTemp, minorTemp);
                    }

                    // Quick sanity Test:
                    //String toasty = "major: " + majorTemp + " minor: " + minorTemp;
                    //Toast.makeText(getApplicationContext(), toasty,
                    //       Toast.LENGTH_SHORT).show();

                }
            }
        });

        // Beacon Major ID: Museum ID
        // Beacon Minor ID: Exhibit ID -not specified for ranging ( want to pick up
        //                          more than one exhibit at a time).
        region = new Region("Beacon Region",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), 1, null);
    }

    private ArrayList<Integer> addDrawable() {
        ArrayList<Integer> list = new ArrayList<>();

        list.add(R.drawable.pink_beacon);
        list.add(R.drawable.blue_beacon);
        list.add(R.drawable.green_beacon);
        list.add(R.drawable.purple_beacon);
        list.add(R.drawable.yellow_beacon);

        return list;
    }
    private void populateFragment() {
        //creates the first fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fgt_beacon, setupFragment())
                .addToBackStack(null)
                .commit()
        ;
    }
    private void selectItem(int id) {

        if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_home) {
            Toast.makeText(this, getResources().getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_maps) {
            Toast.makeText(this, getResources().getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_beacon) {
            drawerLayout.closeDrawers();
        }

    }

    //Provide back button support
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Provide back button support
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent intent = new Intent(this, MuseumSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.main_beacon);
        item.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        int id = menuItem.getItemId();

                        selectItem(id);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.main_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.main_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            startActivity(intent);
            finish();
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Fragment setupFragment() {
        //TODO: build frag using instances and bundles
        BeaconFragment fragment = (BeaconFragment) BeaconFragment.newInstance(jsonAPIReturn);
        return fragment;
    }

    protected void onResume() {
        super.onResume();

        // Beacon runtime permissions checker
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        // Ranging
        beaconManager.connect(new BeaconManager.ServiceReadyCallback(){
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }
}

