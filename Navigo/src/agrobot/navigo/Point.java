package agrobot.navigo;


import com.google.android.maps.GeoPoint;

public class Point {

	public static GeoPoint targetPoint=null;
	public static double targetLatitude=0;
	public static double targetLongitude=0;
	public static double targetAngle=0;
	public static double targetCatetoOposto=0;
	public static double targetCatetoAdjacente=0;
	public static double targetHipotenusa=0;

	public static double firstLatitude=0;
	public static double firstLongitude=0;
	public static double firstAngle=0;
	public static double firstCatetoOposto=0;
	public static double firstCatetoAdjacente=0;
	public static double firstHipotenusa=0;
	
	public static double getFirstLatitude() {
		return firstLatitude;
	}





	public static void setFirstLatitude(double firstLatitude) {
		Point.firstLatitude = firstLatitude;
	}





	public static double getFirstLongitude() {
		return firstLongitude;
	}





	public static void setFirstLongitude(double firstLongitude) {
		Point.firstLongitude = firstLongitude;
	}





	public static double getFirstAngle() {
		return firstAngle;
	}





	public static void setFirstAngle(double firstAngle) {
		Point.firstAngle = firstAngle;
	}





	public static double getFirstCatetoOposto() {
		return firstCatetoOposto;
	}





	public static void setFirstCatetoOposto(double firstCatetoOposto) {
		Point.firstCatetoOposto = firstCatetoOposto;
	}





	public static double getFirstCatetoAdjacente() {
		return firstCatetoAdjacente;
	}





	public static void setFirstCatetoAdjacente(double firstCatetoAdjacente) {
		Point.firstCatetoAdjacente = firstCatetoAdjacente;
	}





	public static double getFirstHipotenusa() {
		return firstHipotenusa;
	}





	public static void setFirstHipotenusa(double firstHipotenusa) {
		Point.firstHipotenusa = firstHipotenusa;
	}





	
	
	
	
	public static GeoPoint getTargetPoint() {
		return targetPoint;
	}





	public static double getTargetLatitude() {
		return targetLatitude;
	}





	public static void setTargetLatitude(double targetLatitude) {
		Point.targetLatitude = targetLatitude;
	}





	public static double getTargetLongitude() {
		return targetLongitude;
	}





	public static void setTargetLongitude(double targetLongitude) {
		Point.targetLongitude = targetLongitude;
	}





	public static double getTargetAngle() {
		return targetAngle;
	}





	public static void setTargetAngle(double targetAngle) {
		Point.targetAngle = targetAngle;
	}





	public static double getTargetCatetoOposto() {
		return targetCatetoOposto;
	}





	public static void setTargetCatetoOposto(double targetCatetoOposto) {
		Point.targetCatetoOposto = targetCatetoOposto;
	}





	public static double getTargetCatetoAdjacente() {
		return targetCatetoAdjacente;
	}





	public static void setTargetCatetoAdjacente(double targetCatetoAdjacente) {
		Point.targetCatetoAdjacente = targetCatetoAdjacente;
	}





	public static double getTargetHipotenusa() {
		return targetHipotenusa;
	}





	public static void setTargetHipotenusa(double targetHipotenusa) {
		Point.targetHipotenusa = targetHipotenusa;
	}





	public static void setTargetPoint(GeoPoint targetPoint) {
		Point.targetPoint = targetPoint;
	}





	public static double makeTriangle(String tipo, double hip,double ang){
		try{
			if(tipo=="Oposto")
				return hip * Math.sin(ang);
			else
				return hip * Math.cos(ang);
			//TargetPoints.setCatetoAdjacente(TargetPoints.getHipotenusa() * Math.cos(TargetPoints.getFirstAngle()));
		//	return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
	}
	
}
