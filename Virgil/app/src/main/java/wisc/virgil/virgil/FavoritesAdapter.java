package wisc.virgil.virgil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ty Talafous on 4/3/2016.
 */
public class FavoritesAdapter extends BaseAdapter {
    private Context mContext;
    private List<FavoriteMuseum> favList;

    public FavoritesAdapter(Context context, List<FavoriteMuseum> favorites) {
        mContext = context;
        favList = favorites;
    }

    @Override
    public int getCount() {
        return favList.size();
    }

    @Override
    public FavoriteMuseum getItem(int position) {
        return favList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return favList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_favorite_item, null);
        }
        imageView = (ImageView) convertView.findViewById(R.id.iv_museum_favorites);
        textView = (TextView) convertView.findViewById(R.id.tv_museum_favorites);

        textView.setText(favList.get(position).getName());

        Bitmap bitmap = BitmapFactory.decodeFile(favList.get(position).getPathToPicture());
        if(bitmap == null) {
            //*Temporary* Small drawable placeholder to prevent too much memory usage
            imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.ic_launcher));
        } else {
            imageView.setImageBitmap(bitmap);
        }

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCropToPadding(true);

        return convertView;
    }
}