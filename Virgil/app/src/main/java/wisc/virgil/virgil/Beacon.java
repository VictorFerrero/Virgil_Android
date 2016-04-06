package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by Summer on 3/28/2016.
 */
public class Beacon extends AppCompatActivity {

    //instance variables
    private Button buttonBeacon;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beacon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_beacon);
        setSupportActionBar(toolbar);

        buttonBeacon = (Button) findViewById(R.id.btn_beacon);
        buttonBeacon.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {

                int action = MotionEventCompat.getActionMasked(motionEvent);
                buttonBeacon.setBackgroundResource(R.drawable.beacon_dark);

                //if there is content then load in the fragment
                switch (action) {
                    case (MotionEvent.ACTION_UP):
                        buttonBeacon.setBackgroundResource(R.drawable.beacon);
                        return true;
                    default:
                        return true;
                }
            }
        });
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id        = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_map) {
            return true;
        } else if (id == R.id.action_favorites) {
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, museumSelectActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

