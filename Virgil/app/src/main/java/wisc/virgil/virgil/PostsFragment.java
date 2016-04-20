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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import android.graphics.drawable.BitmapDrawable;

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
        if(api.getMuseum().getGalleries().isEmpty() || api.getMuseum().getGalleries().get(position).getExhibits().isEmpty()) {
            Toast.makeText(getActivity(), "This gallery is empty!", Toast.LENGTH_LONG).show();
        } else {
            for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
                if(exhibit.getContent().isEmpty() || exhibit.getContent().get(0).getImage(getContext()) == null) {
                    imageList.add(ContextCompat.getDrawable(getContext(), R.drawable.bucky_history));
                } else {
                    imageList.add(new BitmapDrawable(getResources(), exhibit.getContent().get(0).getImage(getContext())));
                }
            }
        }
        return imageList;
    }

    private List<String> createTitleList() {
        List<String> titleList = new ArrayList<>();
        if(api.getMuseum().getGalleries().isEmpty() || api.getMuseum().getGalleries().get(position).getExhibits().isEmpty()) {
            Toast.makeText(getActivity(), "This gallery is empty!", Toast.LENGTH_LONG).show();
        } else {
            for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
                if(exhibit.getName() == null) {
                    titleList.add("Exhibit");
                } else {
                    titleList.add(exhibit.getName());
                }
            }
        }
        return titleList;
    }

    private List<String> createDescList() {
        List<String> descList = new ArrayList<>();
        if(api.getMuseum().getGalleries().isEmpty() || api.getMuseum().getGalleries().get(position).getExhibits().isEmpty()) {
            Toast.makeText(getActivity(), "This gallery is empty!", Toast.LENGTH_LONG).show();
        } else {
            for (Exhibit exhibit : api.getMuseum().getGalleries().get(position).getExhibits()) {
                if(exhibit.getContent().isEmpty() || exhibit.getContent().get(0).getDescription() == null) {
                    descList.add("Description");
                } else {
                    descList.add(exhibit.getContent().get(0).getDescription());
                }
            }
        }
        return descList;
    }
}