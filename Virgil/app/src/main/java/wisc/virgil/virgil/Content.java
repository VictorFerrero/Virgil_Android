package wisc.virgil.virgil;

import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import java.io.FileInputStream;
import java.io.Serializable;
import android.util.Log;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import android.graphics.Bitmap;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Content implements Serializable {

    private final String IP_ADDRESS = "http://52.24.10.104/";
    private final String RESOURCE_PATH = "Virgil_Uploads/images/";

    private int id, galleryId, exhibitId, museumId;
    private String description, pathToContent, cachedImageName;
    public boolean contentFinished;
    public boolean contentError;


    public Content(int id, int galleryId, int exhibitId,
                   int museumId, String description, String pathToContent) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;
        this.pathToContent = IP_ADDRESS + RESOURCE_PATH + pathToContent;
        this.cachedImageName = id + "_" + exhibitId + "_" + galleryId + "_" + museumId +".png";
    }

    public int getId() { return this.id; }

    public int getGallerytId() { return this.galleryId; }

    public int getExhibitId() { return this.exhibitId; }

    public int getMuseumId() { return this.museumId; }

    public String getDescription() { return this.description; }

    public String getPathToContent() { return this.pathToContent; }

    public Bitmap getImage(Context context) {
        Bitmap contentImage = getImageFromInternalStorage(context);

        if (contentImage == null) {
            Log.d("Content", "Pulling image from web");
            //get
            this.contentFinished = false;
            this.contentError = false;
            ImageDownloadTaskRunner runner = new ImageDownloadTaskRunner(this);
            runner.execute(pathToContent);

            //wait
            while (!this.contentFinished) {
                if (this.contentError) {
                    Log.d("Content", "Error in getting image");
                    return null;
                }
            }

            //return
            contentImage = runner.getImage();
            if (contentImage != null){
                Log.d("Content", "SUCCESS!!!!!");
                saveToInternalStorage(contentImage, context);
            }
        }

        return contentImage;
    }


    private String saveToInternalStorage(Bitmap bitmapImage, Context context){
        Log.d("Content", "Saving to internal storage");
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,this.cachedImageName);

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

    private Bitmap getImageFromInternalStorage(Context context)
    {
        Log.d("Content", "Searching internal storage");
        Bitmap museumImage = null;

        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f=new File(directory, this.cachedImageName);
            museumImage = BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return museumImage;
    }

}