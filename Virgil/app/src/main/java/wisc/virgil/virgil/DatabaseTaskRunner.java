package wisc.virgil.virgil;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TylerPhelps on 4/1/16.
 */
public class DatabaseTaskRunner {

    private VirgilAPI myParent;

    private DaoMaster.DevOpenHelper favoritesDBHelper;
    private SQLiteDatabase favoritesDB;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private FavoriteMuseumDao favMuseumDao;
    private Context context;


    public List<FavoriteMuseum> favoriteList;

    public DatabaseTaskRunner (Context context, VirgilAPI parent) {
        this.context = context;
        this.myParent = parent;
        initDatabase();
    }

    private void initDatabase() {
        favoritesDBHelper = new DaoMaster.DevOpenHelper(this.context, "ORM.sqlite", null);
        favoritesDB = favoritesDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(favoritesDB);

        //Create database and tables
        daoMaster.createAllTables(favoritesDB, true);

        daoSession = daoMaster.newSession();

        favMuseumDao = daoSession.getFavoriteMuseumDao();

        if (favMuseumDao.queryBuilder().where(
                FavoriteMuseumDao.Properties.Display.eq(true)).list() == null)
        {
            closeReopenDatabase();
        }

        favoriteList = favMuseumDao.queryBuilder().where(
                FavoriteMuseumDao.Properties.Display.eq(true)).list();

        if (favoriteList != null) {

            for (FavoriteMuseum guest : favoriteList)
            {
                if (guest == null)
                {
                    return;
                }

            }
        }
    }

    public boolean addFavorite(int id)
    {
        Log.d("DB", ("add " + id));

        //myParent.fetchMuseum(id);
        Log.d("DB", "fetched");

        /*while (myParent.museumStatus() == myParent.FINISHED_STATUS) {
            if (myParent.museumStatus() == myParent.ERROR_STATUS) {
                closeDatabase();
                return false;
            }
        }

        Museum museum = myParent.getMuseum();*/
        Random rand = new Random();

        FavoriteMuseum newFav = new FavoriteMuseum(rand.nextLong(), id, "Museum", "Address", "/", true);

        favMuseumDao.insert(newFav);
        Log.d("DB", "added successfully");

        closeDatabase();
        return true;
    }


    public List<FavoriteMuseum> getFavorites() {
        return favoriteList;
    }

    public boolean deleteFavorite(int id) {

        for (FavoriteMuseum favMuseum : favoriteList) {
            if (favMuseum.getMuseumID() == id) {
                Log.d("DB", "Deleting museum " + id);
                favMuseumDao.delete(favMuseum);
                closeDatabase();
                return true;
            }
        }

        closeDatabase();
        return false;
    }

    private void closeDatabase()
    {
        Log.d("DB", "close");
        daoSession.clear();
        favoritesDB.close();
        favoritesDBHelper.close();
    }

    private void closeReopenDatabase()
    {
        Log.d("DB", "close reopen");
        closeDatabase();

        favoritesDBHelper = new DaoMaster.DevOpenHelper(this.context, "ORM.sqlite", null);
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
}