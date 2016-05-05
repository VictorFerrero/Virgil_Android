package wisc.virgil.virgil;

import android.content.Context;
import android.graphics.Bitmap;
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

    private String json;
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

        Bundle args = getArguments();
        if(args != null ) {
            json = args.getString("jsonAPIReturn");
            // Quick sanity Test:
            // String toasty = "json @ frag: " + json;
            // Toast.makeText(getActivity(), toasty,
            //       Toast.LENGTH_SHORT).show();

            try{
                jsonObject = new JSONObject(json);
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
        List<Drawable> images = new ArrayList<>();
        JSONObject json;
        Content content;

        int contentId;
        int galleryId = 0;   // shouldn't matter for what were trying to do here
        int minorId;         // minor id = exhibit id
        int majorId;         // major id = museum id
        String description;  //TODO: if supposed to show fix needed here
        String pathToContent;
        boolean isMap = false;


        for(int i = 0; i < jsonArray.length(); i++){
            try{
                json = jsonArray.getJSONObject(i);
                contentId = Integer.parseInt(json.getString("id"));
                minorId = Integer.parseInt(json.getString("minor"));
                majorId = Integer.parseInt(json.getString("major"));
                description = json.getString("description");
                pathToContent = json.getString("pathToContent");

                content = new Content(contentId, galleryId, minorId, majorId, description, pathToContent, isMap);
                Bitmap bitmap = content.getImage(getActivity());
                BitmapDrawable drawable = new BitmapDrawable(getResources(),bitmap);
                images.add(drawable);

            } catch(org.json.JSONException e){
                System.err.println(e.getMessage());
            }
        }
        return images;
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
            } catch(org.json.JSONException e) {
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

