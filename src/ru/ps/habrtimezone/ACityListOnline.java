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
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.ps.libs.info._Info;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ACityListOnline extends Activity implements OnClickListener, OnItemClickListener {
	ListView outList;
	EditText searchstr;
	Button btnbck,btnsrc;
	long instMAX = 1L;
	long instCUR = 0L;
	String installstring = "";
	ArrayList<_Info> results = new ArrayList<_Info>();
	ArrayAdapter<_Info> arrayAdapter;
	boolean locationsisintalled = false;

	Intent inputIntent;
	CityTask citytask = new CityTask(this);
	ProgressBar pbCont;
	public Random r;
	String[] ew_ar = new String[2];
	String[] ns_ar = new String[2];
	
	Random randomGenerator;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		randomGenerator = new Random(System.currentTimeMillis());
		r = new Random(System.currentTimeMillis());
		ew_ar = getResources().getStringArray(R.array.ew_ar);
		ns_ar = getResources().getStringArray(R.array.ns_ar);
		
		inputIntent = getIntent();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.acities);

		

		outList = (ListView) findViewById(R.id.outList);
		searchstr = (EditText) findViewById(R.id.searchstr);

		btnbck = (Button) findViewById(R.id.btnbck);
		btnsrc = (Button) findViewById(R.id.btnsrc);

		pbCont = (ProgressBar) findViewById(R.id.pbCont);
		

		btnbck.setOnClickListener(this);
		btnsrc.setOnClickListener(this);
		
		arrayAdapter =      
				new MyClassAdapter(this,android.R.layout.simple_list_item_1, results);
	         outList.setAdapter(arrayAdapter); 
	         outList.setOnItemClickListener(this);
	         
	    outList.setVisibility(View.VISIBLE);
	    pbCont.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
		case R.id.btnbck:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.btnsrc:
			pbCont.setVisibility(View.VISIBLE);
			outList.setVisibility(View.GONE);
			String t = searchstr.getText().toString();
			citytask = new CityTask(this);
			citytask.execute(t);
			break;
		default:
			break;
		}
	}

	public class MyClassAdapter extends ArrayAdapter<_Info> {

		    private ArrayList<_Info> items;
		    private Context context;

		    public MyClassAdapter(Context context, int textViewResourceId, ArrayList<_Info> items) {
		        super(context, textViewResourceId, items);
		        this.context = context;
		        this.items = items;
		    }

		    public View getView(int position, View convertView, ViewGroup parent) {
		        View view = convertView;
		        if (view == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            view = inflater.inflate(R.layout.litem, null);
		        }
		        LinearLayout itemroot = (LinearLayout) view.findViewById(R.id.itemroot);
		        itemroot.setBackgroundColor((position%2==0)?(0x99333333):(0x99555555));
		        if (items.size() > 0)
		        {
			        _Info item = items.get(position % items.size());
			        if (item!= null) {
			            // My layout has only one TextView
			        	TextView itemView = (TextView) view.findViewById(R.id.item);
			            if (itemView != null) {
			                // do whatever you want with your string and long
			                itemView.setText(String.format("%s %s", item.getName(), item.getTimezone()));
			            }
			            TextView itemSUB = (TextView) view.findViewById(R.id.txt);
			            if (itemSUB != null) {
			                // do whatever you want with your string and long
			            	itemSUB.setText(_.todeg(item.getLatitude(),1,ew_ar,ns_ar) + " " + _.todeg(item.getLongitude(), 0,ew_ar,ns_ar));
			            }
		         }
		    }
		        return view;
		    }
		}
		@Override
		public void onItemClick(AdapterView <?> parentAdapter, View view, int position,
        long id) {
			// TODO Auto-generated method stub
			//outList.//
			_Info cur = arrayAdapter.getItem(position);
			Intent intent = new Intent(this, ADescription.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(_.FULLDATA, cur);
			startActivity(intent);
		}
		public class CityTask extends AsyncTask<String, Integer, Void>
		{
			private Context с;
			Socket socket = null;
		    boolean reachable = false;
		    boolean reachable_head = false;
		    int responsecode = -1;
		    int pingtime = -1;
		    int pingtime_head = -1;
		    String[] urlString = {"http://api.geonames.org/searchJSON?q=", "&username=","&style=full"};
		    String json = "";
			private JSONObject jObj;
			public CityTask(Context context) {
		        this.с = context;
		    }
			@Override
			protected void onPreExecute() {
			// TODO Auto-generated method stub
				
			}
			
			@Override
			protected Void doInBackground(String... strs) {
				// TODO Auto-generated method stub
				if ("".equals(strs[0])) return null;
		         findplace(strs[0]);
				runOnUiThread(new Runnable() {
				    public void run() {

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
				 pbCont.setVisibility(View.GONE);
				 outList.setVisibility(View.VISIBLE);
			 }
			 private void findplace(String str)
				{

				 String outURL = null;
				try {
					outURL = urlString[0] +  URLEncoder.encode(str, "UTF-8") + urlString[1] + _.names1[r.nextInt(_.names1.length)] + urlString[2];
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 URL inURL = null;
				 
			    	try {
						inURL = new URL(outURL);
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						return ;
					}
			    //	Log.d(OUT_LOG,  "inURL: " + inURL);
			    	long startTime = System.currentTimeMillis();
			        reachable = pinging_port_80(outURL);
			        long endTime = System.currentTimeMillis();
			       // Log.d(OUT_LOG,  "startTime: " + startTime);
			//        Log.d(OUT_LOG,  "delta: " + (endTime - startTime));
			        startTime = System.currentTimeMillis();
					reachable_head = pinging_(outURL,80);
					endTime = System.currentTimeMillis();
			//		Log.d(OUT_LOG,  "delta1: " + (endTime - startTime));
					if (!(reachable || reachable_head)) return;
					json = GoToURL(outURL, 20000, "");
					try {
			            jObj = new JSONObject(json);
			        } catch (JSONException e) {
			            Log.e("JSON Parser", "Error parsing data " + e.toString());
			        }
					JSONArray gNames = new JSONArray();
					try {
						if (jObj!=null)
							gNames = jObj.getJSONArray("geonames");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if (gNames.length() > 0)
					{
						results.clear();
					}
					for (int i = 0; i < gNames.length(); i++)
					{
						JSONObject c = new JSONObject();
						JSONObject tz = new JSONObject();
						String cName = "";
						long population  = 0L;
						double lat = 0.0;
						double lng = 0.0;
						String tzStr = "GMT+00:00";
						
						try {
							c = gNames.getJSONObject(i);
							population = c.getLong("population");
							
							if (population > 0L)
							{
								cName = c.getString("name");
								lat = c.getDouble("lat");
								lng = c.getDouble("lng");
								tz = c.getJSONObject("timezone");
								tzStr = tz.getString("timeZoneId");
								
								_Info city = new _Info();
					    		 city.setName(cName);
					    		 city.setLatitude(lat);
					    		 city.setLongitude(lng);
					    		 city.setTimezone(tzStr);
					    		 city.setPopulation(population);
					    		 results.add(city);
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					if (gNames.length() > 0)
					{
					  runOnUiThread(new Runnable() {
						    public void run() {
						    	 arrayAdapter.notifyDataSetChanged();
						    	Toast.makeText(getApplicationContext(), "ready!", Toast.LENGTH_LONG).show();
						    }
						});
					}
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
}
