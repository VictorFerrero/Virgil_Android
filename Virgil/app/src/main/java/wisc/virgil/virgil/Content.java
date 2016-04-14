package wisc.virgil.virgil;

import android.graphics.drawable.Drawable;
import java.io.Serializable;
import android.util.Log;
import android.graphics.Bitmap;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Content implements Serializable {

    private final String IP_ADDRESS = "http://52.24.10.104/";
    private final String RESOURCE_PATH = "Virgil_Uploads/images/";

    private int id, galleryId, exhibitId, museumId;
    private String description, pathToContent;


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
    }

    public int getId() { return this.id; }

    public int getGallerytId() { return this.galleryId; }

    public int getExhibitId() { return this.exhibitId; }

    public int getMuseumId() { return this.museumId; }

    public String getDescription() { return this.description; }

    public String getPathToContent() { return this.pathToContent; }

    public Drawable getImage() {
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
        Drawable image = runner.getImage();
        if (image != null){
            Log.d("Content", "SUCCESS!!!!!");
        }

        return image;
    }

}