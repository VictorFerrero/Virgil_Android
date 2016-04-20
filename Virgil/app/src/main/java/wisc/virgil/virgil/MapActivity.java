package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created by Ty Talafous on 3/28/2016.
 */
public class MapActivity extends AppCompatActivity {

    VirgilAPI api;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        Intent intent = getIntent();
        api = (VirgilAPI) intent.getSerializableExtra("API");

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_museum_map);
        setSupportActionBar(myToolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_map);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_map);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) break;
        }

        setTitle(api.getMuseum().getName() + " Map");

        ImageView map = (ImageView) findViewById(R.id.map_item);
        map.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.map_placeholder));
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
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("API", api);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


         if (id == R.id.main_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.main_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.main_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
