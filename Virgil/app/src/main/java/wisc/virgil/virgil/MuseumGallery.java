package wisc.virgil.virgil;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
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
    CharSequence Titles[] = {"TAB 1","TAB 2", "TAB 3", "TAB 4", "TAB 5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpTabs();
    }

    void setUpTabs(){
        adapter =  new MainPagerAdapter(this.getSupportFragmentManager(),Titles,Titles.length);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        tabs.getTabAt(0).setIcon(R.drawable.ic_virgil);
        tabs.getTabAt(1).setIcon(R.drawable.ic_virgil);
        tabs.getTabAt(2).setIcon(R.drawable.ic_virgil);
        tabs.getTabAt(3).setIcon(R.drawable.ic_virgil);
        tabs.getTabAt(4).setIcon(R.drawable.ic_virgil);
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