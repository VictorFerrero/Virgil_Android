package wisc.virgil.virgil;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ty Talafous on 3/22/2016.
 */
public class OnItemClickListenerMuseumList implements AdapterView.OnItemClickListener {

    //Eventually sets layout to more detailed view of clicked museum
    //Switches to Museum View
    //TODO: Could be merged into MuseumSelectActivity instead (less java clutter)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();

        //Currently displays a toast containing the selected museum's name and address
        TextView museumName = (TextView) view.findViewById(R.id.tv_museumName);
        String name = museumName.getText().toString();
        TextView museumAddress = (TextView) view.findViewById(R.id.tv_museumAddress);
        String address = museumAddress.getText().toString();

        Toast.makeText(context, "Name: " + name + ", Address: " + address,
                Toast.LENGTH_SHORT).show();
        //End of toast
    }
}
