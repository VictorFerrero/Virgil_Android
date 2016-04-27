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
    private final String GET_EVENTS_PATH = "events/getEventsForMuseum/";
    private final String GET_MUSEUM = "getMuseum";
    private final String GET_ALL_MUSEUMS = "getAllMuseums";
    private final String GET_EVENTS = "getEventsForMuseum";
    private final String DEFAULT_VAL = "-1";

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
        String museumNumber = DEFAULT_VAL;
        String eventNumber = DEFAULT_VAL;

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
            museumNumber = DEFAULT_VAL;
        }
        else if (callType.equals(GET_EVENTS)){
            eventNumber = params[1];

            jsonString = getJSONString(GET_EVENTS_PATH+eventNumber);

            //parse event
            eventNumber = DEFAULT_VAL;
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

        try {

            JSONObject jsonObject = new JSONObject(input);

            JSONArray museums = (JSONArray) jsonObject.get("museums");

            for (int i = 0; i < museums.length(); i++) {
                JSONObject obj = museums.getJSONObject(i);
                String id = obj.getString("id");
                String name = obj.getString("museumName");
                String address = obj.getString("address");
                String hoursString = obj.getString("museumProfileJSON");

                JSONObject hours = new JSONObject(hoursString);

                String[] museumHours = new String[7];
                museumHours[0] = hours.getString("sun");
                museumHours[1] = hours.getString("mon");
                museumHours[2] = hours.getString("tue");
                museumHours[3] = hours.getString("wed");
                museumHours[4] = hours.getString("thur");
                museumHours[5] = hours.getString("fri");
                museumHours[6] = hours.getString("sat");

                this.myParent.museum = new Museum(Integer.parseInt(id), name, address, museumHours);
                Log.d("Mon:", museumHours[0]);
                Log.d("Tue:", museumHours[1]);
                Log.d("Wed:", museumHours[2]);
                Log.d("Thur:", museumHours[3]);
                Log.d("Fri:", museumHours[4]);
                Log.d("Sat:", museumHours[5]);
                Log.d("Sun:", museumHours[6]);

                Log.d("API", "Added " + name);
                this.myParent.museumList.add(new Museum(Integer.parseInt(id), name, address, museumHours));
            }

            JSONArray contents = (JSONArray) jsonObject.get("content");
            for (int i = 0; i < contents.length(); i++) {
                JSONArray subContent = (JSONArray) contents.get(i);
                Log.d("API", ""+subContent.toString());

                for(int j = 0; j < subContent.length(); j++){
                    JSONObject content = subContent.getJSONObject(j);

                    String contentId = content.getString("id");
                    String contentGalleryId = content.getString("galleryId");
                    String contentExhibitId = content.getString("exhibitId");
                    String contentMuseumId = content.getString("museumId");
                    String description = content.getString("description");
                    String pathToContent = content.getString("pathToContent");

                    String isMapString = content.getString("contentProfileJSON");
                    Log.d("Content", "isMap String: " + isMapString);
                    JSONObject isMapObject = new JSONObject(isMapString);
                    boolean isMapField = false;

                    try {
                        isMapField = isMapObject.getBoolean("isMap");
                        Log.d("Content", "isMap: " + isMapField);
                    }
                    catch (Exception e) {

                    }

                    Content newContent = new Content(Integer.parseInt(contentId), Integer.parseInt(contentGalleryId),
                            Integer.parseInt(contentExhibitId), Integer.parseInt(contentMuseumId), description,
                            pathToContent, isMapField);

                    sortContent(newContent);
                    Log.d("API", "Added Content: " + newContent.getDescription());
                }

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
            this.myParent.museumFinished = false;
            this.myParent.museumError = false;

            JSONObject jsonObject = new JSONObject(input);

            JSONArray museum = (JSONArray) jsonObject.get("museum");
            JSONObject obj = museum.getJSONObject(0);
            String id = obj.getString("id");
            String name = obj.getString("museumName");
            String address = obj.getString("address");
            String hoursString = obj.getString("museumProfileJSON");

            JSONObject hours = new JSONObject(hoursString);

            String[] museumHours = new String[7];
            museumHours[0] = hours.getString("sun");
            museumHours[1] = hours.getString("mon");
            museumHours[2] = hours.getString("tue");
            museumHours[3] = hours.getString("wed");
            museumHours[4] = hours.getString("thur");
            museumHours[5] = hours.getString("fri");
            museumHours[6] = hours.getString("sat");

            this.myParent.museum = new Museum(Integer.parseInt(id), name, address, museumHours);
            Log.d("Mon:", museumHours[0]);
            Log.d("Tue:", museumHours[1]);
            Log.d("Wed:", museumHours[2]);
            Log.d("Thur:", museumHours[3]);
            Log.d("Fri:", museumHours[4]);
            Log.d("Sat:", museumHours[5]);
            Log.d("Sun:", museumHours[6]);

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

                String isMapString = content.getString("contentProfileJSON");
                Log.d("Content", "isMap String: " + isMapString);
                JSONObject isMapObject = new JSONObject(isMapString);
                boolean isMapField = false;

                try {
                    isMapField = isMapObject.getBoolean("isMap");
                    Log.d("Content", "isMap: " + isMapField);
                }
                catch (Exception e) {

                }

                Content newContent = new Content(Integer.parseInt(contentId), Integer.parseInt(contentGalleryId),
                        Integer.parseInt(contentExhibitId), Integer.parseInt(contentMuseumId), description,
                        pathToContent, isMapField);

                sortContent(newContent);
                Log.d("API", "Added Content: " + newContent.getDescription());
            }

            Log.d("Runner", "Museum Name: "+this.myParent.museum.getName());
            this.myParent.museumFinished = true;
            Log.d("Runner", "museumFinished: " + myParent.museumFinished);
        }
        catch (Exception e) {
            Log.d("Error", "Couldn't parse this museum.");
            this.myParent.museumError = true;
            this.myParent.museum = new Museum(0, "", "", new String[7]);
        }

        for (Gallery gal : this.myParent.museum.getGalleries()) {
            Log.d(this.myParent.museum.getName(), gal.getName() + ": " + gal.getExhibits().size() + " exhibits.");
        }
        this.myParent.museumFinished = true;
        Log.d("Outside Runner", "museumFinished: " + myParent.museumFinished);
    }

    private void parseEventList(String input) {
        this.myParent.eventList = new ArrayList<>();
        this.myParent.eventListFinished = false;

        try {
            String[] splitInput = input.split("\\[");
            input = splitInput[1];
            splitInput = input.split("\\]");
            input = splitInput[0];
            input = "[" + input + "]";

            JSONArray jsonarray = new JSONArray(input);

            for(int i = 0; i < jsonarray.length(); i++){
                JSONObject obj = jsonarray.getJSONObject(i);

                String id = obj.getString("id");
                String galleryId = obj.getString("galleryid");
                String exhibitId = obj.getString("exhibitId");
                String museumId = obj.getString("museumId");
                String description = obj.getString("description");
                String startTime = obj.getString("startTime");
                String endTime = obj.getString("endTime");

                Event newEvent = new Event(Integer.parseInt(id), Integer.parseInt(galleryId),
                        Integer.parseInt(exhibitId), Integer.parseInt(museumId), description, startTime, endTime);
                this.myParent.eventList.add(newEvent);
            }
            Log.d("API", "List parsed. " + myParent.eventList.size() + " events.");
            this.myParent.eventListFinished = true;
        }
        catch (Exception e) {
            Log.d("Error", "Couldn't parse this list.");
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
                Log.d("Sort Exhibit", "SORTED!");
                gallery.addExhibit(exhibit);
            }
        }
    }
}