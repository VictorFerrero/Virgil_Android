package wisc.virgil.virgil;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Museum {

    private int id;
    private float latitude, longitude;
    private String name, address;
    private ArrayList<Gallery> galleries;
    private ArrayList<Content> content;

    public Museum(int id, float latitude, float longitude, String name, String address) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.address = address;
        this.galleries = new ArrayList<>();
        this.content = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public boolean addGallery(Gallery gallery) {
        return this.galleries.add(gallery);
    }

    public ArrayList<Gallery> getGalleries() {
        return this.galleries;
    }

    public boolean addContent(Content content) {
        return this.content.add(content);
    }

    public ArrayList<Content> getContent() {
        return this.content;
    }
}
