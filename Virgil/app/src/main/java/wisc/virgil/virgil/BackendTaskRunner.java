package wisc.virgil.virgil;

import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.util.Log;


/**
 * Created by TylerPhelps on 3/19/16.
 */
public class BackendTaskRunner extends AsyncTask<String, String, Museum> {

    final String PATH_OF_API = "http://52.24.10.104/Virgil_Backend/index.php/";
    final String GET_MUSEUM_PATH = "getEntireMuseum/";
    final String GET_ALL_MUSEUMS_PATH = "getAllMuseums/";
    final String GET_MUSEUM = "getMuseum";
    final String GET_ALL_MUSEUMS = "getAllMuseums";

    Exception mException = null;
    private Museum museum;

    @Override
    protected void onPreExecute() {
        /*
         *    do things before doInBackground() code runs
         *    such as preparing and showing a Dialog or ProgressBar
        */
    }

    @Override
    protected void onProgressUpdate(String... values) {
        /*
         *    updating data
         *    such a Dialog or ProgressBar
        */

    }

    @Override
    protected Museum doInBackground(String... params) {
        String callType = params[0]; //getAll or getMuseum
        String museumNumber = "0";

        this.museum = new Museum(0, "name", "address");

        if (callType.equals(GET_ALL_MUSEUMS)) {
            //get all museums
            //getJSONObject(GET_ALL_MUSEUMS_PATH);
        }
        else if (callType.equals(GET_MUSEUM)){
            museumNumber = params[1];

            //get specific museum
            //getJSONObject(GET_MUSEUM_PATH+museumNumber);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Museum result) {
        /*
         *    do something with data here
         *    display it or send to mainactivity
         *    close any dialogs/ProgressBars/etc...
        */
    }

    private JSONObject getJSONObject(String subPath) {
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;

        Log.d("API", PATH_OF_API+subPath);

        try {
            url = new URL(PATH_OF_API+subPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";

            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }

            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            this.mException = e;
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        Log.d("JSON Object", object.toString());

        return object;
    }

    public Museum getMuseum() {
        return this.museum;
    }
}