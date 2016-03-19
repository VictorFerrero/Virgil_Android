package wisc.virgil.virgil;

import java.util.ArrayList;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class VirgilAPI {

    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";

    public Museum getMuseum(int id) {
        BackendTaskRunner runner = new BackendTaskRunner();
        runner.execute(GET_MUSEUM, Integer.toString(id));

        return runner.getMuseum();
    }

    public ArrayList<Museum> getAllMuseums() {
        BackendTaskRunner runner = new BackendTaskRunner();
        runner.execute(GET_ALL_MUSEUMS);

        return runner.getMuseumList();
    }
}
