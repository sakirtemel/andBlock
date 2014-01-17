package com.github.sakirtemel.andblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import org.apache.http.entity.StringEntity;

public class DatabaseUpdater {
	public String last_updated = "";
	public boolean isUpdating = false;
	private static Context context;
	
	public static String push_number = "";
	public static String push_message = "";
	
	public DatabaseUpdater(Context context){
		this.context = context;
	}
	public void update(){
		if(isUpdating)
			return;
		
		isUpdating = true;
		 new HttpAsyncTask().execute("");
	}

	public static void pushToParse(){
		
		if(push_number == "")
			return;
		
        HttpClient httpclient = new DefaultHttpClient();
        
        
        
        HttpPost request = new HttpPost("https://api.parse.com/1/batch");
        request.setHeader("X-Parse-Application-Id", "jxyTPWXVjaqdyP0deIDo8OKw7bHbuQ6i52dvX9po");
        request.setHeader("X-Parse-REST-API-Key", "cWfq2JukdBqBxo4a4m8jclKiQsEgFwQEfa3ZVXxN");
        String data_s = "";
        
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        
        data_s = " \"number\" : \"" + push_number + "\", \"content\" : \"" + push_message + "\" ";
        
        String requests = "{\"requests\" : [{  \"method\" : \"POST\", \"path\" : \"/1/classes/blocked_request\", \"body\" : {" + data_s +  "}     }]}";
        
        System.out.println(requests);
        
        //List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
        //nameValuePairs.add(new BasicNameValuePair("requests", requests));
        try {
			//request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	request.setEntity( new StringEntity(requests) );
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        

        
        
        try {
			HttpResponse httpResponse = httpclient.execute(request);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        

		push_number = "";
		push_message = "";
	}
	
	public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {
        	pushToParse();
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
