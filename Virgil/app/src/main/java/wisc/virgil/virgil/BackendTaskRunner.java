package wisc.virgil.virgil;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by TylerPhelps on 3/19/16.
 */
public class BackendTaskRunner extends AsyncTask<String, String, Museum> {

    private final String PATH_OF_API = "http://52.24.10.104/Virgil_Backend/index.php/";
    private final String GET_MUSEUM_PATH = "getEntireMuseum/";
    private final String GET_ALL_MUSEUMS_PATH = "getAllMuseums/";
    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";

    private Museum museum;
    private String jsonString;
    private ArrayList<Museum> museumList;

    @Override
    protected void onPreExecute() {

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

        if (callType.equals(GET_ALL_MUSEUMS)) {
            //get all museums
            jsonString = getJSONString(GET_ALL_MUSEUMS_PATH);
            parseMuseumList(jsonString);
        }
        else if (callType.equals(GET_MUSEUM)){
            museumNumber = params[1];

            //get specific museum
            jsonString = getJSONString(GET_MUSEUM_PATH+museumNumber);

            parseMuseum(jsonString);
        }

        Log.d("API", jsonString);
        return null;
    }

    @Override
    protected void onPostExecute(Museum result) {

    }

    private String getJSONString(String subPath) {
        String returnString = "";

        try {
            URL url = new URL(PATH_OF_API+subPath);

            // read text returned by server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;
            while ((line = in.readLine()) != null) {
                returnString += line;
            }
            in.close();

        }
        catch (MalformedURLException e) {
            System.out.println("Malformed URL: " + e.getMessage());
        }
        catch (IOException e) {
            System.out.println("I/O Error: " + e.getMessage());
        }

        return returnString;
    }

    private void parseMuseumList(String input) {
        //TODO parse list of museums
        //temporary line of code
        this.museumList = null;
    }

    private void parseMuseum(String input) {
        //TODO parse full museum
        //temporary line of code
        this.museum = new Museum(0, "name", "address");
    }

    private void sortContent(Content content) {
        if (content.getGallerytId() == 0) {
            this.museum.addContent(content);
        }
        else {
            for (Gallery gallery : this.museum.getGalleries()) {
                if (gallery.getId() == content.getGallerytId()) {
                    if (content.getExhibitId() == 0) {
                        gallery.addContent(content);
                    }
                    else {
                        for (Exhibit exhibit : gallery.getExhibits()) {
                            if (content.getExhibitId() == exhibit.getId()) {
                                exhibit.addContent(content);
                            }
                        }
                    }
                }
            }
        }
    }

    public Museum getMuseum() {
        return this.museum;
    }

    public ArrayList<Museum> getMuseumList() {
        return this.museumList;
    }
}