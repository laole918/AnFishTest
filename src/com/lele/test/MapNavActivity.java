package com.lele.test;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.lele.dialog.ListViewDialog;
import com.lele.map.Navigation;
import com.lele.map.Route;

@EActivity(R.layout.activity_map_nav)
public class MapNavActivity extends FragmentActivity implements
		ListViewDialog.OnItemClickListener, OnMarkerClickListener {

	// @ViewById
	MapView mapView;
	@ViewById
	ViewPager viewPager;
	@ViewById
	FrameLayout frameLayout;

	private AMap aMap;

	UserStatePagerAdapter adapter;
	// private WeakReference<Bundle> savedInstanceState;

	private ListViewDialog dialog;

	private List<Marker> markers;
	private Marker lastMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// this.savedInstanceState = new
		// WeakReference<Bundle>(savedInstanceState);
		mapView = new MapView(this);
		mapView.onCreate(savedInstanceState);
		aMap = mapView.getMap();
		aMap.getUiSettings().setZoomPosition(
				AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
		aMap.setOnMarkerClickListener(this);
		markers = new ArrayList<Marker>();
		for (int i = 0; i < 100; i++) {
			markers.add(addMark());
		}
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@AfterViews
	public void init() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.MATCH_PARENT);
		FrameLayout content = (FrameLayout) findViewById(android.R.id.content);
		content.addView(mapView, 0, params);
		// mapView.onCreate(savedInstanceState.get());
		// aMap = mapView.getMap();

		dialog = new ListViewDialog(this);
		dialog.setCanceledOnTouchOutside(true);
		dialog.addAllItems(new String[] { "百度地图", "高德地图", "腾讯地图", "苹果地图" });
		dialog.setOnItemClickListener(this);

		adapter = new UserStatePagerAdapter(getSupportFragmentManager());
		ArrayList<Fragment> fragments = new ArrayList<Fragment>();
		for (int i = 0; i < 100; i++) {
			UserFragment fragment = UserFragment_.builder().build();
			fragments.add(fragment);
		}
		adapter.addFragments(fragments);
		viewPager.setAdapter(adapter);
		viewPager.setPageMargin(DisplayUtil.dip2px(this, 15));
		viewPager.setOffscreenPageLimit(3);
		if (Build.VERSION.SDK_INT >= 11) {
			frameLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		} else {
			try {
				Method setLayerTypeMethod = frameLayout.getClass().getMethod(
						"setLayerType", new Class[] { int.class, Paint.class });
				setLayerTypeMethod.setAccessible(true);
				setLayerTypeMethod.invoke(frameLayout, new Object[] {
						View.LAYER_TYPE_SOFTWARE, null });
			} catch (NoSuchMethodException e) {
				// Older OS, no HW acceleration anyway
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		// viewPager.setClipChildren(false);
		// addMark();
	}

	private Marker addMark() {
		double lat = Math.random();
		double lng = Math.random();
		LatLng latlng = new LatLng(36.061 + lat, 103.834 + lng);
		Marker marker = aMap.addMarker(new MarkerOptions()
				.position(latlng)
				.title("海龙垂钓园")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.map_press)).draggable(true));
		marker.setIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_normal));
		// marker.showInfoWindow();// 设置默认显示一个infowinfow

		return marker;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	// @Click(R.id.navigation)
	public void onClickNavigation() {
		dialog.show();
	}

	@Override
	public void onItemClick(int position) {
		Route route = new Route();
		route.setOriginName("小辛庄");
		route.setOriginLatitude(40.0925831675);
		route.setOriginLongitude(116.3752744016);
		LatLng latlng = lastMarker.getPosition();
		String name = lastMarker.getTitle();
		route.setDestinationName(name);
		route.setDestinationLatitude(latlng.latitude);
		route.setDestinationLongitude(latlng.longitude);
		switch (position) {
		case 0: {
			if (!Navigation.OpenMap(this, Navigation.Map_BaiDu, route)) {
				Toast.makeText(this, "没有安装百度地图，请先下载安装", 0).show();
			}
			break;
		}
		case 1: {
			if (!Navigation.OpenMap(this, Navigation.Map_AutoNavi, route)) {
				Toast.makeText(this, "没有安装高德地图，请先下载安装", 0).show();
			}
			break;
		}
		case 2: {
			if (!Navigation.OpenMap(this, Navigation.Map_Tencent, route)) {
				Toast.makeText(this, "没有安装腾讯地图，请先下载安装", 0).show();
			}
			break;
		}
		case 3: {
			Toast.makeText(this, "你想太多了，怎么会有苹果地图", 0).show();
			break;
		}
		}
		dialog.dismiss();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (lastMarker != null) {
			lastMarker.setIcon(BitmapDescriptorFactory
					.fromResource(R.drawable.map_normal));
		}
		dialog.show();
		marker.setIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.map_press));
		lastMarker = marker;
		return false;
	}
}
