package agrobot.navigo;
 



import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
 
public class AndroidMapViewActivity extends MapActivity {
	 MyItemizedOverlay myItemizedOverlay = null;
	 MyLocationOverlay myLocationOverlay = null;
	 MapController controller;
	 public static MapView mapView;
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
	 super.onCreate(savedInstanceState);
     setContentView(R.layout.maps);
     mapView = (MapView) findViewById(R.id.mapview);
     mapView.setBuiltInZoomControls(true);
 //    mapView.getController().setZoom(17);
     mapView.setClickable(true);
     mapView.setEnabled(true);
     mapView.setSatellite(true);    
     mapView.setBuiltInZoomControls(true);
     controller = mapView.getController();
     Drawable marker=getResources().getDrawable(android.R.drawable.star_big_on);
     int markerWidth = marker.getIntrinsicWidth();
     int markerHeight = marker.getIntrinsicHeight();
     marker.setBounds(0, markerHeight, markerWidth, 0);

     
     
     myItemizedOverlay = new MyItemizedOverlay(marker,AndroidMapViewActivity.this);
     mapView.getOverlays().add(myItemizedOverlay);
    
     
     
     
     myLocationOverlay = new MyLocationOverlay(this, mapView);
     mapView.getOverlays().add(myLocationOverlay);
     initMyLocation();
     mapView.postInvalidate();
     
     
     
}


/** Start tracking the position on the map. */
private void initMyLocation() {
   final MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
   overlay.enableMyLocation();
   //overlay.enableCompass(); // does not work in emulator
   overlay.runOnFirstFix(new Runnable() {
      public void run() {
         // Zoom in to current location
         controller.setZoom(17);
         controller.animateTo(overlay.getMyLocation());
      }
   });
   mapView.getOverlays().add(overlay);
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