package wisc.virgil;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ty Talafous on 3/22/2016.
 */
public class MuseumListAdapter extends ArrayAdapter<Museum> {

    Context mContext;
    int layoutResourceId;
    ArrayList<Museum> data = null;
    public MuseumListAdapter(Context mContext, int layoutResourceId, ArrayList<Museum> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position % 2 == 0) {
            this.layoutResourceId = R.layout.museum_list_item;
        } else {
            this.layoutResourceId = R.layout.museum_list_item_dark;
        }

        if(convertView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        Museum museum = data.get(position);

        //Alternates the color of each list item
        //TODO: Slow as it requires redrawing every other list item
        /*
        if(position % 2 == 0) {
            convertView.setBackgroundColor(
                    convertView.getResources().getColor(R.color.colorPrimaryDark));
            convertView.invalidate();
        }
        */

        //Alternative that changes resourceId every time
        if(position % 2 == 0) {
            TextView museumName = (TextView) convertView.findViewById(R.id.tv_museumName);
            museumName.setText(museum.getName());
            TextView museumAddress = (TextView) convertView.findViewById(R.id.tv_museumAddress);
            museumAddress.setText(museum.getAddress());
            //TODO: Add hours field
            //TextView museumHours = (TextView) convertView.findViewById(R.id.tv_museumListHours);
            //museumHours.setText(museum.getAddress());
            //TODO: Add museum list images
            //ImageView museumImage = (ImageView) convertView.findViewById(R.id.iv_museumListImage);
            //museumImage.setImageDrawable();
        } else {
            TextView museumName = (TextView) convertView.findViewById(R.id.tv_museumNameDark);
            museumName.setText(museum.getName());
            TextView museumAddress = (TextView) convertView.findViewById(R.id.tv_museumAddressDark);
            museumAddress.setText(museum.getAddress());
            //TODO: Add hours field
            //TextView museumHours = (TextView) convertView.findViewById(R.id.tv_museumListHoursDark);
            //museumHours.setText(museum.getAddress());
            //TODO: Add museum list images
            //ImageView museumImage = (ImageView) convertView.findViewById(R.id.iv_museumListImageDark);
            //museumImage.setImageDrawable();
        }

        /*
        TextView museumName = (TextView) convertView.findViewById(R.id.tv_museumName);
        museumName.setText(museum.getName());
        TextView museumAddress = (TextView) convertView.findViewById(R.id.tv_museumAddress);
        museumAddress.setText(museum.getAddress());
        //TODO: Add hours field
        //TextView museumHours = (TextView) convertView.findViewById(R.id.tv_museumListHours);
        //museumHours.setText(museum.getAddress());
        //TODO: Add museum list images
        //ImageView museumImage = (ImageView) convertView.findViewById(R.id.iv_museumListImage);
        //museumImage.setImageDrawable();
        */

        return convertView;
    }
}
