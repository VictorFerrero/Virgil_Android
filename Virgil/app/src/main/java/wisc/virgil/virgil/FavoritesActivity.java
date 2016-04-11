package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

        api = new VirgilAPI();

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

        //*Temporary* Clear database so we don't keep creating more of the same museums
        while(!api.getFavorites(this).isEmpty()) {
            api.deleteFavorite(api.getFavorites(this).get(0).getMuseumID(), this);
        }

        api.addFavorite(1, this);
        api.addFavorite(2, this);
        if(api.getFavorites(this) != null && !api.getFavorites(this).isEmpty()) {
            gridView.setAdapter(new FavoritesAdapter(this, api.getFavorites(this)));
        } else {
            //Will display error message
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchToGallery(position);
            }
        });
    }

    public void switchToGallery(int position) {
        api.getFavorites(this).get(position).getMuseumID();
        Intent intent = new Intent(this, GalleryActivity.class);
        intent.putExtra("ID", api.getFavorites(this).get(position).getMuseumID());
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
        api.deleteFavorite(1, this);
        api.deleteFavorite(2, this);
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
            startActivity(intent);
            finish();
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }  else if (id == R.id.main_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
