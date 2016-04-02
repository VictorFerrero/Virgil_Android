package wisc.virgil.virgil;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TylerPhelps on 4/1/16.
 */
public class DatabaseTaskRunner extends AsyncTask<String, String, String> {

    private VirgilAPI myParent;

    DaoMaster.DevOpenHelper favoritesDBHelper;
    SQLiteDatabase favoritesDB;
    DaoMaster daoMaster;
    DaoSession daoSession;
    FavoriteMuseumDao favMuseumDao;
    List<FavoriteMuseum> favoritesFromDB;

    public DatabaseTaskRunner (VirgilAPI parent) {
        this.myParent = parent;
    }

    private void initDatabase() {
        favoritesDBHelper = new DaoMaster.DevOpenHelper(null, "ORM.sqlite", null);

        //Get DaoMaster
        daoMaster = new DaoMaster(favoritesDB);

        daoSession = daoMaster.newSession();

        favMuseumDao = daoSession.getFavoriteMuseumDao();

        if (favMuseumDao.queryBuilder().where(
                FavoriteMuseumDao.Properties.Display.eq(true)).list() == null)
        {
            closeReopenDatabase();
        }

        favoritesFromDB = favMuseumDao.queryBuilder().where(
                FavoriteMuseumDao.Properties.Display.eq(true)).list();

        if (favoritesFromDB != null) {

            for (FavoriteMuseum guest : favoritesFromDB)
            {
                if (guest == null)
                {
                    return;
                }

            }
        }
    }

    private boolean addFavorite(int id)
    {
        myParent.fetchMuseum(id);

        while (myParent.museumStatus() == myParent.FINISHED_STATUS) {
            if (myParent.museumStatus() == myParent.ERROR_STATUS) {
                return false;
            }
        }

        Museum museum = myParent.getMuseum();
        Random rand = new Random();

        FavoriteMuseum newFav = new FavoriteMuseum(rand.nextLong(), "ID", museum.getName(), museum.getAddress(), "/", true);

        favMuseumDao.insert(newFav);
        favoritesFromDB.add(newFav);

        return true;
    }


    public List<FavoriteMuseum> getFavorites() {
        return this.favoritesFromDB;
    }

    public boolean deleteFavorite(int id) {

        for (FavoriteMuseum favMuseum : favoritesFromDB) {
            if (favMuseum.getId() == id) {
                favoritesFromDB.remove(favMuseum);
                favMuseumDao.delete(favMuseum);
                return true;
            }
        }

        return false;
    }

    private void closeDatabase()
    {
        daoSession.clear();
        favoritesDB.close();
        favoritesDBHelper.close();
    }

    private void closeReopenDatabase()
    {
        closeDatabase();

        favoritesDBHelper = new DaoMaster.DevOpenHelper(null, "ORM.sqlite", null);
        favoritesDB = favoritesDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(favoritesDB);

        //Create database and tables
        daoMaster.createAllTables(favoritesDB, true);

        //Create DaoSession
        daoSession = daoMaster.newSession();

        //Create customer addition/removal instances
        favMuseumDao = daoSession.getFavoriteMuseumDao();
    }

    @Override
    protected String doInBackground(String... params) {
        initDatabase();

        String callType = params[0];
        int id;

        if (callType.equals("add")) {
            id = Integer.parseInt(params[1]);
            addFavorite(id);
        }
        else if (callType.equals("delete")) {
            id = Integer.parseInt(params[1]);
            deleteFavorite(id);
        }
        else {
            getFavorites();
        }

        return null;
    }
}