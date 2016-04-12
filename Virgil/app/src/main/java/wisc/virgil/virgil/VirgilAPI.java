package wisc.virgil.virgil;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class VirgilAPI implements Serializable {

    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";
    private final String GET_EVENTS = "getEventsForMuseum";
    public final int PENDING_STATUS = 0;
    public final int ERROR_STATUS = -1;
    public final int FINISHED_STATUS = 1;

    public Museum museum;
    public ArrayList<Museum> museumList;
    public boolean listFinished;
    public boolean museumFinished;
    public boolean museumError;

    public ArrayList<Event> eventList;
    public boolean eventListFinished;

    public VirgilAPI() {
        this.museum = null;
        this.museumList = null;
        this.museumFinished = false;
        this.museumError = false;
        this.listFinished = false;
        this.eventList = null;
        this.eventListFinished = false;
    }

    public void fetchMuseum(int id) {
        this.museum = null;
        this.museumFinished = false;
        this.museumError = false;
        BackendTaskRunner runner = new BackendTaskRunner(this);
        runner.execute(GET_MUSEUM, Integer.toString(id));
    }

    public void fetchMuseum(Museum museum) {
        fetchMuseum(museum.getId());
    }

    public void fetchAllMuseums() {
        this.museumList = null;
        BackendTaskRunner runner = new BackendTaskRunner(this);
        runner.execute(GET_ALL_MUSEUMS);
    }

    public ArrayList<Museum> getMuseumList() {
        Log.d("API", "getting " + this.museumList.size() + " museums.");
        return this.museumList;
    }

    public Museum getMuseum() {
        return this.museum;
    }

    public int museumStatus() {
        if (this.museumFinished) {
            return this.FINISHED_STATUS;
        }
        else if (this.museumError) {
            return this.ERROR_STATUS;
        }
        else {
            return this.PENDING_STATUS;
        }
    }

    public int museumListStatus() {
        if (this.listFinished) {
            return this.FINISHED_STATUS;
        }
        else if (this.museumList != null) {
            return this.PENDING_STATUS;
        }
        else {
            return this.ERROR_STATUS;
        }
    }

    public void fetchEvents(int id) {
        BackendTaskRunner runner = new BackendTaskRunner(this);
        runner.execute(GET_EVENTS, Integer.toString(id));
    }

    public void fetchEvents(Museum museum) {
        fetchEvents(museum.getId());
    }

    public ArrayList<Event> getEventList() {
        return this.eventList;
    }

    public int eventListStatus() {
        if (this.eventListFinished) {
            return this.FINISHED_STATUS;
        }
        else if (this.eventList != null) {
            return this.PENDING_STATUS;
        }
        else {
            return this.ERROR_STATUS;
        }
    }

    public List<FavoriteMuseum> getFavorites(Context context) {
        DatabaseTaskRunner dbRunner = new DatabaseTaskRunner(context, this);
        return dbRunner.getFavorites();
    }

    public boolean addFavorite(int id, Context context) {
   //     this.museum = null;
        DatabaseTaskRunner dbRunner = new DatabaseTaskRunner(context, this);
        boolean success = dbRunner.addFavorite(id);
    //    this.museum = null;
        return success;
    }

    public boolean deleteFavorite(int id, Context context) {
     //   this.museum = null;
        DatabaseTaskRunner dbRunner = new DatabaseTaskRunner(context, this);
        boolean success = dbRunner.deleteFavorite(id);
     //   this.museum = null;
        return success;
    }

    public void addFavorite(Museum favMuseum, Context context) {
        addFavorite(museum.getId(), context);
    }

    public void deleteFavorite(Museum favMuseum, Context context) {
        deleteFavorite(museum.getId(), context);
    }
}
