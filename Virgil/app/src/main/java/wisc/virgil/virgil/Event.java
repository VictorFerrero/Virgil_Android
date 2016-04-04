package wisc.virgil.virgil;

import java.util.ArrayList;

/**
 * Created by TylerPhelps on 4/2/16.
 */
public class Event {
    private int id, galleryId, exhibitId, museumId;
    private String description, startTime, endTime;
    private ArrayList<Content> eventContent;

    public Event(int id, int galleryId, int exhibitId, int museumId, String description, String startTime, String endTime) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventContent = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public int getGalleryId() {
        return this.galleryId;
    }

    public int getExhibitId() {
        return this.exhibitId;
    }

    public int getMuseumId() {
        return this.museumId;
    }

    public String getDescription() {
        return this.getDescription();
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public ArrayList<Content> getEventContent() {
        return this.eventContent;
    }

    public boolean addContent(Content content) {
        return this.eventContent.add(content);
    }

}
