package ru.ps.habrtimezone;

import java.util.TimeZone;

import ru.ps.libs.info._Info;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class AMain extends Activity implements OnClickListener {
	private static final String LOG_TAG = "AMain::HabrTimeZone";
	
	private LocationManager lm;
	private LocationListener gpsLocationListener;
	private LocationListener networkLocationListener;
	
	private Location loc;
	
	private Button btnCancel, btnGMap, btnFromCurrent, btnCities;
	private ScrollView scCoord, scCoordInit;
	private TextView tvLat, tvLon;
	private EditText etLat, etLon;
	private int globType = 0;
	private boolean activated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain);
        
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnGMap = (Button) findViewById(R.id.btnGMap);
        btnFromCurrent = (Button) findViewById(R.id.btnFromCurrent);
        btnCities = (Button) findViewById(R.id.btnCities);
        
        tvLat = (TextView) findViewById(R.id.tvLat);
        tvLon = (TextView) findViewById(R.id.tvLon);
        etLat = (EditText) findViewById(R.id.etLat);
        etLon = (EditText) findViewById(R.id.etLon);
        
        scCoord = (ScrollView) findViewById(R.id.scCoord);
        scCoordInit = (ScrollView) findViewById(R.id.scCoordInit);
        
        btnCancel.setOnClickListener(this);
        btnGMap.setOnClickListener(this);
        btnFromCurrent.setOnClickListener(this);
        btnCities.setOnClickListener(this);
        
	    initListeners();
	    viewType(globType);
    }
    private void initListeners() {
    	lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    	if  (_.useGPS == 1) gpsLocationListener = new LocListener();
    	networkLocationListener = new LocListener();
    	
		if (_.useGPS == 1)
		{
			if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, _.TIMEUPDATE, 0, gpsLocationListener);
				}
		}
			if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, _.TIMEUPDATE, 0, networkLocationListener);
				}
	  }


	 private class LocListener implements LocationListener {
		    
		 @Override
		    public void onLocationChanged(Location location) {
			 activated = true;
		    	loc = location;
		    	Log.d(LOG_TAG,getResources().getString(R.string.Latitude) + " " + loc.getLatitude() + " " + getResources().getString(R.string.Longitude) + " "  + loc.getLongitude());
		    	if (globType == 0)
		    	{
		    		etLat.setText(Double.toString(loc.getLatitude()));
			    	etLon.setText(Double.toString(loc.getLongitude()));
		    	}
		    	tvLat.setText(Double.toString(loc.getLatitude()));
		    	tvLon.setText(Double.toString(loc.getLongitude()));
		    	
		    	viewType(1);
		    }

			@Override
		    public void onProviderDisabled(String provider) {
		    }

		    @Override
		    public void onProviderEnabled(String provider) {
		    }

		    @Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    }
		  }
	 @Override
	  public void onDestroy() {
	    super.onDestroy();
	    if (gpsLocationListener != null)
	    {
	    	lm.removeUpdates(gpsLocationListener);
	    }
	    if (networkLocationListener != null)
	    {
	 		lm.removeUpdates(networkLocationListener);
	    }
	    Log.d(LOG_TAG, "onDestroy");
	  }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String str = "";
		double lat = 55.753636;
		double lon = 37.619799;
		String tz = TimeZone.getDefault().getID();
		boolean errorTZ = false;
		String currentname = "";

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			switch (requestCode) {
			case _.agmap_activity:
				lat = data.getDoubleExtra(_.LATGMAP, lat);
				lon = data.getDoubleExtra(_.LONGMAP, lon);
				tz = data.getStringExtra(_.TIMEZONE);
				errorTZ = data.getBooleanExtra(_.ERRORTZ,errorTZ);
				currentname = data.getStringExtra(_.NAMEOFTHEPLACE);
				if (tz == null || "".equals(tz)) tz = TimeZone.getDefault().getID();
				if (currentname == null || "".equals(currentname)) currentname = "";
				
				etLat.setText(Double.toString(lat));
				etLon.setText(Double.toString(lon));
				
				str = getResources().getString(R.string.Latitude) + " " + Double.toString(lat) + "; " + getResources().getString(R.string.Longitude) + "; " + Double.toString(lon) + "; " +
						getResources().getString(R.string.TimeZone) + " " + tz + "; " + getResources().getString(R.string.Error) + " " + errorTZ + "; " +
						getResources().getString(R.string.CurName) + " " + currentname;
				
				
				break;
			case _.acities_activity:
				break;
			default:
				break;
			}
			
			
			AlertDialog alertbox = new AlertDialog.Builder(this)
			.setMessage(str)
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        // do something when the button is clicked
		        public void onClick(DialogInterface arg0, int arg1) {
		        	arg0.dismiss();
		        }
		    }).show();
		}
		else//RESULT_CANCELED
		{
			Toast.makeText(this, getResources().getString(R.string.Canceled), Toast.LENGTH_LONG).show();
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId())
		{
		case R.id.btnCancel:
			viewType(1);
			break;
		case R.id.btnGMap:
			intent = new Intent(this,AGMap.class);
			
			double[] LatLon = getValsFromUI();
			intent.putExtra(_.LATGMAP, LatLon[0]);
			intent.putExtra(_.LONGMAP, LatLon[1]);
			intent.putExtra(_.LATGMAPCUR, LatLon[2]);
			intent.putExtra(_.LONGMAPCUR, LatLon[3]);
			intent.putExtra(_.CURORMAN, false);

			startActivityForResult(intent, _.agmap_activity);
			break;
		case R.id.btnFromCurrent:
			etLat.setText(Double.toString(loc.getLatitude()));
	    	etLon.setText(Double.toString(loc.getLongitude()));
			break;
		case R.id.btnCities:
			intent = new Intent(this,ACityListOnline.class);
			startActivityForResult(intent, _.acities_activity);
			break;
		}
	}
	private double[] getValsFromUI() {
		// TODO Auto-generated method stub
		double lat = 55.753636;
		double lon = 37.619799;

		
		try {
			
			String str = etLat.getText().toString();
			str= str.replace("&#150;", "-");
	        str= str.replace("–", "-");
	        str= str.replace(",", ".");
	        str= str.replace("n", "");
	        str= str.replace("N", "");
	        str= str.replace("s", "-");
	        str= str.replace("S", "-");
	        str= str.replace(" ", "");
			
	        lat = Double.parseDouble(str)%90.;
	        
			str = etLon.getText().toString();
			str= str.replace("&#150;", "-");
            str= str.replace("–", "-");
            str= str.replace(",", ".");
            str= str.replace("e", "");
            str= str.replace("E", "");
            str= str.replace("w", "-");
            str= str.replace("W", "-");
            str= str.replace(" ", "");
            
            lon = Double.parseDouble(str)%180.;

		
		} catch (NumberFormatException e) {
			lat = 55.753636;
			lon = 37.619799;
		}
		
		double latc = lat;
		double lonc = lon;
		
		if (activated)
		{
			latc = loc.getLatitude();
			lonc = loc.getLongitude();
		}
		
		double[] ar = new double[4];
		
		ar[0] = lat;
		ar[1] = lon;
		
		ar[2] = latc;
		ar[3] = lonc;
		return ar;
	}
	private void viewType(int type)
	{
		globType = type;
		if (type == 0)
		{
			scCoordInit.setVisibility(View.VISIBLE);
			scCoord.setVisibility(View.GONE);
		}
		else
		{
			scCoordInit.setVisibility(View.GONE);
			scCoord.setVisibility(View.VISIBLE);
		}
	}
}
