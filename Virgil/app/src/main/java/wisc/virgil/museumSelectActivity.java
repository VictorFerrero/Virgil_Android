package wisc.virgil;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MuseumSelectActivity extends AppCompatActivity {

    //TODO: Pass one or minimal api(s) between activities to minimize refetching
    VirgilAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_select);

        api = new VirgilAPI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Fetch/repopulate list whenever user goes to the museum list view
        api.fetchAllMuseums();
        //Update and show the list
        showMuseumList();
    }

    private void showMuseumList() {

        ArrayList<Museum> museums;

        //TODO: Will softlock if museum list is stuck pending!!
        while(api.museumListStatus() == api.PENDING_STATUS) {
            ;;
        }
        museums = api.getMuseumList();
        //Check for name status of each museum in list i.e. ERROR_STATUS
        //Without fetching each museum separately
        //One fails, all fail
        for(Museum museum : museums) {
            //If an error is found, present dialog and return to prev screen or splash
            if(museum.getName().isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("List Error")
                        .setMessage("Failed to retrieve the museum list!")
                        .setNeutralButton("Return",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //TODO: Go to previous screen or splash screen
                                    }
                                })
                        .show();
            }
        }

        MuseumListAdapter adapter = new MuseumListAdapter(this, R.layout.museum_list_item, museums);

        ListView museumList = this.findViewById(R.id.lv_museum_select);
        museumList.setAdapter(adapter);
        museumList.setOnItemClickListener(new OnItemClickListenerMuseumList());
    }

}
