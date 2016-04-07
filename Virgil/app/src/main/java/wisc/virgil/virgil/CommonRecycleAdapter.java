package wisc.virgil.virgil;

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
 * Created by Munish on 11/5/15.
 */

public class CommonRecycleAdapter extends RecyclerView.Adapter<CommonRecycleAdapter.ViewHolder> {

    private List<String> itemsData;
    private List<String> itemsTitle;

    public CommonRecycleAdapter(List<String> itemsData, List<String> itemsTitle) {
        this.itemsData  = itemsData;
        this.itemsTitle = itemsTitle;
    }

    @Override
    public CommonRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.exhibit_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.exhibitTitle.setText(itemsData.get(position));
        viewHolder.exhibitDescription.setText(itemsTitle.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_exhibit_title)       TextView exhibitTitle;
        @Bind(R.id.tv_exhibit_description) TextView exhibitDescription;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            ButterKnife.bind(this, itemLayoutView);
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}