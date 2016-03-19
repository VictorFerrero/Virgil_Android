package wisc.virgil.virgil;

import android.os.AsyncTask;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class VirgilAPI extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        /*
         *    do things before doInBackground() code runs
         *    such as preparing and showing a Dialog or ProgressBar
        */
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
        */

    }

    @Override
    protected Void doInBackground(Void... params) {
        //do your work here
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        /*
         *    do something with data here
         *    display it or send to mainactivity
         *    close any dialogs/ProgressBars/etc...
        */
    }
}