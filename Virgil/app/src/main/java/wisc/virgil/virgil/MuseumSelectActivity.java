package wisc.virgil.virgil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MuseumSelectActivity extends AppCompatActivity {

    VirgilAPI api;
    private DrawerLayout drawerLayout;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_select);

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_museum_select);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Setup API and fetch museum list
        api = new VirgilAPI();
        api.fetchAllMuseums();

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumListStatus() != api.FINISHED_STATUS) {}

        showListView();

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_select);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_select);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void selectItem(int id) {
        if (id == R.id.nav_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_maps) {
            Toast.makeText(this, getResources().getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_home) {
            Toast.makeText(this, getResources().getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_search) {
            drawerLayout.closeDrawers();
        }
    }

    private void showListView() {
        ArrayList<Museum> museums = new ArrayList<>();

        MuseumSelectAdapter adapter = new MuseumSelectAdapter(this, museums);

        //Set listview adapter
        ListView listView = (ListView) findViewById(R.id.lv_museum_select);
        listView.setAdapter(adapter);

        //Add all list items to adapter
        Log.d("Museum List Size: ", ""+api.getMuseumList().size());
        ArrayList<Museum> museumList = api.getMuseumList();
        Log.d("Museum List Size: ", ""+api.getMuseumList().size());
        adapter.addAll(museumList);

        adapter.setNotifyOnChange(true);

        api.fetchAllMuseums();

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumListStatus() != api.FINISHED_STATUS) {}

        //Make museums clickable and switch to their respective galleries
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switchToGallery(position);
                }
            }
        );
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        int id = menuItem.getItemId();
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

                        selectItem(id);

                        return true;
                    }
                });
    }

    public void switchToGallery(int position) {

        //Get the selected museum's id...
        int id = api.getMuseumList().get(position).getId();

        Log.d("API", "Position: " + position);
        Log.d("API", "Museum Selected: " + api.getMuseumList().get(position).getName());

        //...and pass it to the new starting gallery view

        api.fetchMuseum(api.getMuseumList().get(position).getId());
        boolean successful = true;

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) {
                Log.d("API", "Fetched museum with ERROR_STATUS");
                successful = false;
                break;
            }
        }

        api.fetchEvents(api.getMuseum().getId());
        while(api.eventListStatus() != api.FINISHED_STATUS) {
            if (api.museumListStatus() == api.ERROR_STATUS) {
                Log.d("API", "Fetched events with ERROR_STATUS");
                successful = false;
                break;
            }
        }

        if (successful) {
            Log.d("API", "OK");
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra("API", api);
            intent.putExtra("ID", id);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Could not get the selected museum.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.main_search);
        item.setVisible(true);
        item.setShowAsAction(1);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
