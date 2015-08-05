package com.lele.test;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class GpsActivity extends Activity {
	
	private TextView tv_gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps);
		tv_gps = (TextView) findViewById(R.id.tv_gps);
		
		LocationCore.getInstance(this).SetOnLocationListener(new LocationCore.OnLocationListener() {
			
			@Override
			public void onLocationChanged(Location location) {
				updateToNewLocation(location);
			}
		});
		LocationCore.getInstance(this).StartUpdateLocation();
	}
	
	private void updateToNewLocation(Location location) {
		if (location != null) {
            double  latitude = location.getLatitude();
            double longitude= location.getLongitude();
            tv_gps.setText("维度：" +  latitude+ "\n经度" + longitude);
        } else {
            tv_gps.setText("无法获取地理信息");
        }
	}

}
