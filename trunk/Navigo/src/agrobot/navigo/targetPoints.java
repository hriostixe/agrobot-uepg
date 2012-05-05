package agrobot.navigo;

import com.google.android.maps.GeoPoint;

public class targetPoints {

	public static GeoPoint targetPoint=null;

	public static GeoPoint getTargetPoint() {
		return targetPoint;
	}

	public static void setTargetPoint(GeoPoint targetPoint) {
		targetPoints.targetPoint = targetPoint;
	}

	public targetPoints() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
