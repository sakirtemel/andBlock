package com.github.sakirtemel.andblock;

import java.util.ArrayList;
import java.util.HashMap;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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
	
	ArrayList<HashMap<String, String>> messagelist = new ArrayList<HashMap<String, String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		messagelist = new ArrayList<HashMap<String, String>>();
		
		 number = (TextView)findViewById(R.id.vers);
		 time = (TextView)findViewById(R.id.name);
		 message = (TextView)findViewById(R.id.api);
		
	
		
		String aStooges[] = {"05352372123", "11:22", "naber?","02134321", "dün", "jjýdwqlýsnaöjbwqjkbdsa"};
		
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
		
		ListAdapter adapter = new SimpleAdapter(MainActivity.this, messagelist,
				R.layout.list_v,
				new String[] { TAG_NUMBER,TAG_TIME, TAG_MESSAGE }, new int[] {
						R.id.vers,R.id.name, R.id.api});
		
		list.setAdapter(adapter);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
