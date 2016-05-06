package wisc.virgil.virgil;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ty Talafous on 3/26/2016.
 */
public class MuseumSelectAdapter extends ArrayAdapter<Museum> {

    public MuseumSelectAdapter(Context context, ArrayList<Museum> museums) {
        super(context, R.layout.museum_list_item, museums);
    }

    private static class ViewHolder {
        TextView name;
        TextView address;
        ImageView image;
        TextView hours;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Museum museum = getItem(position);

        //Create view holder for efficiency to cut down on inflating if reusing same layout
        //DOES NOT ALTERNATE COLORS IN LISTVIEW YET
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.museum_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_museumName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.tv_museumAddress);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.iv_museumListImage);
            viewHolder.hours = (TextView) convertView.findViewById(R.id.tv_museumListHours);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Fill in view holder
        if(museum == null || museum.getName() == null || museum.getAddress() == null || museum.getHours() == null) {
            viewHolder.name.setText("Museum");
            viewHolder.address.setText("Address");
            viewHolder.image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.virgil_white_ic));
            viewHolder.hours.setText("Hours");
        } else {
            viewHolder.name.setText(museum.getName());
            viewHolder.address.setText(museum.getAddress());
            Bitmap museumImage = null;

            Log.d("API", "HERE");
            if (museum.getContent().size() > 0) {
                Log.d("API", "HERE 2");
                for (Content museumContent: museum.getContent()) {
                    if (!museumContent.isMap()) {
                        Log.d("API", "HERE 3");
                        museumImage = museumContent.getImage(this.getContext());
                        break;
                    }
                }
            }

            if (museumImage == null) {
                Log.d("API", "HERE 4");
                viewHolder.image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.museum_list_image));
            }
            else {
                Log.d("API", "HERE 5");
                viewHolder.image.setImageBitmap(museumImage);
            }

            String weekdays[] = new      DateFormatSymbols(Locale.ENGLISH).getWeekdays();
            Calendar c = Calendar.getInstance();
            Date date = new Date();
            c.setTime(date);
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            String hours[] = museum.getHours();
            viewHolder.hours.setText(weekdays[dayOfWeek] + " Hours: " + hours[dayOfWeek-1]);
        }

        return convertView;
    }
}
