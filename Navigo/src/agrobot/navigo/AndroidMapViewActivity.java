package agrobot.navigo;
 



import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
 
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.widget.Toast;
 
public class AndroidMapViewActivity extends MapActivity {
	 private LocationManager lm;
	 private LocationListener locationListener;
	 private MapView mapView;
	 private MapController mc;
	 MyItemizedOverlay myItemizedOverlay = null;
	 MyLocationOverlay myLocationOverlay = null;
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    setContentView(R.layout.maps);
//    MapView mapView = (MapView) findViewById(R.id.mapview);
//    mapView.setBuiltInZoomControls(true);
 
//    Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
//    int markerWidth = marker.getIntrinsicWidth();
//    int markerHeight = marker.getIntrinsicHeight();
//    marker.setBounds(0, markerHeight, markerWidth, 0);
// 
// 
//    MyItemizedOverlay myItemizedOverlay
//     = new MyItemizedOverlay(marker, AndroidMapViewActivity.this);
//    mapView.getOverlays().add(myItemizedOverlay);
//    lm = (LocationManager) 
//            getSystemService(Context.LOCATION_SERVICE);    
//        locationListener = new MyLocationListener();
//        lm.requestLocationUpdates(
//            LocationManager.GPS_PROVIDER, 
//            0, 
//            0, 
//            locationListener);
//        
//        mapView = (MapView) findViewById(R.id.mapview);
//        mc = mapView.getController();
//    
// 
//	super.onCreate(savedInstanceState);
//	setContentView(R.layout.maps);
//
//	//---use the LocationManager class to obtain GPS locations---
//	lm = (LocationManager)
//	getSystemService(Context.LOCATION_SERVICE);
//	locationListener = new MyLocationListener();
//	lm.requestLocationUpdates(
//	LocationManager.GPS_PROVIDER,
//	0,
//	0,
//	locationListener);
//	mapView = (MapView) findViewById(R.id.mapview);
//	mc = mapView.getController();
	
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.maps);
     MapView mapView = (MapView) findViewById(R.id.mapview);
     mapView.setBuiltInZoomControls(true);
 //    mapView.getController().setZoom(17);
     mapView.setClickable(true);
     mapView.setEnabled(true);
     mapView.setSatellite(true);    
    
     Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
     int markerWidth = marker.getIntrinsicWidth();
     int markerHeight = marker.getIntrinsicHeight();
     marker.setBounds(0, markerHeight, markerWidth, 0);
     
     myItemizedOverlay = new MyItemizedOverlay(marker,AndroidMapViewActivity.this);
     mapView.getOverlays().add(myItemizedOverlay);
    
//     GeoPoint myPoint1 = new GeoPoint(0*1000000, 0*1000000);
//     myItemizedOverlay.addItem(myPoint1, "myPoint1", "myPoint1");
//     GeoPoint myPoint2 = new GeoPoint(50*1000000, 50*1000000);
//     myItemizedOverlay.addItem(myPoint2, "myPoint2", "myPoint2");
     
     myLocationOverlay = new MyLocationOverlay(this, mapView);
     mapView.getOverlays().add(myLocationOverlay);
     mapView.postInvalidate();
}
@Override
protected boolean isLocationDisplayed() {
 // TODO Auto-generated method stub
 return false;
}

@Override
protected boolean isRouteDisplayed() {
 // TODO Auto-generated method stub
 return false;
}

@Override
protected void onResume() {
 // TODO Auto-generated method stub
 super.onResume();
 myLocationOverlay.enableMyLocation();
 myLocationOverlay.enableCompass();
 
}

@Override
protected void onPause() {
 // TODO Auto-generated method stub
 super.onPause();
 myLocationOverlay.disableMyLocation();
 myLocationOverlay.disableCompass();
} 


private class MyLocationListener implements LocationListener 
{
    @Override
    public void onLocationChanged(Location loc) {
//        if (loc != null) {                
//            Toast.makeText(getBaseContext(), 
//                "Location changed : Lat: " + loc.getLatitude() + 
//                " Lng: " + loc.getLongitude(), 
//                Toast.LENGTH_SHORT).show();
//            
//            GeoPoint p = new GeoPoint(
//                    (int) (loc.getLatitude() * 1E6), 
//                    (int) (loc.getLongitude() * 1E6));
//            Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
//            int markerWidth = marker.getIntrinsicWidth();
//            int markerHeight = marker.getIntrinsicHeight();
//            marker.setBounds(0, markerHeight, markerWidth, 0);
//         
//            mapView.getOverlays().clear();
//            HelloItemizedOverlay myItemizedOverlay = new HelloItemizedOverlay(marker, AndroidMapViewActivity.this);
//            mapView.getOverlays().add(myItemizedOverlay);
//            
//   
//            myItemizedOverlay.addItem(p, "myPoint1", "myPoint1");
//            
//            mc.animateTo(p);     
//            mc.setZoom(16);                
//            mapView.invalidate();
//        }
    }
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
    @Override
    public void onStatusChanged(String provider, int status, 
        Bundle extras) {
        // TODO Auto-generated method stub
    }
    
 }
}