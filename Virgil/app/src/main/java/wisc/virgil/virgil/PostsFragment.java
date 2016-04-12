package wisc.virgil.virgil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  Written by   : Munish Kapoor
 *  Original Code:
 *  http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/
 **/

public class PostsFragment extends Fragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    VirgilAPI api;
    int position;

    public static Fragment newInstance(Context context) {
        PostsFragment f = new PostsFragment();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api = (VirgilAPI) getArguments().getSerializable("API");
        position = getArguments().getInt("POS");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.gallery_content, null);
        ButterKnife.bind(this, root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonRecycleAdapter adapter = new CommonRecycleAdapter(createTitleList(), createDescList(), createImageList());
        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<Drawable> createImageList() {
        List<Drawable> imageList = new ArrayList<>();
        for(Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            //imageList.add(exhibit.getContent().get(0).getImage());
            imageList.add(ContextCompat.getDrawable(getContext(), R.drawable.bucky_history));
        }
        return imageList;
    }

    private List<String> createTitleList() {
        List<String> titleList = new ArrayList<>();
        for(Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            titleList.add(exhibit.getName());
        }
        return titleList;
    }

    private List<String> createDescList() {
        List<String> descList = new ArrayList<>();
        for(Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
            //descList.add(exhibit.getContent().get(0).getDescription());
            descList.add("I'm a description!");
        }
        return descList;
    }

    private List<String> createItemHeader() {
        List<String> headerList = new ArrayList<>();

        for (int i=0; i<30; i++) {
            headerList.add("Virgil Header : " + i);
        }

        return headerList;
    }
}