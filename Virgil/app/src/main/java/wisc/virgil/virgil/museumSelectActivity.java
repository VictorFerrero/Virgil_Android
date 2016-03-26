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

import java.util.ArrayList;

public class museumSelectActivity extends AppCompatActivity {

    //VirgilAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_select);

        //inflates toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.tb_museum_select);
        setSupportActionBar(myToolbar);

        //api = new VirgilAPI();

        //api.fetchAllMuseums();

        showListView();
    }

    private void showListView() {
        ArrayList<Museum> museums = new ArrayList<Museum>();

        MuseumSelectAdapter adapter = new MuseumSelectAdapter(this, museums);

        ListView listView = (ListView) findViewById(R.id.lv_museum_select);
        listView.setAdapter(adapter);

        //while(api.museumStatus() != api.FINISHED_STATUS) {
        //    ;;
        //}

        //Add fake museums to arraylist
        Museum museum1 = new Museum(0, "Nope", "Yep");
        Museum museum2 = new Museum(1, "Nope", "Nope");
        Museum museum3 = new Museum(2, "Yep", "Yep");
        Museum museum4 = new Museum(3, "Yep", "Nope");
        adapter.add(museum1);
        adapter.add(museum2);
        adapter.add(museum3);
        adapter.add(museum4);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                switchToGallery(position);
                                            }
                                        }
        );

        //adapter.addAll(api.getMuseumList());
    }

    public void switchToGallery(int position) {
        Intent intent = new Intent(this, MuseumGallery.class);
        intent.putExtra("POSITION", position);
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
