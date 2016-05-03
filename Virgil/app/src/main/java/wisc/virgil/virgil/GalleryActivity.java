package wisc.virgil.virgil;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import android.content.Context;
import android.graphics.Bitmap;

/**
 *  Written by   : Munish Kapoor
 *  Original Code:
 *  http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/
 *  MODIFIED BY: Tyler Phelps
 **/
public class GalleryActivity extends AppCompatActivity {

    @Bind(R.id.tb_gallery) Toolbar toolbar;
    @Bind(R.id.tbl_gallery) TabLayout tabs;
    @Bind(R.id.vp_gallery) ViewPager pager;

    MainPagerAdapter adapter;

    VirgilAPI api;
    CharSequence Titles[];
    private DrawerLayout drawerLayout;

    private boolean inDB;
    private int museumId;
    private Context context;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        this.context = this;

        //Setup API, retrieve id of selected museum, and fetch the corresponding gallery
        Intent intent = getIntent();
        api = (VirgilAPI) intent.getSerializableExtra("API");
        this.museumId = api.getMuseum().getId();
        Log.d("Gallery", "ID: " + api.getMuseum().getId());

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_gallery);
        setSupportActionBar(myToolbar);

        //creates an action bar hamburger bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Adds drawer layout
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_gallery);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nv_gallery);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        //sets the database variable
        inDB = api.databaseContains(this.museumId, this);

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
        if(api.getMuseum().getContent().isEmpty() || api.getMuseum().getContent().get(0).getImage(this.context) == null) {
            imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
        } else {
            Bitmap tabImage = null;
            for (Content museumContent : api.getMuseum().getContent()) {
                if (!museumContent.isMap()) {
                    tabImage = museumContent.getImage(getApplicationContext());
                }
            }

            if (tabImage != null) {
                imageView.setImageBitmap(tabImage);
            }
            else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
            }
        }
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

    private void selectItem(int id) {

        if (id == R.id.nav_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_maps) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_favorites) {
            //api.addFavorite(this.museumId, this);
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_search) {
            clearCache();
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_home) {
            Toast.makeText(this, getResources().getString(R.string.not_implemented),
                    Toast.LENGTH_SHORT).show();
        }
    }

    void setUpTabs(){
        adapter =  new MainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length,api);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);

        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager) {
                                          public void onTabSelected(TabLayout.Tab tab) {
                                              super.onTabSelected(tab);

                                              int position = tab.getPosition();
                                              ImageView imageView = (ImageView) findViewById(R.id.iv_gallery);
                                              if(position == 0) {
                                                  //EVENT TAB
                                                  if(api.getMuseum().getContent().isEmpty() || api.getMuseum().getContent().get(0).getImage(getApplication()) == null) {
                                                      imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
                                                  } else {
                                                      Bitmap tabImage = null;
                                                      for (Content museumContent : api.getMuseum().getContent()) {
                                                          if (!museumContent.isMap()) {
                                                              tabImage = museumContent.getImage(getApplicationContext());
                                                          }
                                                      }

                                                      if (tabImage != null) {
                                                          imageView.setImageBitmap(tabImage);
                                                      }
                                                      else {
                                                          imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
                                                      }
                                                  }
                                              } else {
                                                  if (api.getMuseum().getGalleries().isEmpty() ||
                                                          api.getMuseum().getGalleries().get(position - 1).getContent().isEmpty() ||
                                                          api.getMuseum().getGalleries().get(position - 1).getContent().get(0).getImage(getApplication()) == null) {
                                                      imageView.setImageDrawable(ContextCompat.getDrawable(getApplication(), R.drawable.ic_virgil));
                                                  } else {
                                                      imageView.setImageBitmap(api.getMuseum().getGalleries().get(position - 1).getContent().get(0).getImage(getApplication()));
                                                  }
                                                  if(api.getMuseum().getGalleries().isEmpty() || api.getMuseum().getGalleries().get(position - 1).getExhibits().isEmpty()) {
                                                      Toast.makeText(getApplication(), "This gallery is empty!", Toast.LENGTH_SHORT).show();
                                                  }
                                              }
                                          }
                                      }
        );

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
        clearCache();
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
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        MenuItem item = menu.findItem(R.id.action_favorite_item);

        if (inDB) {
            item.setIcon(R.drawable.ic_star_white_48dp);
        } else if (!inDB){
            item.setIcon(R.drawable.ic_star_border_white_48dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.action_beacon) {
            Intent intent = new Intent(this, BeaconActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_map) {
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_favorites) {
            //api.addFavorite(this.museumId, this);
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_search) {
            clearCache();
            Intent intent = new Intent(this, MuseumSelectActivity.class);
            intent.putExtra("API", api);
            startActivity(intent);
            finish();
        } else if (id == R.id.action_favorite_item) {

            if (inDB) {
                item.setIcon(R.drawable.ic_star_border_white_48dp);
                api.deleteFavorite(this.museumId, this);
                Toast.makeText(this, getResources().getString(R.string.unfavorited),
                        Toast.LENGTH_SHORT).show();
                inDB = false;
            } else if (!inDB) {
                try {
                    if (api.addFavorite(this.museumId, this.context)) {
                        Toast.makeText(this, getResources().getString(R.string.added_favorite),
                                Toast.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_star_white_48dp);
                        inDB = true;
                    }
                    else {
                        Toast.makeText(this, "Error fetching data from server.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.d("ADD FAV", "ERROR:");
					e.printStackTrace();
				}

            }
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearCache() {
        ContextWrapper cw = new ContextWrapper(context);
        File dir = cw.getDir("cachedImageDir", Context.MODE_PRIVATE);

        Log.d("Cache", "cache size: " + dir.list().length);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

        Log.d("Cache", "cache size: " + dir.list().length);
        Log.d("Cache", "cleared cached");
    }
}