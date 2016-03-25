package wisc.virgil.virgil;

import java.util.ArrayList;

/**
 * Created by TylerPhelps on 3/18/16.
 */
public class Gallery {

    private int id, museumId;
    private String name;
    private ArrayList<Exhibit> exhibits;
    private ArrayList<Content> content;

    public Gallery(int id, int museumId, String name) {
        this.id = id;
        this.museumId = museumId;
        this.name = name;
        this.exhibits = new ArrayList<>();
        this.content = new ArrayList<>();
    }

    public int getId() {
        return this.id;
    }

    public int getMuseumId() {
        return this.museumId;
    }

    public String getName() {
        return this.name;
    }

    public boolean addExhibit(Exhibit exhibit) {
        return this.exhibits.add(exhibit);
    }

    public ArrayList<Exhibit> getExhibits(){
        return this.exhibits;
    }

    public boolean addContent(Content content) {
        return this.content.add(content);
    }

    public ArrayList<Content> getContent() {
        return this.content;
    }
}
