package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

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
        Intent intent = getIntent();
        museumId = intent.getIntExtra("ID", 0);
        api = (VirgilAPI) intent.getSerializableExtra("API");

        Log.d("Gallery", "ID: " + this.museumId);
        /* Future code for when favorites api contains content/galleries/exhibits
        if(!api.getFavorites(this).isEmpty()) {
            for(int i = 0; i < api.getFavorites(this).size(); i++) {
                if(api.getFavorites(this).get(i).getMuseumID() == museumId) {
                    //Assign found museum so no fetch necessary (Offline Viewing)
                }
            }
        }
        End of future code */

        setTitle(api.getMuseum().getName());

        //Fill Titles for tabs with gallery names
        List<String> nameList = new ArrayList<>();
        nameList.add("Events");
        for(int i = 0; i < api.getMuseum().getGalleries().size(); i++) {
            nameList.add(api.getMuseum().getGalleries().get(i).getName());
        }

        Titles = nameList.toArray(new CharSequence[nameList.size()]);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpTabs();

        ImageView imageView = (ImageView) findViewById(R.id.iv_gallery);
        if(api.getMuseum().getContent().isEmpty() || api.getMuseum().getContent().get(0).getImage() == null) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
        } else {
            imageView.setImageDrawable(api.getMuseum().getContent().get(0).getImage());
        }

    }

    void setUpTabs(){
        adapter =  new MainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        //setupTabIcons();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageView imageView = (ImageView) findViewById(R.id.iv_gallery);
                if(position == 0) {
                    if(api.getMuseum().getContent().isEmpty() || api.getMuseum().getContent().get(0).getImage() == null) {
                        imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
                    } else {
                        imageView.setImageDrawable(api.getMuseum().getContent().get(0).getImage());
                    }
                } else if(api.getMuseum().getGalleries().isEmpty() || api.getMuseum().getGalleries().get(position - 1).getContent().isEmpty() || api.getMuseum().getGalleries().get(position - 1).getContent().get(0).getImage() == null) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
                } else {
                    imageView.setImageDrawable(api.getMuseum().getGalleries().get(position - 1).getContent().get(0).getImage());
                }

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setCropToPadding(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("API", api);
            intent.putExtra("ID", museumId);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_favorites) {
            //api.addFavorite(this.museumId, this);
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}