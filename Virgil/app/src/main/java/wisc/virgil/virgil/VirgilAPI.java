package wisc.virgil.virgil;

import java.util.ArrayList;
import android.util.Log;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class VirgilAPI {

    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";
    private Museum returnMuseum;
    private ArrayList<Museum> returnList;

    public Museum getMuseum(int id) {
        BackendTaskRunner runner = new BackendTaskRunner();
        runner.execute(GET_MUSEUM, Integer.toString(id));

        return returnMuseum;
    }

    public Museum getMuseum(Museum museum) {
        return getMuseum(museum.getId());
    }

    public ArrayList<Museum> getAllMuseums() {
        returnList = new ArrayList<>();

        BackendTaskRunner runner = new BackendTaskRunner();
        runner.execute(GET_ALL_MUSEUMS);

        return returnList;
    }
}
