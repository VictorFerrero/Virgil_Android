package wisc.virgil.virgil;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * Created by TylerPhelps on 3/19/16.
 */
public class BackendTaskRunner extends AsyncTask<String, String, Museum> {

    private final String PATH_OF_API = "http://52.24.10.104/Virgil_Backend/index.php/";
    private final String GET_MUSEUM_PATH = "getEntireMuseum/";
    private final String GET_ALL_MUSEUMS_PATH = "getAllMuseums/";
    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";

    private String jsonString;

    private VirgilAPI myParent = null;

    public BackendTaskRunner (VirgilAPI parent) {
        this.myParent = parent;
    }

    @Override
    protected void onPreExecute() {
        this.myParent.museumList = new ArrayList<>();
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

        this.myParent.museumList = new ArrayList<>();
        this.myParent.listFinished = false;

        String[] splitInput = input.split("\\[");
        input = splitInput[1];
        splitInput = input.split("\\]");
        input = splitInput[0];
        input = "[" + input + "]";

        try {
            JSONArray jsonarray = new JSONArray(input);

            for(int i = 0; i < jsonarray.length(); i++){
                JSONObject obj = jsonarray.getJSONObject(i);

                String id = obj.getString("id");
                String name = obj.getString("museumName");
                String address = obj.getString("address");

                Museum newMuseum = new Museum(Integer.parseInt(id), name, address);
                this.myParent.museumList.add(newMuseum);
            }
            Log.d("API", "List parsed. " + myParent.museumList.size() + " museums.");
            this.myParent.listFinished = true;
        }
        catch (Exception e) {
            Log.d("Error", "Couldn't parse this list.");
        }
    }

    private void parseMuseum(String input) {

        try {
            JSONObject jsonObject = new JSONObject(input);

            JSONArray museum = (JSONArray) jsonObject.get("museum");
            JSONObject obj = museum.getJSONObject(0);
            String id = obj.getString("id");
            String name = obj.getString("museumName");
            String address = obj.getString("address");

            this.myParent.museum = new Museum(Integer.parseInt(id), name, address);

            JSONArray galleries = (JSONArray) jsonObject.get("galleries");
            for(int i = 0; i < galleries.length(); i++){
                JSONObject gallery = galleries.getJSONObject(i);

                String galleryId = gallery.getString("id");
                String galleryName = gallery.getString("galleryName");

                Gallery newGallery = new Gallery(Integer.parseInt(galleryId), this.myParent.museum.getId(), galleryName);
                this.myParent.museum.addGallery(newGallery);
                Log.d("API", "Added Gallery:" + newGallery.getName());
            }


            JSONArray exhibits = (JSONArray) jsonObject.get("exhibits");
            for(int i = 0; i < exhibits.length(); i++){
                JSONObject exhibit = exhibits.getJSONObject(i);

                String exhibitId = exhibit.getString("id");
                String galleryId = exhibit.getString("galleryId");
                String exhibitName = exhibit.getString("exhibitName");

                Exhibit newExhibit = new Exhibit(Integer.parseInt(exhibitId), Integer.parseInt(galleryId), this.myParent.museum.getId(), exhibitName);
                sortExhibits(newExhibit);

                Log.d("API", "Added exhibit: " + newExhibit.getName() + " to Gallery:" + newExhibit.getGallerytId());
            }

            JSONArray contents = (JSONArray) jsonObject.get("content");
            for(int i = 0; i < contents.length(); i++){
                JSONObject content = contents.getJSONObject(i);

                String contentId = content.getString("id");
                String contentGalleryId = content.getString("galleryId");
                String contentExhibitId = content.getString("exhibitId");
                String contentMuseumId = content.getString("museumId");
                String description = content.getString("description");
                String pathToContent = content.getString("pathToContent");

                Content newContent = new Content(Integer.parseInt(contentId), Integer.parseInt(contentGalleryId),
                        Integer.parseInt(contentExhibitId), Integer.parseInt(contentMuseumId), description, pathToContent);

                sortContent(newContent);
                Log.d("API", "Added Content: " + newContent.getDescription());
            }

            Log.d("Runner", "Museum Name: "+this.myParent.museum.getName());

        }
        catch (Exception e) {
            Log.d("Error", "Couldn't parse this museum.");
            this.myParent.museum = new Museum(0, "", "");
        }
    }

    private void sortContent(Content content) {
        if (content.getGallerytId() == 0) {
            this.myParent.museum.addContent(content);
        }
        else {
            for (Gallery gallery : this.myParent.museum.getGalleries()) {
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

    private void sortExhibits(Exhibit exhibit) {
        for (Gallery gallery : this.myParent.museum.getGalleries()) {
            if (gallery.getId() == exhibit.getGallerytId()) {
                gallery.addExhibit(exhibit);
            }
        }
    }
}