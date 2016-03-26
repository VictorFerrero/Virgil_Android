package wisc.virgil.virgil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Posthuman on 3/26/2016.
 */
public class MuseumSelectAdapter extends ArrayAdapter<Museum> {

    public MuseumSelectAdapter(Context context, ArrayList<Museum> museums) {
        super(context, R.layout.museum_list_item, museums);
    }

    private static class ViewHolder {
        TextView name;
        TextView address;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Museum museum = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.museum_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_museumName);
            viewHolder.address = (TextView) convertView.findViewById(R.id.tv_museumAddress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(museum.getName());
        viewHolder.address.setText(museum.getAddress());

        return convertView;
    }
}
