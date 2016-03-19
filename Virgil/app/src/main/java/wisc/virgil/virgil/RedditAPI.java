package aperture.axon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Object that interacts with the Reddit API. Extends AxonAPI for more functionality.
 * 
 * @author Victor Ferrero
 */
public class RedditAPI /*extends AxonAPI */ {

	// no data members, all methods just return a result

	/**
	 * Constructor doesnt do anything
	 */
	public RedditAPI(){

	}

	/** 
	 * Makes a call to reddit to get a list of subreddits. This is used for populating the CategoryMenu
	 * 
	 * @param limit number of subreddits to get
	 * @param after the redditID of subreddit that we want to start getting articles after
	 * @return Subreddit[] array of Subreddit objects. Only thing set is the ID and the reddit Title
	 * @throws APICallFailureException
	 * @throws JSONException 
	 */
	public Subreddit[] getSubreddits
	(int limit, String after) throws APICallFailureException, JSONException{
		Subreddit[] subreddits = new Subreddit[limit];
		// setup the String with request for the server
		String formattedUrl = ""; 
		if(limit > 0) {
			formattedUrl = "http://www.reddit.com/subreddits.json?limit=" + limit + "&&after=" + after;
		}
		else {
			formattedUrl = "http://www.reddit.com/subreddits.json";
		}
		JSONObject j1 = this.getJsonObject(formattedUrl);
		// go through the JSON object and extract things
		int count = 0;
		try {
			while(true) { // keep going till we trigger an exception. 
				JSONArray children = j1.getJSONObject("data").getJSONArray("children");
				String subredditTitle = children.getJSONObject(count).getJSONObject("data").getString("display_name");
				String ID = children.getJSONObject(count).getJSONObject("data").getString("name");
				Subreddit r1 = new Subreddit(subredditTitle, ID); // only creating subreddit with the ID
				subreddits[count] = r1;
				count++;
			}
		} catch(Exception e) {
			// break out of the loop.
		}
		return subreddits;
	}

	/**
	 * Makes a call to reddit API to do a general search. 
	 * 
	 * @param searchQuery the general query for reddit
	 * @param sort url param for how to sort the query
	 * @param after the redditID of the article to start at
	 * @return Article[] array of articles
	 * @throws APICallFailureException
	 * @throws JSONException 
	 */
	public Article[] getArticlesForGeneralSearch
	(String searchQuery, String sort, String after, AxonRedditBrowser browser) throws APICallFailureException, JSONException {
		AxonRedditBrowserSettings settings = browser.getSettings();
		Article[] articles = new Article[settings.maxNumberOfDisplayedArticles];
		String formattedURL =  "http://www.reddit.com/search.json?q=" + searchQuery + "&&sort=" + sort + "&&after=" + after;
		JSONObject j1 = this.getJsonObject(formattedURL);

		// go through the JSON object and extract things
		for(int i = 0; i < settings.maxNumberOfDisplayedArticles; i++) {
			JSONArray children = j1.getJSONObject("data").getJSONArray("children");
			String articleURL = children.getJSONObject(i).getJSONObject("data").getString("url");
			String title = children.getJSONObject(i).getJSONObject("data").getString("title");
			String redditID = children.getJSONObject(i).getJSONObject("data").getString("name");
			String upVotes = String.valueOf(children.getJSONObject(i).getJSONObject("data").getInt("ups"));
			String downVotes = String.valueOf(children.getJSONObject(i).getJSONObject("data").getInt("downs"));
			int creationTime = children.getJSONObject(i).getJSONObject("data").getInt("created_utc");
			String numComments = String.valueOf(children.getJSONObject(i).getJSONObject("data").getInt("num_comments"));
			String thumbnailSrc = "";
			// some articles do not have a thumbnail.
			try {
				thumbnailSrc = children.getJSONObject(i).getJSONObject("data").getString("thumbnail");
			} catch(Exception e) {
				thumbnailSrc = "";
			}
			// note the use of GENERAL_SEARCH here to denote the fact this article is from a search and is not
			// currently being associated with a subreddit
			// TODO: might have to change this
			String category = "GENERAL_SEARCH";
			Article a1 = new Article(category, title, upVotes, downVotes, creationTime,
					numComments, thumbnailSrc, articleURL, redditID);
			articles[i] = a1;
		}
		return articles;
	}


