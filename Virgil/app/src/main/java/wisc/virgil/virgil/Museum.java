package wisc.virgil.virgil;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Museum implements Serializable {

    private int id;
    private String[] hours;
    private String name, address;
    private ArrayList<Gallery> galleries;
    private ArrayList<Content> content;

    public Museum(int id, String name, String address, String[] hours) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.galleries = new ArrayList<>();
        this.content = new ArrayList<>();
        this.hours = hours;
    }

    public int getId() {
        return this.id;
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

    public String[] getHours() {
        return this.hours;
    }
}