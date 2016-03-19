package wisc.virgil.virgil;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Content {

    private int id, galleryId, exhibitId, museumId;
    private String description, pathToContent;

    public Content(int id, int galleryId, int exhibitId,
                   int museumId, String description, String pathToContent) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;
        this.pathToContent = pathToContent;
    }

    public int getId() { return this.id; }

    public int getGallerytId() { return this.galleryId; }

    public int getExhibitId() { return this.exhibitId; }

    public int getMuseumId() { return this.museumId; }

    public String getDescription() { return this.description; }

    public String getPathToContent() { return this.pathToContent; }

}
