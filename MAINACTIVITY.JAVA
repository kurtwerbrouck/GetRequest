package com.mycompany.myapp2;

import android.app.*;
import android.os.*;
import org.apache.http.client.*;
import org.apache.http.impl.client.*;
import org.apache.http.client.methods.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.message.*;
import org.apache.http.client.entity.*;
import java.io.*;
import android.view.*;
import org.apache.http.conn.*;
import android.util.*;
import org.apache.http.util.*;
import android.widget.*;
import org.json.*;

public class MainActivity extends Activity 
{	TextView text;
	TextView tempdata;
	TextView citydata;
	TextView coorddata;
	TextView weatherdata;
	TextView humiditydata;
	TextView pressuredata;
	static String[] cities ={"2783089","2172797","7873004"};
	static String reply= null;
	int indexcities = 0;
	byte index = 0x00;
	
		
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Log.d("Info","OnCreate done");
		
		text = (TextView) findViewById(R.id.txt);
		tempdata = (TextView) findViewById(R.id.tempdata);
		citydata = (TextView) findViewById(R.id.citydata);
		coorddata = (TextView) findViewById(R.id.coorddata);
		weatherdata = (TextView) findViewById(R.id.weatherdata);
		humiditydata = (TextView) findViewById(R.id.humiditydata);
		pressuredata = (TextView) findViewById(R.id.pressuredata);
		
		Handler kurt = new Handler();
		kurt.postDelayed(run,5000);
		Handler kurt2 = new Handler();
		//kurt.postDelayed(run2,1000);
		
	}
	
	Runnable run = new Runnable() 
	{ 
		@Override public void run() 
		{ 
		Handler kurt = new Handler();
		
			MyAsynckTask runner = new MyAsynckTask();
			runner.execute(cities[indexcities]);
			if(runner.getStatus().equals(AsyncTask.Status.RUNNING))
				{text.setText("Async is running....");
			
				}
			indexcities++;
			indexcities=indexcities&1;
			Log.d("info","arrayinedx: "+indexcities);
		
			//while(!runner.getStatus().equals(AsyncTask.Status.FINISHED)) {}
			//text.setText("finished");
			kurt.postDelayed(this,5000);
			//doitagain();
			
		}
	};

	/*Runnable run2 = new Runnable()
	{

		@Override
		public void run()
		{
			// TODO: Implement this method
		}
	
	
	};*/
		
	public class MyAsynckTask extends AsyncTask<String,String,String>
	{
		@Override
		protected String doInBackground(String...params)
		{
			HttpClient httpclient = new DefaultHttpClient();
			Log.d("Info","httpclient");
			
			HttpGet httppost = new HttpGet("http://api.openweathermap.org/data/2.5/weather?id="+params[0]+"&APPID=134a9d51d97c4db2bbf5a4aa83b97c69");
			httppost.addHeader("User-Agent","USER-AGENT");
			Log.d("Info",httppost.toString());
						
			try
				{
					HttpResponse response = httpclient.execute(httppost);
					Log.d("Info","Post execute");
					HttpEntity entity = response.getEntity();
					if (entity != null) 
					{	reply=EntityUtils.toString(entity);
						Log.d("reply",reply);
						//Log.d("Get Response",EntityUtils.toString(entity));
		
					}	else { Log.d("Info","No info...");
							 }
					
											
				} catch (ClientProtocolException e) 
					{
						e.printStackTrace();
					}
					catch (IOException e) 
						{
							e.printStackTrace();
						}
			
			return "fff";
		}

		@Override
		protected void onPreExecute()
		{	
			text.setText("Send/Receive data....");
		}
		
		@Override
		protected void onPostExecute(String result)
		{	
			try
			{
				JSONObject response=new JSONObject(reply);
				JSONObject sys = response.getJSONObject("sys");
				JSONObject coord = response.getJSONObject("coord");
				JSONObject main=response.getJSONObject("main");
				JSONArray weather = response.getJSONArray("weather");
				JSONObject test = weather.getJSONObject(0);
								
				citydata.setText(response.getString("name")+", "+sys.getString("country"));
								
				tempdata.setText(Double.toString(main.getDouble("temp"))+" C");//incorrect value
				
				coorddata.setText("Lon: "+ Double.toString(coord.getDouble("lon"))+" , Lat: "+Double.toString(coord.getDouble("lat")));
								
				weatherdata.setText(test.getString("main")+" , "+test.getString("description"));
				
				humiditydata.setText(Double.toString(main.getDouble("humidity")));
				
				pressuredata.setText(Double.toString(main.getDouble("pressure")));
				
			}
			
			catch (JSONException e)
			{}
			text.setText("Data Received.....");
			
		}

		@Override
		protected void onProgressUpdate(String[] values)
		{
			
		}

	}

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}
	
   
}
