package wisc.virgil.virgil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;

import java.util.ArrayList;

public class museumSelectActivity extends AppCompatActivity {

    VirgilAPI api;

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

        showListView();
    }

    private void showListView() {
        ArrayList<Museum> museums = new ArrayList<Museum>();

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

    public void switchToGallery(int position) {

        //EXTRA FETCH FOR TESTING PURPOSES
        Log.d("API", "Position: " + position);
        api.fetchMuseum(api.getMuseumList().get(position).getId());

        while(api.museumStatus() != api.FINISHED_STATUS) {
            if (api.museumStatus() == api.ERROR_STATUS) break;
        }

        Log.d("API", "Museum Selected: " + api.getMuseum().getName());
        //END OF EXTRA FETCH AND TEST

        //Get the selected museum's id and pass it to the new starting gallery view
        int id = api.getMuseumList().get(position).getId();
        Intent intent = new Intent(this, MuseumGallery.class);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
