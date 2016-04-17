package wisc.virgil.virgil;

import android.graphics.drawable.Drawable;
import java.io.Serializable;

import android.os.Environment;
import android.util.Log;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;

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

    public Drawable getImage() {
        Drawable image = null;

        image = getImageFromMemCache();

        if (image == null) {
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
            image = runner.getImage();
            if (image != null){
                Log.d("Content", "SUCCESS!!!!!");
                addImageToMemoryCache(image);
            }
            return image;
        }
        else {
            return image;
        }
    }


    public void addImageToMemoryCache(Drawable imageToCache) {
        try {
            Bitmap bm = drawableToBitmap(imageToCache);
            File fileDir = new File("cache/images/");
            saveBitmapToFile(fileDir, cachedImageName, bm, Bitmap.CompressFormat.PNG, 75);
        }
        catch (Exception e) {

        }

    }

    public Drawable getImageFromMemCache() {
        Drawable d = Drawable.createFromPath("cache/images/"+cachedImageName);
        return d;
    }



    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }



    public boolean saveBitmapToFile(File dir, String fileName, Bitmap bm,
                                    Bitmap.CompressFormat format, int quality) {

        File imageFile = new File(dir,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(format,quality,fos);

            fos.close();

            return true;
        }
        catch (Exception e) {
            Log.e("Content", e.getMessage());
        }
        return false;
    }

}