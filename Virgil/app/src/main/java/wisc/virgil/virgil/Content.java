package wisc.virgil.virgil;

import android.graphics.drawable.Drawable;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import android.util.Log;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Content implements Serializable {

    private final String IP_ADDRESS = "http://52.24.10.104/";
    private final String RESOURCE_PATH = "Virgil_Uploads/images/";
    private final String DEFAULT_IMAGE = "drawable/museum_list_image.jpg";

    private int id, galleryId, exhibitId, museumId;
    private String description, pathToContent;
    private Drawable image;

    public Content(int id, int galleryId, int exhibitId,
                   int museumId, String description, String pathToContent) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;
        this.pathToContent = IP_ADDRESS + RESOURCE_PATH + pathToContent;
        this.image = null;

        loadContentImage();
    }

    public int getId() { return this.id; }

    public int getGallerytId() { return this.galleryId; }

    public int getExhibitId() { return this.exhibitId; }

    public int getMuseumId() { return this.museumId; }

    public String getDescription() { return this.description; }

    private void loadContentImage() {
        try {
            InputStream URLcontent = (InputStream) new URL(pathToContent).getContent();
            this.image = Drawable.createFromStream(URLcontent, null);
        }
        catch (Exception e) {
            this.image = Drawable.createFromPath(DEFAULT_IMAGE);
            Log.d("Content", "Could not get image for content");
        }
    }

    public String getPathToContent() { return this.pathToContent; }

    public Drawable getImage() {
        return this.image;
    }

}
