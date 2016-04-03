package wisc.virgil.virgil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Ty Talafous on 3/28/2016.
 */
public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        //inflates toolbar
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_museum_select);
        //setSupportActionBar(myToolbar);
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
