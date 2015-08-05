package com.lele.test;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import android.app.Activity;
import android.content.Intent;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {
	
	@Click(R.id.comments)
	public void onClickComments() {
		Intent i = new Intent();
		i.setClass(this, CommentsActivity.class);
		this.startActivity(i);
	}
	
	@Click(R.id.uploadphotos)
	public void onClickUploadPhotos() {
		UploadPhotos_.intent(this).start();
	}
	
	@Click(R.id.gps)
	public void onClickGps() {
		Intent i = new Intent();
		i.setClass(this, GpsActivity.class);
		this.startActivity(i);
	}
	
	@Click(R.id.smsdata)
	public void onClickSmsData() {
		Intent i = new Intent();
		i.setClass(this, SmsTestActivity.class);
		this.startActivity(i);
	}
	
	@Click(R.id.maptest)
	public void onClickMapTest() {
		MapNavActivity_.intent(this).start();
	}
	
}
