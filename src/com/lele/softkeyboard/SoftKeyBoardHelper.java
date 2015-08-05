package com.lele.softkeyboard;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

public class SoftKeyBoardHelper {

	private static HashMap<Activity, SoftKeyBoardHelper> mHashMap = new HashMap<Activity, SoftKeyBoardHelper>();
	
	private Activity mContext;
	private InputMethodManager imm;
	private FrameLayout contentView;
	
	private ListView listView;
	private ScrollView scrollView;
	
	private int anchorY;
	private int keyBoardHeight;
	
	private InputBox inputBox;
	private boolean isShowing;
	
	private SoftKeyBoardHelper(Activity context) {
		keyBoardHeight = -1;
		isShowing = false;
		init(context);
	}
	
	public static SoftKeyBoardHelper GetInstance(Context context) {
		if (context instanceof Activity) {
			Activity a = (Activity) context;
			if (mHashMap.containsKey(a)) {
				return mHashMap.get(a);
			} else {
				SoftKeyBoardHelper helper = new SoftKeyBoardHelper(a);
				mHashMap.put(a, helper);
				return helper;
			}
		}
		return null;
	}
	
	private void init(Activity context) {
		mContext = context;
		contentView = (FrameLayout) mContext.findViewById(android.R.id.content);
		imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
		contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                contentView.getWindowVisibleDisplayFrame(r);
                int screenHeight = contentView.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                Log.d("Keyboard Size", "Size: " + heightDifference);
                if(heightDifference > r.top) {
                	contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                	keyBoardHeight = heightDifference;
                	int inputHeight = 0;
            		if(inputBox != null && inputBox.getInputView() != null) {
            			View view = inputBox.getInputView();
            			inputHeight = view.getHeight();
            		}
                	int yy = anchorY - r.bottom + inputHeight;
                	if(listView != null) {
                		listView.smoothScrollBy(yy, 0);
                	}
                	if(scrollView != null) {
                		scrollView.smoothScrollBy(yy, 0);
                	}
                }
            }
        });
		
	}

	public void SetListView(ListView listView) {
		this.listView = listView;
	}
	
	public void SetScrollView(ScrollView scrollView) {
		this.scrollView = scrollView;
	}
	
	public void SetInputBox(InputBox inputBox) {
		this.inputBox = inputBox;
	}
	
	public void ShowSoftInput() {
		if(inputBox == null || inputBox.getInputText() == null) {
			return;
		}
		if (!isShowing) {
			View layout = inputBox.getInputLayout();
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			contentView.addView(layout, params);
			isShowing = true;
		}
		View text = inputBox.getInputText();
		text.setFocusable(true);
		text.requestFocus();
		imm.showSoftInput(text, InputMethodManager.SHOW_FORCED);
	}
	
	public void ShowSoftInputAnchor(View anchor) {
		Rect r = new Rect();
		int[] location = new int[2];
		anchor.getLocalVisibleRect(r);
		anchor.getLocationInWindow(location);
//		anchor.getLocationOnScreen(location);
		if(keyBoardHeight < 0) {
			anchorY = location[1] + r.bottom;
		} else {
			anchorY = location[1];
			smoothScrollBy();
		}
		ShowSoftInput();
	}
	
	private void smoothScrollBy() {
		int inputHeight = 0;
		if(inputBox != null && inputBox.getInputView() != null) {
			View view = inputBox.getInputView();
			inputHeight = view.getHeight();
		}
    	int yy = anchorY - keyBoardHeight + inputHeight;
    	if(listView != null) {
    		listView.smoothScrollBy(yy, 500);
    	}
    	if(scrollView != null) {
    		scrollView.smoothScrollBy(yy, 500);
    	}
	}
	
	public void HideSoftInput() {
		if(inputBox == null || inputBox.getInputText() == null) {
			return;
		}
		isShowing = false;
		View text = inputBox.getInputText();
		imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
		View layout = inputBox.getInputLayout();
		contentView.removeView(layout);
	}

}
