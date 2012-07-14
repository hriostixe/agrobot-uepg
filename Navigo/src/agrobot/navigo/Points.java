package agrobot.navigo;


import java.text.DecimalFormat;

import com.google.android.maps.GeoPoint;

public class Points {

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

	public static int maxIteracoes=5;
	public static int iteracao=0;
	public static double mediaValor=0;

	public static String PRECISAO="#.####";	
	public static int getMaxIteracoes() {
		return maxIteracoes;
	}





	public static String getPRECISAO() {
		return PRECISAO;
	}





	public static void setPRECISAO(String pRECISAO) {
		PRECISAO = pRECISAO;
	}





	public static void setMaxIteracoes(int maxIteracoes) {
		Points.maxIteracoes = maxIteracoes;
	}





	public static int getIteracao() {
		return iteracao;
	}





	public static void setIteracao(int iteracao) {
		Points.iteracao = iteracao;
	}





	public static double getMediaValor() {
		return mediaValor;
	}





	public static void setMediaValor(double mediaValor) {
		Points.mediaValor = mediaValor;
	}





	public static double getFirstLatitude() {
		return firstLatitude;
	}





	public static void setFirstLatitude(double firstLatitude) {
		Points.firstLatitude = firstLatitude;
	}





	public static double getFirstLongitude() {
		return firstLongitude;
	}





	public static void setFirstLongitude(double firstLongitude) {
		Points.firstLongitude = firstLongitude;
	}





	public static double getFirstAngle() {
		return firstAngle;
	}





	public static void setFirstAngle(double firstAngle) {
		Points.firstAngle = firstAngle;
	}





	public static double getFirstCatetoOposto() {
		return firstCatetoOposto;
	}





	public static void setFirstCatetoOposto(double firstCatetoOposto) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.firstCatetoOposto = Double.parseDouble(deci.format(firstCatetoOposto).replace(',', '.'));
	}





	public static double getFirstCatetoAdjacente() {
		return firstCatetoAdjacente;
	}





	public static void setFirstCatetoAdjacente(double firstCatetoAdjacente) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.firstCatetoAdjacente = Double.parseDouble(deci.format(firstCatetoAdjacente).replace(',', '.'));
	}





	public static double getFirstHipotenusa() {
		return firstHipotenusa;
	}





	public static void setFirstHipotenusa(double firstHipotenusa) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.firstHipotenusa = Double.parseDouble(deci.format(firstHipotenusa).replace(',', '.'));
	}





	
	
	
	
	public static GeoPoint getTargetPoint() {
		return targetPoint;
	}





	public static double getTargetLatitude() {
		return targetLatitude;
	}





	public static void setTargetLatitude(double targetLatitude) {
		Points.targetLatitude = targetLatitude;
	}





	public static double getTargetLongitude() {
		return targetLongitude;
	}





	public static void setTargetLongitude(double targetLongitude) {
		Points.targetLongitude = targetLongitude;
	}





	public static double getTargetAngle() {
		return targetAngle;
	}





	public static void setTargetAngle(double targetAngle) {
		Points.targetAngle = targetAngle;
	}





	public static double getTargetCatetoOposto() {
		return targetCatetoOposto;
	}





	public static void setTargetCatetoOposto(double targetCatetoOposto) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.targetCatetoOposto = Double.parseDouble(deci.format(targetCatetoOposto).replace(',', '.'));
	}





	public static double getTargetCatetoAdjacente() {
		return targetCatetoAdjacente;
	}





	public static void setTargetCatetoAdjacente(double targetCatetoAdjacente) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.targetCatetoAdjacente = Double.parseDouble(deci.format(targetCatetoAdjacente).replace(',', '.'));
	}





	public static double getTargetHipotenusa() {
		return targetHipotenusa;
	}





	public static void setTargetHipotenusa(double targetHipotenusa) {
		DecimalFormat deci = new DecimalFormat(PRECISAO);		
		Points.targetHipotenusa = Double.parseDouble(deci.format(targetHipotenusa).replace(',', '.'));
	}





	public static void setTargetPoint(GeoPoint targetPoint) {
		Points.targetPoint = targetPoint;
	}





	public static double makeTriangle(String tipo, double hip,double ang){
		try{
			if(tipo=="Oposto")
				return Math.abs(hip * Math.sin(Math.toRadians(ang)));
			else
				return Math.abs( hip * Math.cos(Math.toRadians(ang)));
			//TargetPoints.setCatetoAdjacente(TargetPoints.getHipotenusa() * Math.cos(TargetPoints.getFirstAngle()));
		//	return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
		
	}
	
}
