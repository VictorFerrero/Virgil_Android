package wisc.virgil.virgil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONArray;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Summer on 4/29/2016.
 */

//**** JSON Cheat Sheet ****//
// => Example ( one piece of content )
//    {
//        "beaconContent":
//        [
//        {
//                "id":"2",
//                "major":"1",
//                "minor":"2",
//                "title":"this is a title for content for a beacon",
//                "description":"this is a beacon description for content. it is the 2nd beacon being added",
//                "pathToContent":"1\/vintageFootball.jpg",
//                "beaconContentProfileJSON":"{}"
//        }
//        ],"success":true}


// => Example (Two pieces of content)
//    {
//        "beaconContent":
//        [
//        {
//            "id":"3",
//            "major":"1",
//            "minor":"3","title":"this is a title for content for a beacon",
//            "description":"and the 3rd beacon to make it to the database is ......",
//            "pathToContent":"1\/sharperTo.jpg",
//            "beaconContentProfileJSON":"{}"
//        },
//        {
//            "id":"7","major":"1","minor":"3",
//            "title":"Just a second piece of content",
//            "description":"just adding some more content to this beacon",
//            "pathToContent":"no Image",
//            "beaconContentProfileJSON":"{}"
//        }
//        ],"success":true}

public class BeaconFragment extends Fragment {
                @Bind(R.id.recyclerView) RecyclerView recyclerView;

    //VirgilAPI api;
    //int position;
    private String jsonAPIReturn;
    private JSONObject jsonObject;
    private JSONArray jsonArray;



    public static Fragment newInstance(@NonNull final String jsonAPIReturn) {
        final BeaconFragment fragment = new BeaconFragment();
        final Bundle args = new Bundle();

        args.putString("jsonAPIReturn", jsonAPIReturn);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: make sure dont need to do this later then delete
        //api = (VirgilAPI) getArguments().getSerializable("API");
        //position = getArguments().getInt("POS");

        Bundle args = getArguments();
        if(args != null ) {


            while(jsonAPIReturn.equals("-1")) {
                // Stalling until default value ("-1") -> JSON string
                // see @param: jsonAPIReturn
            }
            //TODO: populate lists with JSON array

            try{
                jsonObject = new JSONObject(jsonAPIReturn);
                jsonArray = jsonObject.getJSONArray("beaconContent");
            } catch(org.json.JSONException e){
                System.err.println(e.getMessage());
            }
        }

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.gallery_content, null);
        ButterKnife.bind(this, root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        BeaconRecycleAdapter adapter = new BeaconRecycleAdapter(createTitleList(), createDescList(), createImageList());
        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<Drawable> createImageList() {
        List<Drawable> imageList = new ArrayList<>();

        /*for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            for(int i = 0; i < exhibit.getContent().size(); i++) {
                if(exhibit.getContent().isEmpty() || exhibit.getContent().get(i).getImage(getContext()) == null) {
                    imageList.add(ContextCompat.getDrawable(getContext(), R.mipmap.virgil_white_ic));
                } else {
                    imageList.add(new BitmapDrawable(getResources(), exhibit.getContent().get(i).getImage(getContext())));
                }
            }
        }*/

        for (int i = 0; i < 30; i++) {
            imageList.add(ContextCompat.getDrawable(getContext(), R.mipmap.virgil_white_ic));
        }
        return imageList;
    }

    private List<String> createTitleList() {
        List<String> titles = new ArrayList<String>();
        JSONObject json;
        String title;

        for(int i = 0; i < jsonArray.length(); i++){
            try{
                json = jsonArray.getJSONObject(i);
                //TODO: remove "title1: " after testing
                title = "title" + i + ": " + json.getString("title");
                titles.add(title);
            } catch(org.json.JSONException e){
                System.err.println(e.getMessage());
            }
        }
        return titles;
    }

    private List<String> createDescList() {
        List<String> descriptions = new ArrayList<>();
        JSONObject json;
        String description;

        for(int i = 0; i < jsonArray.length(); i++){
            try{
                json = jsonArray.getJSONObject(i);
                //TODO: remove "description1: " after testing
                description = "title" + i + ": " + json.getString("description");
                descriptions.add(description);
            } catch(org.json.JSONException e){
                System.err.println(e.getMessage());
            }
        }
        return descriptions;
    }
}

