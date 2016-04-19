package wisc.virgil.virgil;

import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

/**
 * Entity mapped to table FAVORITE_MUSEUM.
 */
public class FavoriteMuseum implements Serializable {

    private Long id;
    private Integer museumID;
    private String name;
    private String address;
    private String pathToPicture;
    private Boolean display;
    private Context context;

    public FavoriteMuseum() {
    }

    public FavoriteMuseum(Long id) {
        this.id = id;
    }

    public FavoriteMuseum(Long id, Integer museumID, String name, String address, String pathToPicture, Boolean display, Bitmap image, Context context) {
        this.id = id;
        this.museumID = museumID;
        this.name = name;
        this.address = address;
        this.pathToPicture = pathToPicture;
        this.display = display;

        this.context = context;

        if (image != null) {
            Log.d("FavMuseum", "Image to save for the museum!");
            saveToInternalStorage(image);
        }
        else {
            Log.d("FavMuseum", "No museum image to save");
        }
    }

    public FavoriteMuseum(Long id, Integer museumID, String name, String address, String pathToPicture, Boolean display) {
        this.id = id;
        this.museumID = museumID;
        this.name = name;
        this.address = address;
        this.pathToPicture = pathToPicture;
        this.display = display;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMuseumID() {
        return museumID;
    }

    public void setMuseumID(Integer museumID) {
        this.museumID = museumID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPathToPicture() {
        return pathToPicture;
    }

    public void setPathToPicture(String pathToPicture) {
        this.pathToPicture = pathToPicture;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(this.context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("favImageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,this.pathToPicture);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return directory.getAbsolutePath();
    }

    public Bitmap getImage(Context context)
    {
        Bitmap museumImage = null;

        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("favImageDir", Context.MODE_PRIVATE);
            File f=new File(directory, this.pathToPicture);
            museumImage = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return museumImage;
    }

}
