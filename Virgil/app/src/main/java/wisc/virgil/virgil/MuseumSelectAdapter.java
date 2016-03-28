package wisc.virgil.virgil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

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
        //no images yet
        //no hours yet
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
            //no images yet
            //no hours yet
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Fill in view holder
        viewHolder.name.setText(museum.getName());
        viewHolder.address.setText(museum.getAddress());

        return convertView;
    }
}
