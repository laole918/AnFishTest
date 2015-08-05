package com.lele.test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class SmsTestActivity extends Activity {

	private MyBroadcastReceiver mbr;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mbr = new MyBroadcastReceiver();
		String SENT = "sms_sent";
		String DELIVERED = "sms_delivered";

		PendingIntent sentPI = PendingIntent.getActivity(this, 0, new Intent(
				SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getActivity(this, 0,
				new Intent(DELIVERED), 0);

		registerReceiver(mbr, new IntentFilter(SENT));

		registerReceiver(mbr, new IntentFilter(DELIVERED));

		SmsManager mgr = SmsManager.getDefault();
//		mgr.sendDataMessage("15311431745", null, (short) 1000,
//				"I Love You!".getBytes(), sentPI, deliveredPI);
		mgr.sendTextMessage("15311431745", null, "ÂòÌ¿ÉÕ¼¦³Ô°É¡£", sentPI, deliveredPI);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mbr);
	}
	
	public class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			System.out.println(action);
		}
		
	}
}
