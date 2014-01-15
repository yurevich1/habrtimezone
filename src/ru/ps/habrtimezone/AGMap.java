package ru.ps.habrtimezone;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.internal.cc;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AGMap extends FragmentActivity implements OnCameraChangeListener, OnClickListener{
	LatLng HEREIAM = null;
	private GoogleMap mMap;
	LatLng pos = null;
	GoogleMapOptions options = new GoogleMapOptions();
	MarkerOptions mo,mo1;
	Marker mar,mar1;
	
	double plat = 0., plon = 0.,platcur = 0., ploncur = 0.;
	boolean cur = true;
	Button btnbck,btnSave;
	Random randomGenerator;
	ImageView background;
	String currentname = "";
	GeoCoderTask geocodertask = new  GeoCoderTask(this);
	TimeZoneTask timezonetask = new TimeZoneTask(this);
	private boolean finishwithsave = false;
	Intent intent = new Intent();
	public Random r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    
    	r = new Random(System.currentTimeMillis());
     	intent = getIntent();
    	
    	if(intent.hasExtra(_.LATGMAP)){
    		plat = intent.getDoubleExtra(_.LATGMAP, 0.); 
        }
    	if(intent.hasExtra(_.LONGMAP)){
    		plon = intent.getDoubleExtra(_.LONGMAP, 0.); 
        }
    	if(intent.hasExtra(_.LATGMAPCUR)){
    		platcur = intent.getDoubleExtra(_.LATGMAPCUR, 0.); 
        }
    	if(intent.hasExtra(_.LONGMAPCUR)){
    		ploncur = intent.getDoubleExtra(_.LONGMAPCUR, 0.); 
        }
    	if(intent.hasExtra(_.CURORMAN)){
    		cur = intent.getBooleanExtra(_.CURORMAN, cur); 
        }
    	
		HEREIAM = new LatLng(platcur, ploncur);
		pos =  new LatLng(plat, plon);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agmap);
        
		try
		{
	        setUpMap();
	        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	        mMap.isMyLocationEnabled();
	        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
	        .compassEnabled(false)
	        .rotateGesturesEnabled(false)
	        .tiltGesturesEnabled(false);

	        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((cur)?HEREIAM:pos, 12));
	        mMap.setOnCameraChangeListener(this);
	        mo = new MarkerOptions();
	        mo1 = new MarkerOptions();
	        mo.position(HEREIAM).title(getResources().getString(R.string.YouAreHere)).snippet("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
	        mo1.position(pos).title(getResources().getString(R.string.ThisPlace)).snippet(getResources().getString(R.string.ThisPlaceDesc)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	        
	        mar = mMap.addMarker(mo);
	        mar1 = mMap.addMarker(mo1);
	        
	        btnbck  = (Button) findViewById(R.id.btnbck);
	        btnbck.setOnClickListener(this);
	        btnSave  = (Button) findViewById(R.id.btnSave);
			btnSave.setOnClickListener(this);

		}
		catch(NullPointerException e)
		{
			Toast.makeText(this, "ERROR! Write to developer, please.", Toast.LENGTH_LONG).show();
			finishAct(true, TimeZone.getDefault().getID());
		}
    }


	@Override
	public void onCameraChange(CameraPosition cp) {
		// TODO Auto-generated method stub
		  pos = cp.target;
		  mar1.setPosition(pos);
		//  uniqueExec();
		// uniqueExecTZ(pos);
	
	}
	private void uniqueExecTZ(LatLng pos2) {
		// TODO Auto-generated method stub
		if(timezonetask.getStatus() == AsyncTask.Status.PENDING){
		    // My AsyncTask has not started yet
			timezonetask.execute(pos2.latitude,pos2.longitude);
			return;
		}

		if(timezonetask.getStatus() == AsyncTask.Status.RUNNING){
		    // My AsyncTask is currently doing work in doInBackground()
		}

		if(timezonetask.getStatus() == AsyncTask.Status.FINISHED){
		    // My AsyncTask is done and onPostExecute was called
			timezonetask = new TimeZoneTask(this);
			timezonetask.execute(pos2.latitude,pos2.longitude);
			return;
		}
	}
	public String ConvertPointToLocation(LatLng point) 
    {
        String address = "";
        Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geoCoder.getFromLocation(
                point.latitude,
                point.longitude, 1);

            if (addresses.size() > 0) {
                for (int index = 0; index < addresses.get(0).getMaxAddressLineIndex(); index++)
                {
                	address += addresses.get(0).getAddressLine(index);
                	if (index !=  addresses.get(0).getMaxAddressLineIndex() - 1) address +=", ";
                }
                
            }

        }
        catch (IOException e) {                
            e.printStackTrace();
        }   

        return address;
    } 

    private void setUpMap() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
        	 mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId())
		{
		case R.id.btnbck:
			setResult(RESULT_CANCELED);
			finish();
		break;
		case R.id.btnSave:
			
			finishwithsave  = true;
			uniqueExec();
			uniqueExecTZ(pos);

		break;
		default:
		break;
		}
	}
	public void uniqueExec()
	{
		
		
		if(geocodertask.getStatus() == AsyncTask.Status.PENDING){
		    // My AsyncTask has not started yet
			geocodertask.execute();
			return;
		}

		if(geocodertask.getStatus() == AsyncTask.Status.RUNNING){
		    // My AsyncTask is currently doing work in doInBackground()
		}

		if(geocodertask.getStatus() == AsyncTask.Status.FINISHED){
		    // My AsyncTask is done and onPostExecute was called
			geocodertask = new GeoCoderTask(this);
			geocodertask.execute();
			return;
		}
	}
	public class GeoCoderTask extends AsyncTask<Void, Integer, Void>
	{
		private Context с;

		public GeoCoderTask(Context context) {
	        this.с = context;
	    }
		@Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
			
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			currentname = ConvertPointToLocation(pos);
			
			runOnUiThread(new Runnable() {
			    public void run() {
			    	Toast.makeText(getApplicationContext(), currentname, Toast.LENGTH_LONG).show();
			    }
			});
			return null;
		}

		 @Override
		 protected void onProgressUpdate(Integer... values) {
		  // TODO Auto-generated method stub
		 }
	
		 @Override
		 protected void onPostExecute(Void result) {
		  // TODO Auto-generated method stub

		 }
		 
	
	}
	public class TimeZoneTask extends AsyncTask<Double, Integer, Void>
	{
		private Context с;
		Socket socket = null;
	    boolean reachable = false;
	    boolean reachable_head = false;
	    int responsecode = -1;
	    int pingtime = -1;
	    int pingtime_head = -1;
	    //String[] urlString = {"http://api.geonames.org/findNearbyJSON?lat=","&lng=" ,"&radius=50&username=horoscope&style=full&maxRows=1"};
	    String[] urlString = {"http://api.geonames.org/findNearbyJSON?lat=","&lng=", "&radius=50&username=","&style=full&maxRows=1"};
	    //http://api.geonames.org/findNearbyJSON?username=horoscope&lat=55.55&lng=38.01&radius=50&style=full&maxRows=1
	    String json = "";
		private JSONObject jObj;
		//_Info city = new _Info();
		TimeZone cityTZ = TimeZone.getDefault();
		boolean errorTZ = false;
		public TimeZoneTask(Context context) {
	        this.с = context;
	    }
		@Override
		protected void onPreExecute() {
		// TODO Auto-generated method stub
			
		}
		
		@Override
		protected Void doInBackground(Double... ii) {
			// TODO Auto-generated method stub

	         findtimezone(ii[0],ii[1]);
			runOnUiThread(new Runnable() {
			    public void run() {
			    //	Toast.makeText(getApplicationContext(), currentname, Toast.LENGTH_LONG).show();
			    }
			});
			return null;
		}

		 @Override
		 protected void onProgressUpdate(Integer... values) {
		  // TODO Auto-generated method stub
		 }
	
		 @Override
		 protected void onPostExecute(Void result) {
		  // TODO Auto-generated method stub
			 if (finishwithsave) finishAct(errorTZ, cityTZ.getID());
		 }
		 private void findtimezone(double lat, double lon)
			{
			 DecimalFormat f = new DecimalFormat("0.000000");
			 String outURL = null;
			outURL = urlString[0] +  f.format(lat).replace(",", ".") + urlString[1] + f.format(lon).replace(",", ".") + urlString[2] + _.names1[r.nextInt(_.names1.length)] + urlString[3];
			 URL inURL = null;
			 
		    	try {
					inURL = new URL(outURL);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return ;
				}

		    	long startTime = System.currentTimeMillis();
		        reachable = pinging_port_80(outURL);
		        long endTime = System.currentTimeMillis();

		        startTime = System.currentTimeMillis();
				reachable_head = pinging_(outURL,80);
				endTime = System.currentTimeMillis();

				if (!(reachable || reachable_head)) return;
				json = GoToURL(outURL, 20000, "");
				Log.d("JSONTIMEZONE",json);
				try {
		            jObj = new JSONObject(json);
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
				JSONArray gNames = new JSONArray();
				try {
					gNames = jObj.getJSONArray("geonames");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (gNames.length() > 0)
				{
					
				}
				
				for (int i = 0; i < gNames.length(); i++)
				{
					JSONObject c = new JSONObject();
					JSONObject tz = new JSONObject();


					String tzStr = "GMT+00:00";
					try {
						c = gNames.getJSONObject(i);

							tz = c.getJSONObject("timezone");
							tzStr = tz.getString("timeZoneId");
							
							

				    		 cityTZ = TimeZone.getTimeZone(tzStr);

						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				if (gNames.length() > 0)
				{
				  runOnUiThread(new Runnable() {
					    public void run() {

					    	Toast.makeText(getApplicationContext(), cityTZ.getID() +  ": ready!", Toast.LENGTH_LONG).show();
					    }
					});
				}
				else
				{

					String tzApprox = "GMT+00:00";
					lon = normalize_angle(lon, true);
					if (lon < 7.5)
					{
						tzApprox = "GMT+00:00";
					}
					else if (lon < 22.5)
					{
						tzApprox = "GMT+01:00";
					}
					else if (lon < 37.5)
					{
						tzApprox = "GMT+02:00";
					}
					else if (lon < 52.5)
					{
						tzApprox = "GMT+03:00";
					}
					else if (lon < 67.5)
					{
						tzApprox = "GMT+04:00";
					}
					else if (lon < 82.5)
					{
						tzApprox = "GMT+05:00";
					}
					else if (lon < 97.5)
					{
						tzApprox = "GMT+06:00";
					}
					else if (lon < 112.5)
					{
						tzApprox = "GMT+07:00";
					}
					else if (lon < 127.5)
					{
						tzApprox = "GMT+08:00";
					}
					else if (lon < 142.5)
					{
						tzApprox = "GMT+09:00";
					}
					else if (lon < 157.5)
					{
						tzApprox = "GMT+10:00";
					}
					else if (lon < 172.5)
					{
						tzApprox = "GMT+11:00";
					}
					else if (lon < 180.0)
					{
						tzApprox = "GMT+12:00";
					}
					else if (lon < 187.5)
					{
						tzApprox = "GMT-12:00";
					}
					else if (lon < 202.5)
					{
						tzApprox = "GMT-11:00";
					}
					else if (lon < 217.5)
					{
						tzApprox = "GMT-10:00";
					}
					else if (lon < 232.5)
					{
						tzApprox = "GMT-09:00";
					}
					else if (lon < 247.5)
					{
						tzApprox = "GMT-08:00";
					}
					else if (lon < 262.5)
					{
						tzApprox = "GMT-07:00";
					}
					else if (lon < 277.5)
					{
						tzApprox = "GMT-06:00";
					}
					else if (lon < 292.5)
					{
						tzApprox = "GMT-05:00";
					}
					else if (lon < 307.5)
					{
						tzApprox = "GMT-04:00";
					}
					else if (lon < 322.5)
					{
						tzApprox = "GMT-03:00";
					}
					else if (lon < 337.5)
					{
						tzApprox = "GMT-02:00";
					}
					else if (lon < 352.5)
					{
						tzApprox = "GMT-01:00";
					}
					else
					{
						tzApprox = "GMT+00:00";
					}
					cityTZ = TimeZone.getTimeZone(tzApprox);
				}
			}
			public double normalize_angle(double diff,boolean onlyPositive) {
				// TODO Auto-generated method stub
				 diff = ((diff)%360.);
				 diff = (diff < 0)? (diff + 360.): diff;
				 if (onlyPositive)
				 return diff;
				 else
					 if (diff > 180 - 0.00000001)
						 return diff - 360.;
					 else
						 return diff;
							 
			}
			protected String GoToURL(String url, int timeout, String request)
			{
		        //	boolean connected = false;
		        	String outtext1="";
		            url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
		           

		            HttpURLConnection urlConnection = null;
		            	try
		            	{
		            		urlConnection = (HttpURLConnection) new URL(url).openConnection();
		            		urlConnection.setDoOutput(true);
		            		urlConnection.setDoInput(true);
		            		PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
		        			out.println(request);
		        			out.close();
		        			
		            		InputStream tmptmp = urlConnection.getInputStream();
		            		InputStream in = new BufferedInputStream(tmptmp);
		                	outtext1 = IStoString(in);
		            	}
		            	catch (MalformedURLException e)
		    			{
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    			catch (IOException e)
		    			{
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		            	finally
		            	{
		            		//outstr = urlConnection.getErrorStream().toString();
		            		urlConnection.disconnect();
		            	}
		            return outtext1;
		        }
			public String IStoString(InputStream in)
		    {
		    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
				StringBuilder sb = new StringBuilder();
				String line;
		    	try {
					while ((line = br.readLine()) != null) {
						sb.append(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	return sb.toString();
		    }
			protected boolean pinging_port_80(String url)
		    {
		    	boolean reach_t = false;
		    	
		    	URL inURL80 = null;
		    	try {
					inURL80 = new URL(url);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return false;
				}
		//    	Log.d(OUT_LOG,  "inURL80: " + inURL80);
		    	try {
		            socket = new Socket( inURL80.getHost(), 80);
		 //           Log.d(OUT_LOG,  "socket: " + socket);
		            reach_t = true;
		        } catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
		            if (socket != null) 
		            	try {
		            		socket.close(); 
		            	} catch(IOException e)
		            	{
		            		
		            	}
		        }
				return reach_t;
		    }
			protected boolean pinging_(String url, int timeout) {
		    	/**
		    	 * Pings a HTTP URL. This effectively sends a HEAD request and returns <code>true</code> if the response code is in 
		    	 * the 200-399 range.
		    	 * @param url The HTTP URL to be pinged.
		    	 * @param timeout The timeout in millis for both the connection timeout and the response read timeout. Note that
		    	 * the total timeout is effectively two times the given timeout.
		    	 * @return <code>true</code> if the given HTTP URL has returned response code 200-399 on a HEAD request within the
		    	 * given timeout, otherwise <code>false</code>.
		    	 */
		        url = url.replaceFirst("https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
		        try {
		            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		            connection.setConnectTimeout(timeout);
		            connection.setReadTimeout(timeout);
		            connection.setRequestMethod("HEAD");
		            int responseCode = connection.getResponseCode();
		            responsecode = responseCode;
		            return (200 <= responseCode && responseCode <= 399);
		        } catch (IOException exception) {
		            return false;
		        }
		    }
	}
	public void finishAct(boolean errorTZ, String value) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		Toast.makeText(getApplicationContext(), currentname, Toast.LENGTH_LONG).show();
		intent.putExtra(_.LATGMAP, pos.latitude);
		intent.putExtra(_.LONGMAP, pos.longitude);
		intent.putExtra(_.TIMEZONE, value);
		intent.putExtra(_.ERRORTZ, errorTZ);
		intent.putExtra(_.NAMEOFTHEPLACE, currentname);
		setResult(RESULT_OK,intent);
		finish();
	}
	
	
}