	/**
	 * Retrieves articles from the front page of reddit.
	 *  
	 * @param searchFilter
	 * @param after
	 * @param before
	 * @return an array of articles from the reddit front page
	 * @throws APICallFailureException
	 * @throws JSONException
	 */
	public Article[] getArticlesFromRedditFrontPage
	(String searchFilter, String after, String before, AxonRedditBrowser browser)
			throws APICallFailureException, JSONException {
		AxonRedditBrowserSettings settings = browser.getSettings();
		Article[] articles;
		ArrayList<Article> tempList = new ArrayList<Article>();
		String formattedURL = "";
		if(searchFilter.equals("")) {
			formattedURL = "http://www.reddit.com/.json?limit=" + settings.maxNumberOfDisplayedArticles + "&&after=" + after + "&&before=" + before;
		}
		else {
			formattedURL = "http://www.reddit.com/" + searchFilter + ".json?limit=" + settings.maxNumberOfDisplayedArticles + "&&after=" + after + "&&before=" + before;
		}

		JSONObject j1 = this.getJsonObject(formattedURL);

		JSONArray children = j1.getJSONObject("data").getJSONArray("children");
		// go through the JSON object and extract things
		try {
			for(int i = 0; i < settings.maxNumberOfDisplayedArticles; i++) {
				String articleURL = children.getJSONObject(i).getJSONObject("data").getString("url");
				String title = children.getJSONObject(i).getJSONObject("data").getString("title");
				String redditID = children.getJSONObject(i).getJSONObject("data").getString("name");
				int upVotess = (children.getJSONObject(i).getJSONObject("data").getInt("ups"));
				String upVotes = String.valueOf(upVotess);
				int downVotess  = (children.getJSONObject(i).getJSONObject("data").getInt("downs"));
				String downVotes = String.valueOf(downVotess);
				int creationTime = children.getJSONObject(i).getJSONObject("data").getInt("created_utc");
				int numCom = children.getJSONObject(i).getJSONObject("data").getInt("num_comments");
				String numComments = String.valueOf(numCom);
				String thumbnailSrc = "";
				try {
					thumbnailSrc = children.getJSONObject(i).getJSONObject("data").getString("thumbnail");
				} catch(Exception e) { // some articles do not have thumbnails
					thumbnailSrc = "";
					e.printStackTrace();
				}
				String category = "REDDIT_FRONT_PAGE";
				Article a1 = new Article(category, title, upVotes, downVotes, creationTime, numComments, thumbnailSrc, articleURL, redditID);
				tempList.add(a1);
			}
		} catch(JSONException e) {

		}

		articles = new Article[tempList.size()];
		for(int i = 0; i < articles.length; i++) {
			articles[i] = tempList.get(i);
		}
		return articles;
	}

	/**
	 * Makes an API call to figure out what the subreddit ID is for a given subreddit.
	 * 
	 * @param subreddit the name of subreddit to get the ID for
	 * @return the Subreddit ID
	 * @throws JSONException
	 * @throws APICallFailureException
	 */
	public String getSubredditID(String subreddit) throws JSONException, APICallFailureException {
		String formattedURL = "http://www.reddit.com/r/" + subreddit  + "/.json?limit=1";
		JSONObject j1 = this.getJsonObject(formattedURL);
		JSONArray children = j1.getJSONObject("data").getJSONArray("children");
		String redditID = children.getJSONObject(0).getJSONObject("data").getString("subreddit_id");
		return redditID;
	}
	
