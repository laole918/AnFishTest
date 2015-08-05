package com.lele.test;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NoLineClickableSpan extends ClickableSpan {
	
	private View.OnClickListener mListener;
	
	public void setOnClickListener(View.OnClickListener l) {
		mListener = l;
	}
	
	@Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false);
    }

	@Override
	public void onClick(View widget) {
		if(mListener != null) {
			mListener.onClick(widget);
		}
	}

}
