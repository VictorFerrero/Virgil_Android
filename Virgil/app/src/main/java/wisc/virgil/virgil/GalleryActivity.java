package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  Written by   : Munish Kapoor
 *  Original Code:
 *  http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/
 **/
public class GalleryActivity extends AppCompatActivity {

    @Bind(R.id.tb_gallery) Toolbar toolbar;
    @Bind(R.id.tbl_gallery) TabLayout tabs;
    @Bind(R.id.vp_gallery) ViewPager pager;

    MainPagerAdapter adapter;
    int museumId;

    VirgilAPI api;
    CharSequence Titles[];
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //Setup API, retrieve id of selected museum, and fetch the corresponding gallery
        api = new VirgilAPI();
        Intent intent = getIntent();
        museumId = intent.getIntExtra("ID", 0);

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_gallery);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.dl_gallery);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_gallery);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        /* Future code for when api contains content/galleries/exhibits (crap search though)
        if(!api.getFavorites(this).isEmpty()) {
            for(int i = 0; i < api.getFavorites(this).size(); i++) {
                if(api.getFavorites(this).get(i).getMuseumID() == museumId) {
                    //Assign found museum so no fetch necessary (Offline Viewing)
                }
            }
        }
        End of future code */

        api.fetchMuseum(museumId);

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) break;
        }
        setTitle(api.getMuseum().getName());

        //Fill Titles for tabs with gallery names
        List<String> nameList = new ArrayList<>();
        int count = 0;
        nameList.add("Description");
        while(count < api.getMuseum().getGalleries().size()) {
            nameList.add(api.getMuseum().getGalleries().get(count).getName());
            count++;
        }

        //Extra galleries to show scrollable tabs
        while(count < 8) {
            nameList.add("Gallery " + count);
            count++;
        }

        Titles = nameList.toArray(new CharSequence[nameList.size()]);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpTabs();
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


    void setUpTabs(){
        adapter =  new MainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        //TODO: Unsure how to edit the lists within each tab

        /*
        //Create list of strings to fill tabs with
        ArrayList<String> exhibitList;
        for(int i = 0; i < api.getMuseum().getGalleries().size(); i++) {
            exhibitList = new ArrayList<String>();
            for(int j = 0; j < api.getMuseum().getGalleries().get(i).getExhibits().size(); j++) {
                exhibitList.add(api.getMuseum().getGalleries().get(i).getExhibits().get(j).getName());
            }
            adapter.getItem(i).setUPList(exhibitList);
        }
        */

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
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("ID", museumId);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_favorite_item) {
            Toast.makeText(this, getResources().getString(R.string.added_favorite),
                    Toast.LENGTH_SHORT).show();
            return true;

        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}