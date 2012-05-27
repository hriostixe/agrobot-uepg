package agrobot.navigo;

import android.content.IntentSender.SendIntentException;

import com.google.android.maps.GeoPoint;

public class targetPoints {

	public static GeoPoint targetPoint=null;
	public static double firstAngle=0;
	public static double catetoOposto=0;
	public static double catetoAdjacente=0;
	public static double hipotenusa=0;
	
	
	public static double getFirstAngle() {
		return firstAngle;
	}

	public static void setFirstAngle(double firstAngle) {
		targetPoints.firstAngle = firstAngle;
	}

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

	public static double getCatetoOposto() {
		return catetoOposto;
	}

	public static void setCatetoOposto(double catetoOposto) {
		targetPoints.catetoOposto = catetoOposto;
	}

	public static double getCatetoAdjacente() {
		return catetoAdjacente;
	}

	public static void setCatetoAdjacente(double catetoAdjacente) {
		targetPoints.catetoAdjacente = catetoAdjacente;
	}

	public static double getHipotenusa() {
		return hipotenusa;
	}

	public static void setHipotenusa(double hipotenusa) {
		targetPoints.hipotenusa = hipotenusa;
	}
	
	public static boolean makeTriangle(){
		
		targetPoints.setCatetoOposto(targetPoints.getHipotenusa() * Math.sin(targetPoints.getFirstAngle()));
		targetPoints.setCatetoAdjacente(targetPoints.getHipotenusa() * Math.cos(targetPoints.getFirstAngle()));
		return true;
		
	}
	
}