	/**
	 * Searches for a set of articles that are within a specific subreddit
	 * 
	 * @param category the category of the subreddit. Must contain /r/SUBREDDIT
	 * @param tabbedSearchTerm this is a filter for the search
	 * @param after the redditID to start get articles after
	 * @return Article[] an array of Articles from the desired subreddit
	 * @throws APICallFailureException
	 * @throws JSONException 
	 */
	public Article[] getArticlesFromSubreddit
	(String category, String searchFilter, String after, String before, AxonRedditBrowser browser) throws APICallFailureException, JSONException {
		AxonRedditBrowserSettings settings = browser.getSettings();
		Article[] articles;
		ArrayList<Article> tempList = new ArrayList<Article>();
		String formattedURL = "";
		if(searchFilter.equals("")) {
			formattedURL = "http://www.reddit.com/r/" + category + "/.json?limit=" + settings.maxNumberOfDisplayedArticles + "&&after=" + after + "&&before=" + before;
		}
		else
		{
			formattedURL = "http://www.reddit.com/r/" + category + "/" + searchFilter + "/.json?limit=" + settings.maxNumberOfDisplayedArticles + "&&after=" + after + "&&before=" + before;
		}
		JSONObject j1 = this.getJsonObject(formattedURL);

		JSONArray children = j1.getJSONObject("data").getJSONArray("children");
		// go through the JSON object and extract things
		try {
			for(int i = 0; i < settings.maxNumberOfDisplayedArticles; i++) {
				String articleURL = children.getJSONObject(i).getJSONObject("data").getString("url");
				String title = children.getJSONObject(i).getJSONObject("data").getString("title");
				String redditID = children.getJSONObject(i).getJSONObject("data").getString("name");
				int upVotess = (children.getJSONObject(i).getJSONObject("data").getInt("ups"));
				String upVotes = String.valueOf(upVotess);
				int downVotess  = (children.getJSONObject(i).getJSONObject("data").getInt("downs"));
				String downVotes = String.valueOf(downVotess);
				int creationTime = children.getJSONObject(i).getJSONObject("data").getInt("created_utc");
				int numCom = children.getJSONObject(i).getJSONObject("data").getInt("num_comments");
				String numComments = String.valueOf(numCom);
				String thumbnailSrc = "";
				try {
					thumbnailSrc = children.getJSONObject(i).getJSONObject("data").getString("thumbnail");
				} catch(Exception e) { // some articles do not have thumbnails
					thumbnailSrc = "";
					e.printStackTrace();
				}
				Article a1 = new Article(category, title, upVotes, downVotes, creationTime, numComments, thumbnailSrc, articleURL, redditID);
				tempList.add(a1);
			}
		} catch(JSONException e) {

		}

		// copy articles from arraylist into an array.
		articles = new Article[tempList.size()];
		for(int i = 0; i < articles.length; i++) {
			articles[i] = tempList.get(i);
		}
		return articles;
	}


	// Not working. Need Oauth???
	/*	public void login
	(String password, boolean rem, String user) throws APICallFailureException{
		String url = "www.reddit.com/api/login/";
		String USER_AGENT = "Mozilla/5.0";
		HttpsURLConnection con = null;
		//add reuqest header
		try {
			URL obj = new URL(url);
			con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			String urlParameters = "api_type=json&passwd=" + password + "&rem=" + rem + "&user=" + user;

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			//print result
			System.out.println(response.toString());
		} catch(ProtocolException x) {
			throw new APICallFailureException("ProtocolException: " + x.getMessage());
		} 
		catch(MalformedURLException e) {
			throw new APICallFailureException("MalformedURLException: " + e.getMessage());
		} 
		catch(IOException e) {
			throw new APICallFailureException("IOException: " + e.getMessage());
		}
		finally {
			if(con != null){
				con.disconnect();
			}
		}
	}
	 */	
	/**
	 * Private helper method for just getting a JSON object. The public methods send a formated URL 
	 * to this method.
	 * 
	 * @param formattedURL properly formatted URL for making call to Reddit API
	 * @return
	 * @throws APICallFailureException 
	 * @throws JSONException 
	 */
	private JSONObject getJsonObject
	(String formattedURL) throws APICallFailureException, JSONException {
		HttpURLConnection con = null;
		BufferedReader in = null;
		try {
			URL obj = new URL(formattedURL);
			con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
			//add request header
			String USER_AGENT = "Mozilla/5.0";
			con.setRequestProperty("User-Agent", USER_AGENT);
			//	int responseCode = con.getResponseCode();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			String jsonString = response.toString();
			JSONObject j1 = new JSONObject(jsonString);
			return j1;
		}
		// wrap all these exceptions into APICallFailure Exception
		catch(ProtocolException x) {
			throw new APICallFailureException("ProtocolException: " + x.getMessage());
		} 
		catch(MalformedURLException e) {
			throw new APICallFailureException("MalformedURLException: " + e.getMessage());
		} 
		catch(IOException e) {
			throw new APICallFailureException("IOException: " + e.getMessage());
		}
		finally {
			if(con != null){
				con.disconnect();
			}
		}
	}
}