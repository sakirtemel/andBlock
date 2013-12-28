package com.github.sakirtemel.andblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	
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
	
	ArrayList<HashMap<String, String>> messagelist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		new DatabaseUpdater(this).update();
		DatabaseHelper db = DatabaseHelper.getInstance(this);
		 
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
		 
		 blocked.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	image1.setVisibility(View.VISIBLE);
			    	image2.setVisibility(View.INVISIBLE);
			    	String aStooges[] = {"05352372123", "11:22", "naber?","02134321", "dün", "jjýdwqlýsnaöjbwqjkbdsa"};
			    	list1(aStooges);
			    }
			});
		 
		 unblocked.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View v) {
			    	image2.setVisibility(View.VISIBLE);
			    	image1.setVisibility(View.INVISIBLE);
			    	String aStooges[] = {"blocklanmamýs", "11:22", "naber?"}; 
			    	list1(aStooges);
			    }
			});
			
		
	}
	
	public void list1(String aStooges[])
	{
		messagelist.clear();
		for(int i = 0; i < aStooges.length; i++){
			
			String number1 = aStooges[i].toString();
			String time1 = aStooges[i+1].toString();
			String message1 = aStooges[i+2].toString();
			i = i + 2;
			
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TAG_NUMBER, number1);
		map.put(TAG_TIME, time1);
		map.put(TAG_MESSAGE, message1);
		
		messagelist.add(map);
		list=(ListView)findViewById(R.id.list);
		registerForContextMenu(list);
		ListAdapter adapter = new SimpleAdapter(MainActivity.this, messagelist,
				R.layout.list_v,
				new String[] { TAG_NUMBER,TAG_TIME, TAG_MESSAGE }, new int[] {
						R.id.vers,R.id.name, R.id.api});
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at "+messagelist.get(+position).get("number1"), Toast.LENGTH_SHORT).show();

            }
        });
		}
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
	      switch(item.getItemId()) {
	         case R.id.add:
	         // add stuff here	        	 
	            return true;
	          case R.id.edit:
	            // edit stuff here
	                return true;
	          case R.id.delete:
	        // remove stuff here
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

}
