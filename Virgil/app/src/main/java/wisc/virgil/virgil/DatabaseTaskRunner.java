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

        myParent.favoriteList = favMuseumDao.queryBuilder().where(
                FavoriteMuseumDao.Properties.Display.eq(true)).list();

        if (myParent.favoriteList != null) {

            for (FavoriteMuseum guest : myParent.favoriteList)
            {
                if (guest == null)
                {
                    return;
                }

            }
        }
    }

    private void addFavorite(int id)
    {
        myParent.fetchMuseum(id);

        while (myParent.museumStatus() == myParent.FINISHED_STATUS) {
            if (myParent.museumStatus() == myParent.ERROR_STATUS) {
                myParent.dbSuccess = false;
            }
        }

        Museum museum = myParent.getMuseum();
        Random rand = new Random();

        FavoriteMuseum newFav = new FavoriteMuseum(rand.nextLong(), "ID", museum.getName(), museum.getAddress(), "/", true);

        favMuseumDao.insert(newFav);
        myParent.favoriteList.add(newFav);

        myParent.dbSuccess = true;
    }


    public List<FavoriteMuseum> getFavorites() {
        return myParent.favoriteList;
    }

    public void deleteFavorite(int id) {

        for (FavoriteMuseum favMuseum : myParent.favoriteList) {
            if (favMuseum.getId() == id) {
                myParent.favoriteList.remove(favMuseum);
                favMuseumDao.delete(favMuseum);
                myParent.dbSuccess = true;
            }
        }

        myParent.dbSuccess = false;
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