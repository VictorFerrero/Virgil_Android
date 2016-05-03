package wisc.virgil.virgil;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Summer on 4/29/2016.
 */
public class   BeaconRecycleAdapter extends RecyclerView.Adapter<BeaconRecycleAdapter.ViewHolder> {

    private List<String> itemsData;
    private List<String> itemsTitle;
    private List<Drawable> itemsImage;
    private List<String> itemsHeader;
    private ViewHolder viewHolder;
    private View itemLayoutView;

    public BeaconRecycleAdapter(List<String> itemsTitle, List<String> itemsData,
                                List<Drawable> itemsImage)
    {
        this.itemsData  = itemsData;
        this.itemsTitle = itemsTitle;
        this.itemsImage = itemsImage;
        this.itemsHeader = itemsTitle;
    }

    @Override
    public BeaconRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.exhibit_view, parent, false);
        this.viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if(this.itemsTitle.get(position) == "Exhibit") {
            viewHolder.exhibitTitle.setVisibility(View.GONE);
        } else {
            viewHolder.exhibitTitle.setText(this.itemsTitle.get(position));
        }
        if(this.itemsData.get(position) == "Description") {
            viewHolder.exhibitDescription.setVisibility(View.GONE);
        } else {
            viewHolder.exhibitDescription.setText(this.itemsData.get(position));
        }
        if(this.itemsTitle.get(position) == "Exhibit" && this.itemsData.get(position) == "Description") {
            itemLayoutView.findViewById(R.id.cv_exhibit_text_content).setVisibility(View.GONE);
        }
        if(this.itemsImage.get(position) == null) {
            viewHolder.exhibitImage.setVisibility(View.GONE);
            itemLayoutView.findViewById(R.id.cv_exhibit_image).setVisibility(View.GONE);
        } else {
            viewHolder.exhibitImage.setImageDrawable(this.itemsImage.get(position));
        }
        if(this.itemsHeader.get(position) == "Exhibit") {
            viewHolder.exhibitHeader.setVisibility(View.GONE);
            itemLayoutView.findViewById(R.id.exhibit_header).setVisibility(View.GONE);
        } else {
            viewHolder.exhibitHeader.setText(this.itemsHeader.get(position));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_exhibit_title)
        TextView exhibitTitle;
        @Bind(R.id.tv_exhibit_description) TextView exhibitDescription;
        @Bind(R.id.iv_exhibit_image)
        ImageView exhibitImage;
        @Bind(R.id.exhibit_title)          TextView exhibitHeader;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }
    }

    @Override
    public int getItemCount() {
        return itemsTitle.size();
    }
}
