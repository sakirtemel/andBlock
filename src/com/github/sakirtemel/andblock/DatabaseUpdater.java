package com.github.sakirtemel.andblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;

public class DatabaseUpdater {
	public String last_updated = "";
	public boolean isUpdating = false;
	private static Context context;
	
	
	public DatabaseUpdater(Context context){
		this.context = context;
	}
	public void update(){
		if(isUpdating)
			return;
		
		isUpdating = true;
		 new HttpAsyncTask().execute("");
	}

	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
 
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            String last_updated = "2011-08-21T18:02:52.249Z";
            
            DatabaseHelper db = DatabaseHelper.getInstance(context);
			
            
            last_updated = db.getLastUpdatedDate();
            Log.d("last_update", last_updated);
            String paramString = URLEncoder.encode("where={\"createdAt\":{\"$gt\":{\"__type\":\"Date\",\"iso\":\"" + last_updated + "\"}}}", "utf-8");
            
            HttpGet request = new HttpGet("https://api.parse.com/1/classes/blocked_number?" + paramString);
            Log.d("URLLLL", "https://api.parse.com/1/classes/blocked_number?" + paramString);
            request.setHeader("X-Parse-Application-Id", "jxyTPWXVjaqdyP0deIDo8OKw7bHbuQ6i52dvX9po");
            request.setHeader("X-Parse-REST-API-Key", "cWfq2JukdBqBxo4a4m8jclKiQsEgFwQEfa3ZVXxN");
            
            HttpResponse httpResponse = httpclient.execute(request);
 
            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
 
            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
 
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
 
        return result;
    }
 
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
 
    public boolean isConnected(){
       /* ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else*/
                return false;   
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String[] urls) {
 
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	if (result == "")
        		return;
        	Log.d("jresult", result);

        	JSONObject json;
			try {
				json = new JSONObject(result);

				JSONArray results = json.getJSONArray("results");
				
				String numbers[] = new String[results.length()];
				for(int i=0;i<results.length();i++){
					numbers[i] = results.getJSONObject(i).getString("number_hash");
					
				}
				
				String lastRecord = results.getJSONObject(results.length()-1).getString("createdAt");
				
				DatabaseHelper db = DatabaseHelper.getInstance(context);
				db.updateDatabase(numbers);
				Log.d("lastRecord", lastRecord);
				db.setLastUpdateDate(lastRecord);
				Log.d("laaast", db.getLastUpdatedDate());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
        	
        	/* TODO: last updated date, from the last record */
            isUpdating = false;
            
       }
    }
	
}
