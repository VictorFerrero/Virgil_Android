package wisc.virgil.virgil;

import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.BitmapFactory;
import java.io.InputStream;
import android.util.Log;
import android.graphics.drawable.BitmapDrawable;



/**
 * Created by TylerPhelps on 4/14/16.
 */
public class ImageDownloadTaskRunner extends AsyncTask<String, Void, Bitmap> {

    private Content parent;
    private Bitmap bitmap;

    public ImageDownloadTaskRunner (Content parent) {
        this.parent = parent;
        this.bitmap = null;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Log.d("Downnloading", "Bitmap");
        download_Image(url);
        Log.d("Downnloading", "done");
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        this.parent.contentFinished = true;
    }

    private void download_Image(String url) {
        try {
            Log.d("Downloader", "trying");
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();

            this.bitmap = BitmapFactory.decodeStream(input);
            Log.d("Downloader", "winning");
            this.parent.contentFinished = true;
        }
        catch (Exception e) {
            Log.d("Downloader", "failing");
            this.parent.contentError = true;
        }
    }

    public Bitmap getImage() {
        Log.d("Downloader", "returning");
        return this.bitmap;
    }
}