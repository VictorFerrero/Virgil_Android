package wisc.virgil.virgil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by TylerPhelps on 4/2/16.
 */
public class Event implements Serializable {
    private int id, galleryId, exhibitId, museumId;
    private int startDay, startMonth, startYear, startHour, startMin, startSec;
    private int endDay, endMonth, endYear, endHour, endMin, endSec;
    private String description, startTime, endTime;
    private ArrayList<Content> eventContent;

    public Event(int id, int galleryId, int exhibitId, int museumId, String description, String startTime, String endTime) {
        this.id = id;
        this.galleryId = galleryId;
        this.exhibitId = exhibitId;
        this.museumId = museumId;
        this.description = description;

        this.startTime = startTime;
        String dateStrings[] = this.startTime.split("\\s+");
        if (dateStrings.length > 1) {
            String splitDate[] = dateStrings[0].split("-");
            String splitTime[] = dateStrings[1].split(":");

            if (splitDate.length > 2) {
                this.startYear = Integer.parseInt(splitDate[0]);
                this.startMonth = Integer.parseInt(splitDate[1]);
                this.startDay = Integer.parseInt(splitDate[2]);
            }
            else {
                this.startYear = 0;
                this.startMonth = 0;
                this.startDay = 0;
            }

            if (splitTime.length > 2) {
                this.startHour = Integer.parseInt(splitTime[0]);
                this.startMin = Integer.parseInt(splitTime[1]);
                this.startSec = Integer.parseInt(splitTime[2]);
            }
            else {
                this.startHour = 0;
                this.startMin = 0;
                this.startSec = 0;
            }

        }
        else {
            this.startYear = 0;
            this.startMonth = 0;
            this.startDay = 0;
            this.startHour = 0;
            this.startMin = 0;
            this.startSec = 0;
        }

        this.endTime = endTime;
        dateStrings = this.endTime.split("\\s+");
        if (dateStrings.length > 1) {
            String splitDate[] = dateStrings[0].split("-");
            String splitTime[] = dateStrings[1].split(":");

            if (splitDate.length > 2) {
                this.endYear = Integer.parseInt(splitDate[0]);
                this.endMonth = Integer.parseInt(splitDate[1]);
                this.endDay = Integer.parseInt(splitDate[2]);
            }
            else {
                this.endYear = 0;
                this.endMonth = 0;
                this.endDay = 0;
            }

            if (splitTime.length > 2) {
                this.endHour = Integer.parseInt(splitTime[0]);
                this.endMin = Integer.parseInt(splitTime[1]);
                this.endSec = Integer.parseInt(splitTime[2]);
            }
            else {
                this.endHour = 0;
                this.endMin = 0;
                this.endSec = 0;
            }
        }
        else {
            this.endYear = 0;
            this.endMonth = 0;
            this.endDay = 0;
            this.endHour = 0;
            this.endMin = 0;
            this.endSec = 0;
        }

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

    public int getStartDay() {
        return this.startDay;
    }

    public int getStartMonth() {
        return this.startMonth;
    }

    public int getStartYear() {
        return this.startYear;
    }

    public int getStartHour() {
        return this.startHour;
    }

    public int getStartMin() {
        return this.startMin;
    }

    public int getStartSec() {
        return this.startSec;
    }

    public int getEndDay() {
        return this.endDay;
    }

    public int getEndMonth() {
        return this.endMonth;
    }

    public int getEndYear() {
        return this.endYear;
    }

    public int getEndHour() {
        return this.endHour;
    }

    public int getEndMin() {
        return this.endMin;
    }

    public int getEndSec() {
        return this.endSec;
    }

}
