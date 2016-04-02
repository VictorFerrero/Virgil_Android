package wisc.virgil.virgil;

/**
 * Created by TylerPhelps on 4/2/16.
 */
public class Event {
    private int id, galleryId, exhibitId, museumId;
    private String description, startTime, endTime;

    public Event(int id, int galleryId, int exhibitId, int museumId, String description, String startTime, String endTime) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
