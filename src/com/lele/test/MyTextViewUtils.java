package com.lele.test;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;


public class MyTextViewUtils {

	public static TextView GetTextView(final Context context, CharSequence source) {
		TextView tv = new TextView(context);
		View.OnClickListener l = new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(context, CommentsActivity.class);
				context.startActivity(i);
				System.out.println("ÄãºÃ°¡£¡£¡£¡");
			}
		};
		
		
		String str = source.toString();
		SpannableString sstr = new SpannableString(str);

		int start = 0;
		int end = str.indexOf("»Ø¸´");
		if(end != -1) {
			NoLineClickableSpan nlcSpan = new NoLineClickableSpan();
		    nlcSpan.setOnClickListener(l);
			sstr.setSpan(nlcSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			start = end + "»Ø¸´".length();
		}
		end = str.indexOf("£º");
		
//		ClickableSpan
	    NoLineClickableSpan nlcSpan = new NoLineClickableSpan();
	    nlcSpan.setOnClickListener(l);
		sstr.setSpan(nlcSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(sstr);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		return tv;
	}

}
