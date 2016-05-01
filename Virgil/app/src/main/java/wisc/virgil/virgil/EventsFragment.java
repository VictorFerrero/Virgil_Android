package wisc.virgil.virgil;

import android.content.Intent;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  Written by   : Munish Kapoor
 *  Original Code:
 *  http://manishkpr.webheavens.com/android-material-design-tabs-collapsible-example/
 **/

public class EventsFragment extends Fragment {

    @Bind(R.id.recyclerView) RecyclerView recyclerView;

    VirgilAPI api;
    int position;

    public static Fragment newInstance(Context context) {
        EventsFragment f = new EventsFragment();
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        api = (VirgilAPI) getArguments().getSerializable("API");
        position = getArguments().getInt("POS");

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.gallery_content, null);
        ButterKnife.bind(this, root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        EventRecycleAdapter adapter = new EventRecycleAdapter(createTitleList(), createDescList(),
                createImageList(), createHourList(), createLocationList(), createDateList());
        recyclerView.setAdapter(adapter);
        return root;
    }

    private List<String> createTitleList() {
        List<String> titleList = new ArrayList<>();
        for (Event event : api.getEventList()) {
            titleList.add("Event");
        }
        return titleList;
    }

    private List<String> createDescList() {
        List<String> descList = new ArrayList<>();

        for (Event event : api.getEventList()) {
            if(event.getDescription() == null) {
                descList.add("Description");
            } else {
                descList.add(event.getDescription());
            }

        }
        return descList;
    }

    private List<Drawable> createImageList() {
        List<Drawable> imageList = new ArrayList<>();
        for (Event event : api.getEventList()) {
            if(event.getEventContent().isEmpty() || event.getEventContent().get(0).getImage(getContext()) == null) {
                imageList.add(ContextCompat.getDrawable(getContext(), R.mipmap.virgil_white_ic));
            } else {
                imageList.add(new BitmapDrawable(getResources(), event.getEventContent().get(0).getImage(getContext())));
            }
        }
        return imageList;
    }

    private List<String> createHourList() {
        List<String> hourList = new ArrayList<>();
        for (Event event : api.getEventList()) {
            String startAMPM = "AM";
            String endAMPM = "AM";
            if(event.getStartHour() > 12) {
                startAMPM = "PM";
            }
            if(event.getEndHour() > 12) {
                endAMPM = "PM";
            }
            if(event.getStartHour() == 0 && event.getEndHour() == 0) {
                hourList.add("Hours");
            } else if(event.getEndHour() == 0) {
                hourList.add("Opens at " + event.getStartHour() + ":" + event.getStartMin() + startAMPM);
            } else {
                hourList.add("From " + event.getStartHour() + ":" + event.getStartMin() + startAMPM + " to " + event.getEndHour() + ":" + event.getEndMin() + endAMPM);
            }
        }
        return hourList;
    }

    private List<String> createLocationList() {
        List<String> locationList = new ArrayList<>();
        for (Event event : api.eventList) {
            locationList.add("Location");
        }
        return locationList;
    }

    private List<String> createDateList() {
        List<String> dateList = new ArrayList<>();
        for (Event event : api.getEventList()) {
            String string;
            String startMonth = new DateFormatSymbols().getMonths()[event.getStartMonth()-1];
            String endMonth = new DateFormatSymbols().getMonths()[event.getEndMonth()-1];
            if(event.getStartYear() != 0) {
                string = startMonth + " " + event.getStartDay() + ", " + event.getStartYear() + " to "
                        + endMonth + " " + event.getEndDay() + ", " + event.getEndYear();
            } else if(event.getStartMonth() != 0) {
                string = startMonth + " " + event.getStartDay() + " to "
                        + endMonth + " " + event.getStartDay();
            } else if(event.getStartDay() != 0){
                string = "The " + event.getStartDay() + " to " + event.getEndDay() + " of this month";
            } else {
                string = "Date";
            }
            dateList.add(string);
        }
        return dateList;
    }
}