package agrobot.navigo;
 
import java.util.ArrayList;
 
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import android.widget.Toast;

 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;



public class MyItemizedOverlay extends ItemizedOverlay<OverlayItem> {
 
private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
Context context;

public MyItemizedOverlay(Drawable marker, Context c) {
super(boundCenterBottom(marker));
// TODO Auto-generated constructor stub
populate();
context = c;
}
 
@Override
public boolean onTap(GeoPoint p, MapView mapView) {
 // TODO Auto-generated method stub
  
 if(super.onTap(p, mapView)){
  return true;
 }
  
 targetPoints.setTargetPoint(p);
 targetPoints.setFirstAngle(0);
// Toast.makeText(context, 
//		 ""+targetPoints.getFirstAngle(), 
//         Toast.LENGTH_SHORT).show();
 String title = "pt:" + String.valueOf( " : Lat: " + p.getLatitudeE6() + 
         " Lng: " + p.getLongitudeE6());
 String snippet = "geo:\n"
   + String.valueOf(p.getLatitudeE6()) + "\n"
   + String.valueOf(p.getLongitudeE6());
 
 addItem(p, title, snippet);
 
  return true;
}
 
@Override
protected boolean onTap(int index) {
 // TODO Auto-generated method stub
 //return super.onTap(index);
  
 Toast.makeText(context,
   "Ponto marcado: \n" + overlayItemList.get(index).getTitle(),
   Toast.LENGTH_LONG).show();
 
 return true;
} 



public void addItem(GeoPoint p, String title, String snippet){
OverlayItem newItem = new OverlayItem(p, title, snippet);
overlayItemList.removeAll(overlayItemList);
overlayItemList.add(newItem);
populate();
}
 
@Override
protected OverlayItem createItem(int i) {
// TODO Auto-generated method stub
return overlayItemList.get(i);
}
 
@Override
public int size() {
// TODO Auto-generated method stub
return overlayItemList.size();
}
 
@Override
public void draw(Canvas canvas, MapView mapView, boolean shadow) {
// TODO Auto-generated method stub
super.draw(canvas, mapView, shadow);
}
}