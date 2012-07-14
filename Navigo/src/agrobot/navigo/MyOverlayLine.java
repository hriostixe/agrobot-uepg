package agrobot.navigo;

import agrobot.navigo.MyOverlayLine;
import agrobot.navigo.Points;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;


import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
//
//mapView.getOverlays().add(
//		new MyOverlayLine(Points.getTargetPoint().getLatitudeE6(),
//				Points.getTargetPoint().getLongitudeE6(),(int)(location.getLatitude()*1E6)
//						, (int) (location.getLongitude()*1E6)));

public class MyOverlayLine extends Overlay {
    private GeoPoint gp1;
    private GeoPoint gp2;

    public MyOverlayLine(int fromlatE6,int fromlonE6,int tolatE6,int tologE6)    {          

        int flat=0,flog=0,tlat=0,tlog=0;
        flat=fromlatE6;
        flog=fromlonE6;
        tlat=tolatE6;
        tlog=tologE6;                       
        gp1 = new GeoPoint(flat,flog);
        gp2 = new GeoPoint(tlat,tlog);
    }

    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow,long when) {
        Projection projection = mapView.getProjection();
        if (shadow == false) {
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            Point point = new Point();
            projection.toPixels(gp1, point);
            paint.setColor(Color.WHITE);
            Point point2 = new Point();
            projection.toPixels(gp2, point2);
            paint.setStrokeWidth(5);
            
            canvas.drawLine((float) point.x, (float) point.y, (float) point2.x,(float) point2.y, paint);
            
        }
        return super.draw(canvas, mapView, shadow, when);
    }
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // TODO Auto-generated method stub
        super.draw(canvas, mapView, shadow);
        
    }
}