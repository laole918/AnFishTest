package com.lele.test;

import java.util.ArrayList;
import java.util.List;

import com.lele.softkeyboard.InputBox;
import com.lele.softkeyboard.SoftKeyBoardHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CommentsActivity extends Activity {

	private ListView listView1;
	
	SoftKeyBoardHelper helper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		helper = SoftKeyBoardHelper.GetInstance(this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View layout = inflater.inflate(R.layout.layout_input, null, false);
		
		EditText editText = (EditText) layout.findViewById(R.id.comment);
		View keyBoardView = layout.findViewById(R.id.keyBoardView);
		View keyBoardBlank = layout.findViewById(R.id.keyBoardBlank);
		keyBoardBlank.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				helper.HideSoftInput();
			}
		});
		View submit = layout.findViewById(R.id.submit);
		submit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				helper.HideSoftInput();
			}
		});
		InputBox box = new InputBox();
		box.setInputLayout(layout);
		box.setInputView(keyBoardView);
		box.setInputText(editText);
		helper.SetInputBox(box);

		listView1 = (ListView) findViewById(R.id.listView1);
		listView1.setAdapter(new ListView1Adapter());
		helper.SetListView(listView1);
	}
	
	class ListView1Adapter extends BaseAdapter implements View.OnClickListener {

		private List<LinearLayout> layouts;
		
		public ListView1Adapter() {
			layouts = new ArrayList<LinearLayout>();
			for(int i = 0; i < 30; i++) {
				LinearLayout l = new LinearLayout(CommentsActivity.this);
				l.setOrientation(LinearLayout.VERTICAL);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				l.setLayoutParams(params);
				layouts.add(l);
			} 
		}
		
		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			
			return null;
		}

		@Override
		public long getItemId(int position) {
			
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null) {
				holder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(CommentsActivity.this);
				convertView = inflater.inflate(R.layout.layout_comments_item, parent, false);
				holder.button1 = (Button) convertView.findViewById(R.id.button1);
				holder.button2 = (Button) convertView.findViewById(R.id.button2);
				holder.linearLayout1 = (LinearLayout) convertView.findViewById(R.id.linearLayout1);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.button1.setOnClickListener(this);
			holder.button1.setText("position:" + position);
			holder.button2.setOnClickListener(this);
			holder.button1.setTag(R.string.position, position);
			holder.button1.setTag(R.string.button, "button1");
			holder.button1.setTag(R.string.layout, holder.linearLayout1);
			holder.button2.setTag(R.string.position, position);
			holder.button2.setTag(R.string.button, "button2");
			holder.button2.setTag(R.string.layout, holder.linearLayout1);

			LinearLayout ll1 = (LinearLayout) (layouts.get(position).getParent());
			if(ll1 != null) {
				ll1.removeAllViews();
			}
			holder.linearLayout1.removeAllViews();
			holder.linearLayout1.addView(layouts.get(position));

//			if(position % 2 == 0) {
//				convertView.setBackgroundColor(Color.BLUE);
//			} else {
//				convertView.setBackgroundColor(Color.GREEN);
//			}
			System.out.println("position::::::::::" + position);
			return convertView;
		}

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag(R.string.position);
			String btnName = (String) v.getTag(R.string.button);
			LinearLayout ll = (LinearLayout) v.getTag(R.string.layout);
			LinearLayout l = layouts.get(position);
			if(l.getParent() == null) {
				ll.addView(l);
			}
			if("button1".equals(btnName)) {
				TextView tv = MyTextViewUtils.GetTextView(CommentsActivity.this, "尤文乐：真好看呀！！！" + position);
				tv.setBackgroundResource(R.drawable.selector_word);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.topMargin = DisplayUtil.dip2px(CommentsActivity.this, 10);
				l.addView(tv);
				tv.setTag(position);
				tv.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						helper.ShowSoftInputAnchor(v);
					}
				});
			} else if("button2".equals(btnName)) {
				TextView tv = MyTextViewUtils.GetTextView(CommentsActivity.this, "曹志风回复尤文乐：是啊，你看那朵花好漂亮呀！！！给你摘一支吧。。。" + position);
//				tv.setBackgroundResource(R.drawable.selector_word);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.topMargin = DisplayUtil.dip2px(CommentsActivity.this, 10);
				l.addView(tv);
			}
		}
		
	}
	
	class ViewHolder {
		Button button1;
		Button button2;
		LinearLayout linearLayout1;
	}

}
