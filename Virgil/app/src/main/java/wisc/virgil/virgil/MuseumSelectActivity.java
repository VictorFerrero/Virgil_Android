package wisc.virgil.virgil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class MuseumSelectActivity extends AppCompatActivity {

    VirgilAPI api;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_select);

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_museum_select);
        setSupportActionBar(myToolbar);

        //Setup API and fetch museum list
        api = new VirgilAPI();
        api.fetchAllMuseums();

        api.fetchMuseum(0);

        showListView();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_select);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_select);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void showListView() {
        ArrayList<Museum> museums = new ArrayList<>();

        MuseumSelectAdapter adapter = new MuseumSelectAdapter(this, museums);

        //Set listview adapter
        ListView listView = (ListView) findViewById(R.id.lv_museum_select);
        listView.setAdapter(adapter);

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumListStatus() != api.FINISHED_STATUS) {
        }

        //Add all list items to adapter
        adapter.addAll(api.getMuseumList());

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
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
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
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
        finish();
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


        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_map) {
            return true;
        } else if (id == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
