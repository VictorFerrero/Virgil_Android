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
import android.view.Display;

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
public class DatabaseTaskRunner extends AsyncTask<String, String, String> {

    private VirgilAPI myParent;

    private DaoMaster.DevOpenHelper favoritesDBHelper;
    private SQLiteDatabase favoritesDB;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private FavoriteMuseumDao favMuseumDao;
    private Context context;

    public DatabaseTaskRunner (VirgilAPI parent) {
        this.myParent = parent;
        this.context = new Context() {
            @Override
            public AssetManager getAssets() {
                return null;
            }

            @Override
            public Resources getResources() {
                return null;
            }

            @Override
            public PackageManager getPackageManager() {
                return null;
            }

            @Override
            public ContentResolver getContentResolver() {
                return null;
            }

            @Override
            public Looper getMainLooper() {
                return null;
            }

            @Override
            public Context getApplicationContext() {
                return null;
            }

            @Override
            public void setTheme(int resid) {

            }

            @Override
            public Resources.Theme getTheme() {
                return null;
            }

            @Override
            public ClassLoader getClassLoader() {
                return null;
            }

            @Override
            public String getPackageName() {
                return null;
            }

            @Override
            public ApplicationInfo getApplicationInfo() {
                return null;
            }

            @Override
            public String getPackageResourcePath() {
                return null;
            }

            @Override
            public String getPackageCodePath() {
                return null;
            }

            @Override
            public SharedPreferences getSharedPreferences(String name, int mode) {
                return null;
            }

            @Override
            public FileInputStream openFileInput(String name) throws FileNotFoundException {
                return null;
            }

            @Override
            public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
                return null;
            }

            @Override
            public boolean deleteFile(String name) {
                return false;
            }

            @Override
            public File getFileStreamPath(String name) {
                return null;
            }

            @Override
            public File getFilesDir() {
                return null;
            }

            @Override
            public File getNoBackupFilesDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalFilesDir(String type) {
                return null;
            }

            @Override
            public File[] getExternalFilesDirs(String type) {
                return new File[0];
            }

            @Override
            public File getObbDir() {
                return null;
            }

            @Override
            public File[] getObbDirs() {
                return new File[0];
            }

            @Override
            public File getCacheDir() {
                return null;
            }

            @Override
            public File getCodeCacheDir() {
                return null;
            }

            @Nullable
            @Override
            public File getExternalCacheDir() {
                return null;
            }

            @Override
            public File[] getExternalCacheDirs() {
                return new File[0];
            }

            @Override
            public File[] getExternalMediaDirs() {
                return new File[0];
            }

            @Override
            public String[] fileList() {
                return new String[0];
            }

            @Override
            public File getDir(String name, int mode) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
                return null;
            }

            @Override
            public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
                return null;
            }

            @Override
            public boolean deleteDatabase(String name) {
                return false;
            }

            @Override
            public File getDatabasePath(String name) {
                return null;
            }

            @Override
            public String[] databaseList() {
                return new String[0];
            }

            @Override
            public Drawable getWallpaper() {
                return null;
            }

            @Override
            public Drawable peekWallpaper() {
                return null;
            }

            @Override
            public int getWallpaperDesiredMinimumWidth() {
                return 0;
            }

            @Override
            public int getWallpaperDesiredMinimumHeight() {
                return 0;
            }

            @Override
            public void setWallpaper(Bitmap bitmap) throws IOException {

            }

            @Override
            public void setWallpaper(InputStream data) throws IOException {

            }

            @Override
            public void clearWallpaper() throws IOException {

            }

            @Override
            public void startActivity(Intent intent) {

            }

            @Override
            public void startActivity(Intent intent, Bundle options) {

            }

            @Override
            public void startActivities(Intent[] intents) {

            }

            @Override
            public void startActivities(Intent[] intents, Bundle options) {

            }

            @Override
            public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws IntentSender.SendIntentException {

            }

            @Override
            public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {

            }

            @Override
            public void sendBroadcast(Intent intent) {

            }

            @Override
            public void sendBroadcast(Intent intent, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Override
            public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {

            }

            @Override
            public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void sendStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void removeStickyBroadcast(Intent intent) {

            }

            @Override
            public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Override
            public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {

            }

            @Override
            public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {

            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                return null;
            }

            @Nullable
            @Override
            public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
                return null;
            }

            @Override
            public void unregisterReceiver(BroadcastReceiver receiver) {

            }

            @Nullable
            @Override
            public ComponentName startService(Intent service) {
                return null;
            }

            @Override
            public boolean stopService(Intent service) {
                return false;
            }

            @Override
            public boolean bindService(Intent service, ServiceConnection conn, int flags) {
                return false;
            }

            @Override
            public void unbindService(ServiceConnection conn) {

            }

            @Override
            public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
                return false;
            }

            @Override
            public Object getSystemService(String name) {
                return null;
            }

            @Override
            public String getSystemServiceName(Class<?> serviceClass) {
                return null;
            }

            @Override
            public int checkPermission(String permission, int pid, int uid) {
                return PackageManager.PERMISSION_DENIED;
            }

            @Override
            public int checkCallingPermission(String permission) {
                return PackageManager.PERMISSION_DENIED;
            }

            @Override
            public int checkCallingOrSelfPermission(String permission) {
                return PackageManager.PERMISSION_DENIED;
            }

            @Override
            public int checkSelfPermission(String permission) {
                return PackageManager.PERMISSION_DENIED;
            }

            @Override
            public void enforcePermission(String permission, int pid, int uid, String message) {

            }

            @Override
            public void enforceCallingPermission(String permission, String message) {

            }

            @Override
            public void enforceCallingOrSelfPermission(String permission, String message) {

            }

            @Override
            public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {

            }

            @Override
            public void revokeUriPermission(Uri uri, int modeFlags) {

            }

            @Override
            public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
                return 0;
            }

            @Override
            public int checkCallingUriPermission(Uri uri, int modeFlags) {
                return 0;
            }

            @Override
            public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
                return 0;
            }

            @Override
            public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
                return 0;
            }

            @Override
            public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {

            }

            @Override
            public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {

            }

            @Override
            public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {

            }

            @Override
            public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {

            }

            @Override
            public Context createPackageContext(String packageName, int flags) throws PackageManager.NameNotFoundException {
                return null;
            }

            @Override
            public Context createConfigurationContext(Configuration overrideConfiguration) {
                return null;
            }

            @Override
            public Context createDisplayContext(Display display) {
                return null;
            }
        };
        initDatabase();
    }

    private void initDatabase() {
        favoritesDBHelper = new DaoMaster.DevOpenHelper(null, "ORM.sqlite", null);
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
        Log.d("DB", ("add " + id));

        myParent.fetchMuseum(id);
        Log.d("DB", "fetched");

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
        Log.d("DB", "added successfully");

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

    @Override
    protected String doInBackground(String... params) {
        Log.d("DB", "here");

        String callType = params[0];
        int id;

        if (callType.equals("add")) {
            Log.d("DB", "going to add");
            id = Integer.parseInt(params[1]);
            addFavorite(1);
        }
        else if (callType.equals("delete")) {
            id = Integer.parseInt(params[1]);
            deleteFavorite(id);
        }
        else {
            getFavorites();
        }

        closeDatabase();
        return null;
    }
}