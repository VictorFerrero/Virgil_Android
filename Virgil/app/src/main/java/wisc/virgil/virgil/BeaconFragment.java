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

import com.estimote.sdk.repackaged.gson_v2_3_1.com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Summer on 4/29/2016.
 */
public class BeaconFragment extends Fragment {
                @Bind(R.id.recyclerView) RecyclerView recyclerView;

    //VirgilAPI api;
    //int position;
    private String jsonAPIReturn;
    private JSONObject jsonObject;
    private JsonParser jsonParser;



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

            // Stalling until deafult value ("-1") -> JSON string
            // see @param: jsonAPIReturn
            while(jsonAPIReturn.equals("-1")) {
                //STALL
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
        List<String> titleList = new ArrayList<>();

        /**
        for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            for(int i = 0; i < exhibit.getContent().size(); i++) {
                if(exhibit.getName() == null) {
                    titleList.add("Exhibit");
                } else if (i == 0) {
                    titleList.add(exhibit.getName());
                } else {
                    titleList.add("Exhibit");
                }
            }
        }**/

        for (int i = 0; i < 30; i++) {
            titleList.add("Title " + i);
        }
        return titleList;
    }

    private List<String> createDescList() {
        List<String> descList = new ArrayList<>();

        /**
        for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            for(int i = 0; i < exhibit.getContent().size(); i++) {
                if(exhibit.getContent().isEmpty() || exhibit.getContent().get(i).getDescription() == null) {
                    descList.add("Description");
                } else {
                    descList.add(exhibit.getContent().get(i).getDescription());
                }
            }
        }**/

        for (int i = 0; i < 30; i++) {

            descList.add("Description" + i);
        }
        return descList;
    }
}

