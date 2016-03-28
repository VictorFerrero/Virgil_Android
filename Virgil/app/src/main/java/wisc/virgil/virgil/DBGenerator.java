package wisc.virgil.virgil;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by TylerPhelps on 3/27/16.
 */

public class DBGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "wisc.virgil.virgil"); //Scheme for GreenDAO ORM
        createDB(schema);
        new DaoGenerator().generateAll(schema, "./app/src/main/java/");
        //where you want to store the generated classes.
    }

    private static void createDB(Schema schema) {

        //Add FavoriteMuseum
        Entity guest = schema.addEntity("FavoriteMuseum");
        guest.addIdProperty();
        guest.addStringProperty("museumID");
        guest.addStringProperty("name");
        guest.addStringProperty("address");
        guest.addStringProperty("pathToPicture");
        guest.addBooleanProperty("display");
    }

}
