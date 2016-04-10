package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //Setup API, retrieve id of selected museum, and fetch the corresponding gallery
        api = new VirgilAPI();
        Intent intent = getIntent();
        museumId = intent.getIntExtra("ID", 0);

        /* Future code for when favorites api contains content/galleries/exhibits
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

        //setupTabIcons();
    }

    private void setupTabIcons() {
        //Set each tab's icon
        for(int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(R.drawable.ic_virgil);
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
        }
        return super.onOptionsItemSelected(item);
    }
}