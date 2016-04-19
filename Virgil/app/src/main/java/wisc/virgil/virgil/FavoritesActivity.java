package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Ty Talafous on 4/3/2016.
 */
public class FavoritesActivity extends AppCompatActivity {

    VirgilAPI api;
    List<FavoriteMuseum> favs;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_favorites);
        setTitle("Favorites");

        Intent intent = getIntent();
        api = (VirgilAPI) intent.getSerializableExtra("API");

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_favorites);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_favorites);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_favorites);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        GridView gridView = (GridView) findViewById(R.id.gv_favorites);

        for (FavoriteMuseum favMus : api.getFavorites(this)) {
            Log.d("FAV", favMus.getName() + " " + favMus.getMuseumID());
        }

        if(api.getFavorites(this) != null && !api.getFavorites(this).isEmpty()) {
            gridView.setAdapter(new FavoritesAdapter(this, api.getFavorites(this)));
        } else {
            //Will display error message
        }

        Log.d("API", "Favorites: "+api.getFavorites(this).size());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchToGallery(position);
            }
        });
    }

    public void switchToGallery(int position) {
        Log.d("API", "" + api.getFavorites(this).get(position).getMuseumID());
       api.fetchMuseum(api.getFavorites(this).get(position).getMuseumID());

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) {
                Log.d("API", "Fetched museum with ERROR_STATUS");
                break;
            }
        }

        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("API", api);
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

    //*Temporary* Clear database so we don't keep creating more of the same museums
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

        MenuItem item = menu.findItem(R.id.main_favorites);
        item.setVisible(false);
        this.invalidateOptionsMenu();
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
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_search) {

        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }  else if (id == R.id.main_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
