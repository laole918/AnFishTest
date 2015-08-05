package com.lele.test;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

public class LocationCore {
	
	private static LocationCore mInstance;
	
	private Context mContext;
	private LocationManager mLocationManager;
	
	private OnLocationListener mListener;
	
	private LocationCore(Context context) {
		mContext = context;
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		
	}
	
	public static LocationCore getInstance(Context context) {
		if(mInstance == null) {
			mInstance = new LocationCore(context);
		}
		return mInstance;
	}
	
	public void SetOnLocationListener(OnLocationListener l) {
		mListener = l;
	}
	
	private LocationListener mLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			if(mListener != null) {
				mListener.onLocationChanged(location);
			}
		}
	};
	
	public void StartUpdateLocation() {
		boolean isGpsEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if(!(isGpsEnable || isNetworkEnable)) {
			openGPS();
		}

		Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = mLocationManager.getBestProvider(criteria, true);
        Location location = mLocationManager.getLastKnownLocation(provider);
        if(mListener != null) {
			mListener.onLocationChanged(location);
		}
        mLocationManager.requestLocationUpdates(provider, 10 * 1000, 500, mLocationListener);
	}
	
	private void openGPS() {
		if(Build.VERSION.SDK_INT < 14) {
			Intent GPSIntent = new Intent(); 
	        GPSIntent.setClassName("com.android.settings", 
	                "com.android.settings.widget.SettingsAppWidgetProvider"); 
	        GPSIntent.addCategory("android.intent.category.ALTERNATIVE"); 
	        GPSIntent.setData(Uri.parse("custom:3")); 
	        try {
	            PendingIntent.getBroadcast(mContext, 0, GPSIntent, 0).send(); 
	        } catch (CanceledException e) {
	            e.printStackTrace(); 
	        }
		} else {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			mContext.startActivity(intent);
		}
	}
	
	public void StopUpdateLocation() {
		mLocationManager.removeUpdates(mLocationListener);
	}
	
	public interface OnLocationListener {
		public void onLocationChanged(Location location);
	}
}
