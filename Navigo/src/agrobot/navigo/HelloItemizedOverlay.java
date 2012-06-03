package agrobot.navigo;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
 
/*public class HelloItemizedOverlay extends ItemizedOverlay{
	private ArrayList mOverlays = new ArrayList();
	private Context mContext;
 
        //Não usar!!!!
	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}
 
	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}
 
	@Override
	protected OverlayItem createItem(int i) {
	  return (OverlayItem) mOverlays.get(i);
	}
 
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
 
	@Override
	public int size() {
	  return mOverlays.size();
	}
 
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = (OverlayItem) mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}*/
public class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	
	private ArrayList<OverlayItem> overlayItemList = new ArrayList<OverlayItem>();
	Context context;
 
	public HelloItemizedOverlay(Drawable marker, Context c) {
		super(boundCenterBottom(marker));
	// TODO Auto-generated constructor stub
	populate();
	context = c;
	}
	 
//	@Override
//	protected boolean onTap(int index) {
//	// TODO Auto-generated method stub
//	//return super.onTap(index);
//	 
//	Toast.makeText(context,
//	 "Touch on marker: \n" + overlayItemList.get(index).getTitle(),
//	 Toast.LENGTH_LONG).show();
//	 
//	return true;
//	}
	
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = overlayItemList.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(context);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
	public void addItem(GeoPoint p, String title, String snippet){
		overlayItemList.clear();
		OverlayItem newItem = new OverlayItem(p, title, snippet);
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
