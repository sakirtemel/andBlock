package com.github.sakirtemel.andblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	public ListAdapter adapter;
	private static final String TAG_NUMBER= "number1";
	private static final String TAG_TIME = "time1";
	private static final String TAG_MESSAGE = "message1";
	ListView list;
	TextView number;
	TextView time;
	TextView message;
	Button blocked;
	Button unblocked;
	ImageView image1;
	ImageView image2;
	ImageView imageListview;
	DatabaseUpdater updater;
	
	ArrayList<HashMap<String, String>> messagelist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		updater = new DatabaseUpdater(this);
		updater.update();
		
		final DatabaseHelper db = DatabaseHelper.getInstance(this);
		 
        /**
         * CRUD Operations
         * */
		

		
		messagelist = new ArrayList<HashMap<String, String>>();
		
		 number = (TextView)findViewById(R.id.vers);
		 time = (TextView)findViewById(R.id.name);
		 message = (TextView)findViewById(R.id.api);
		 blocked = (Button)findViewById(R.id.btnBlocked);
		 unblocked = (Button)findViewById(R.id.btnUnblocked);
		 image1 = (ImageView)findViewById(R.id.imageView3);
		 image2 = (ImageView)findViewById(R.id.imageView4);
		 imageListview = (ImageView)findViewById(R.id.img);
		 
		 blocked.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	List<Message> data = db.getAllBlockedMessages();
			    	image1.setVisibility(View.VISIBLE);
			    	image2.setVisibility(View.INVISIBLE);
			    	//String aStooges[] = {"05352372123", "11:22", "naber?","02134321", "d�n", "jj�dwql�sna�jbwqjkbdsa"};
			    	list1(data);
			    }
			});
		 
		 unblocked.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	image2.setVisibility(View.VISIBLE);
			    	image1.setVisibility(View.INVISIBLE);
			    	/*String aStooges[] = {"blocklanmam�s", "11:22", "naber?"}; 
			    	list1(aStooges);*/
			    	List<Message> data = getUnblockedMessages();
			    	list1(data);
			    }
			});
			
		
	}
	
	
	public List<Message> getUnblockedMessages() {
   	 
    	List<Message> dataList = new ArrayList<Message>();
    	
    	
    	
    	
    	Uri uri = Uri.parse("content://sms/inbox");
        Cursor c = getContentResolver().query(uri, null, null ,null,null);
        startManagingCursor(c);
         
        // Read the sms data and store it in the list
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
            	
            	Message data = new Message();
	           	data.setNumber( c.getString(c.getColumnIndexOrThrow("address")).toString() );
	           	data.setMessage(  c.getString(c.getColumnIndexOrThrow("body")).toString() );
	           	data.setDate(  c.getString(c.getColumnIndexOrThrow("date")).toString() );
	           	dataList.add(data);
                 
                c.moveToNext();
            }
        }
        c.close();
 
        return dataList;
    }
	
	public void list1(List<Message> aStooges)
	{
		messagelist.clear();
		/*for(int i = 0; i < aStooges.length; i++){
			
			String number1 = aStooges[i].toString();
			String time1 = aStooges[i+1].toString();
			String message1 = aStooges[i+2].toString();
			i = i + 2;
			
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TAG_NUMBER, number1);
		map.put(TAG_TIME, time1);
		map.put(TAG_MESSAGE, message1);
		
		messagelist.add(map);*/
		
		for (Message val :  aStooges) {

            // Writing values to map
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TAG_NUMBER,val.getNumber());
        map.put(TAG_MESSAGE, val.getMessage());
        map.put(TAG_TIME, val.getDate());
        messagelist.add(map);        
		}
		
		list=(ListView)findViewById(R.id.list);
		registerForContextMenu(list);
		adapter = new SimpleAdapter(MainActivity.this, messagelist,
				R.layout.list_v,
				new String[] { TAG_NUMBER,TAG_TIME, TAG_MESSAGE }, new int[] {
						R.id.vers,R.id.name, R.id.api});
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at "+messagelist.get(+position).get("number1"), Toast.LENGTH_SHORT).show();

            }
        });
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	      super.onCreateContextMenu(menu, v, menuInfo);
	      if (v.getId()==R.id.list) {
	          MenuInflater inflater = getMenuInflater();
	          inflater.inflate(R.menu.menu_list, menu);
	          
	      }
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	      int position = info.position; // listview item Position
	      View wantedView = list.getChildAt(position);
	      TextView text = (TextView) wantedView.findViewById(R.id.vers);
	      String Number4 = (String)text.getText();
	      switch(item.getItemId()) {
	         case R.id.block:
	         // add stuff here	
	        	blockMessage( messagelist.get(+position).get("number1"), messagelist.get(+position).get("message1") );
	        	return true;

	          default:
	                return super.onContextItemSelected(item);
	      }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void blockMessage(String number, String message){
		updater.push_number = number;
		updater.push_message = message;
		updater.update();
		
        Toast.makeText(MainActivity.this, "You Clicked at " + number + ",," + message, Toast.LENGTH_SHORT).show();
	}

}
