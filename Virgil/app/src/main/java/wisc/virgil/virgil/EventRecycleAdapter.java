package wisc.virgil.virgil;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Munish on 11/5/15.
 */

public class EventRecycleAdapter extends RecyclerView.Adapter<EventRecycleAdapter.ViewHolder> {

    private List<String> eventsDesc;
    private List<String> eventsTitle;
    private List<Drawable> eventsImage;
    private List<String> eventsHeader;
    private ViewHolder viewHolder;
    private List<String> eventsHours;
    private List<String> eventsLocation;
    private List<String> eventsDate;

    public EventRecycleAdapter(List<String> eventsTitle, List<String> eventsDesc,
                               List<Drawable> eventsImage, List<String> eventsHours,
                               List<String> eventsLocation, List<String> eventsDate) {

        this.eventsDesc  = eventsDesc;
        this.eventsTitle = eventsTitle;
        this.eventsImage = eventsImage;
        this.eventsDate = eventsDate;
        this.eventsHours = eventsHours;
        this.eventsLocation = eventsLocation;
        //this.eventsHeader   = eventsHeader;
    }

    @Override
    public EventRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.event_content, parent, false);

        this.viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        if(this.eventsTitle.get(position) == "Event") {
            viewHolder.eventTitle.setVisibility(View.GONE);
        } else {
            viewHolder.eventTitle.setText(this.eventsTitle.get(position));
        }
        if(this.eventsDesc.get(position) == "Description") {
            viewHolder.eventDescription.setVisibility(View.GONE);
        } else {
            viewHolder.eventDescription.setText(this.eventsDesc.get(position));
        }
        if(this.eventsImage.get(position) == null) {
            viewHolder.eventImage.setVisibility(View.GONE);
        } else {
            viewHolder.eventImage.setImageDrawable(this.eventsImage.get(position));
        }
        //viewHolder.eventHeader.setText(this.eventsHeader.get(position));
        if(this.eventsDate.get(position) == "Date") {
            viewHolder.eventDate.setVisibility(View.GONE);
        } else {
            viewHolder.eventDate.setText(this.eventsDate.get(position));
        }
        if(this.eventsHours.get(position) == "Hours") {
            viewHolder.eventHour.setVisibility(View.GONE);
        } else {
            viewHolder.eventHour.setText(this.eventsHours.get(position));
        }
        if(this.eventsLocation.get(position) == "Location") {
            viewHolder.eventLocation.setVisibility(View.GONE);
        } else {
            viewHolder.eventLocation.setText(this.eventsLocation.get(position));
        }
        viewHolder.addEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addEvent(v, viewHolder);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_event_location)       TextView eventLocation;
        @Bind(R.id.tv_event_description) TextView eventDescription;
        @Bind(R.id.iv_event_image)       ImageView eventImage;
        @Bind(R.id.tv_event_title)          TextView eventTitle;
        @Bind(R.id.tv_event_date)          TextView eventDate;
        @Bind(R.id.tv_event_hours)         TextView eventHour;
        @Bind(R.id.btnAddEvent)             Button addEvent;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }
    }

    @Override
    public int getItemCount() {
        return eventsTitle.size();
    }

    private void addEvent(View v, ViewHolder viewHolder) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        //intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, viewHolder.eventTitle.getText());
        //intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, viewHolder.eventTitle.getText());
        intent.putExtra(CalendarContract.Events.TITLE, viewHolder.eventTitle.getText());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, viewHolder.eventDescription.getText());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, viewHolder.eventLocation.getText());
        v.getContext().startActivity(intent);
    }
}