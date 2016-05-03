package wisc.virgil.virgil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by Summer on 3/28/2016.
 */
public class BeaconActivity extends AppCompatActivity {

    private Button buttonBeacon;
    VirgilAPI api;
    private DrawerLayout drawerLayout;
    private FrameLayout frame;
    private ArrayList<Integer> beaconList;
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
        BeaconFragment beaconContent = new BeaconFragment();

        return beaconContent;
    }

    protected void onResume() {
        super.onResume();

        // Beacon runtime permissions checker
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

}

