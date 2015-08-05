package com.lele.test;

import java.io.File;
import java.util.ArrayList;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OnActivityResult.Extra;
import org.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoSelectorActivity;

@EActivity(R.layout.activity_upload)
public class UploadPhotos extends Activity {

	private String url = "http://192.168.123.1:8080/fServer/upf";

	@ViewById
	ImageView imageView1;
	@ViewById
	Button button1;
	
	private ArrayList<String> arrayList = new ArrayList<String>();
	
	private BitmapUtils bitmapUtils;
	
	@AfterViews
	public void init() {
		bitmapUtils = new BitmapUtils(this);
		
		DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder() //
				.considerExifParams(true) // 调整图片方向
				.resetViewBeforeLoading(true) // 载入之前重置ImageView
				.showImageOnLoading(R.drawable.ic_picture_loading) // 载入时图片设置为黑色
				.showImageOnFail(R.drawable.ic_picture_loadfailed) // 加载失败时显示的图片
				.delayBeforeLoading(0) // 载入之前的延迟时间
				.build(); //
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultDisplayImageOptions)
				.memoryCacheExtraOptions(480, 800).threadPoolSize(5).build();
		ImageLoader.getInstance().init(config);
	}
	
	@Click(R.id.button1)
	public void onClickButton1() {
		Intent intent = new Intent(this, PhotoSelectorActivity.class);
		intent.putExtra("limit", 2);
		this.startActivityForResult(intent, 1);
	}
	
	@OnActivityResult(value = 1)
	public void onPhotoSelectorResult(@Extra ArrayList<PhotoModel> photos) {
		if(photos != null && !photos.isEmpty()) {
			PhotoModel photo = photos.get(0);
			bitmapUtils.display(imageView1, photo.getOriginalPath());
			for(int i = 0; i < photos.size(); i ++) {
				arrayList.add(photos.get(i).getOriginalPath());
			}
		}
	}
	
	@Click(R.id.button2)
	public void onClickButton2() {

		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("token", "1");
		for(int i = 0; i < arrayList.size(); i++) {
			File file = new File(arrayList.get(i));
			params.addBodyParameter("file" + (i + 1), file);
		}
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
	        public void onStart() {
				LogUtils.d("开始上传");
	        }

	        @Override
	        public void onLoading(long total, long current, boolean isUploading) {
	            if (isUploading) {
	            	LogUtils.d("upload: " + current + "/" + total);
	            } else {
	            	LogUtils.d("reply: " + current + "/" + total);
	            }
	        }
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
//				responseInfo.result;
				LogUtils.d(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtils.d("code:" + error.getExceptionCode());
				LogUtils.d(msg, error);
			}
		});
	}
}
