package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Summer on 3/21/2016.
 */
public class MuseumGallery extends AppCompatActivity {

    @Bind(R.id.tb_gallery) Toolbar toolbar;
    @Bind(R.id.tbl_gallery) TabLayout tabs;
    @Bind(R.id.vp_gallery) ViewPager pager;

    MainPagerAdapter adapter;
    CharSequence Titles[];
    int museumId;

    VirgilAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        //Setup API, retrieve id of selected museum, and fetch the corresponding gallery
        api = new VirgilAPI();
        Intent intent = getIntent();
        museumId = intent.getIntExtra("ID", 0);
        api.fetchMuseum(museumId);

        //Wait for fetch to finish (WILL STALL IF FETCH NEVER FINISHES)
        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) break;
        }

        //Fill Titles for tabs with gallery names
        List<String> nameList = new ArrayList<String>();
        for(int i = 0; i < api.getMuseum().getGalleries().size(); i++) {
            nameList.add(api.getMuseum().getGalleries().get(i).getName());
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

        setupTabIcons();
    }

    private void setupTabIcons() {
        //Set each tab's icon
        for(int i = 0; i < api.getMuseum().getGalleries().size(); i++) {
            tabs.getTabAt(i).setIcon(R.drawable.ic_virgil);
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}