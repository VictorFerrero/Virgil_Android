package wisc.virgil;

import java.util.ArrayList;
import android.util.Log;
import android.os.AsyncTask;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class VirgilAPI {

    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";
    public final int PENDING_STATUS = 0;
    public final int ERROR_STATUS = -1;
    public final int FINISHED_STATUS = 1;

    public Museum museum;
    public ArrayList<Museum> museumList;

    public VirgilAPI() {
        this.museum = null;
        this.museumList = null;
    }

    public void fetchMuseum(int id) {
        BackendTaskRunner runner = new BackendTaskRunner(this);
        runner.execute(GET_MUSEUM, Integer.toString(id));
    }

    public void fetchMuseum(Museum museum) {
        fetchMuseum(museum.getId());
    }

    public void fetchAllMuseums() {
        BackendTaskRunner runner = new BackendTaskRunner(this);
        runner.execute(GET_ALL_MUSEUMS);
    }

    public ArrayList<Museum> getMuseumList() {
        return this.museumList;
    }

    public Museum getMuseum() {
        return this.museum;
    }

    public int museumStatus() {
        if (this.museum != null) {
            if (this.museum.getName().isEmpty()) {
                return this.ERROR_STATUS;
            }
            else return this.FINISHED_STATUS;
        }
        else return this.PENDING_STATUS;
    }

    public int museumListStatus() {
        if (this.museumList != null) {
            return this.FINISHED_STATUS;
        }
        else {
            return this.PENDING_STATUS;
        }
    }
}
