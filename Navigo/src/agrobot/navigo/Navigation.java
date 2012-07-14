package agrobot.navigo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fuz.Fuzzy;

import android.draw.GeoUtils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.draw.RadarView;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Navigation extends Activity implements LocationListener {
	public static final String LOG_TAG = "Navigation";
	private static final String MOCK_GPS_PROVIDER_INDEX = "NavigationIndex";

	private MockGpsProvider mMockGpsProviderTask = null;
	private Integer mMockGpsProviderIndex = 0;

	private static final int MENU_STANDARD = Menu.FIRST + 1;
	private static final int MENU_METRIC = Menu.FIRST + 2;
	private static final String RADAR = "radar";
	private static final String PREF_METRIC = "metric";

	private RadarView radarView;
	private SharedPreferences mPrefs;

	private double mTargetLat;
	private double mTargetLon;
	private double mMyLocationLat;
	private double mMyLocationLon;

	public static final double AUMENTO = 1000;
	public static final ArrayList<Double> listaValores = null;
	private double mDistance; // Distance to target, in KM

	@Override
	public void onBackPressed() {
		NavigoActivity ParentActivity;
		ParentActivity = (NavigoActivity) this.getParent();
		ParentActivity.onBackPressed_Activity();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.navigation);

		/** Use saved instance state if necessary. */
		if (savedInstanceState instanceof Bundle) {
			/** Let's find out where we were. */
			mMockGpsProviderIndex = savedInstanceState.getInt(
					MOCK_GPS_PROVIDER_INDEX, 0);
		}

		/** Setup GPS. */
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// use real GPS provider if enabled on the device
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 0, 0, this);
		} else if (!locationManager
				.isProviderEnabled(MockGpsProvider.GPS_MOCK_PROVIDER)) {
			// otherwise enable the mock GPS provider
			locationManager.addTestProvider(MockGpsProvider.GPS_MOCK_PROVIDER,
					false, false, false, false, true, false, false, 0, 5);
			locationManager.setTestProviderEnabled(
					MockGpsProvider.GPS_MOCK_PROVIDER, true);
		}

		if (locationManager
				.isProviderEnabled(MockGpsProvider.GPS_MOCK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					MockGpsProvider.GPS_MOCK_PROVIDER, 0, 0, this);

			/** Load mock GPS data from file and create mock GPS provider. */
			try {
				// create a list of Strings that can dynamically grow
				List<String> data = new ArrayList<String>();

				/**
				 * read a CSV file containing WGS84 coordinates from the
				 * 'assets' folder (The website http://www.gpsies.com offers
				 * downloadable tracks. Select a track and download it as a CSV
				 * file. Then add it to your assets folder.)
				 */
				InputStream is = getAssets().open("mock_gps_data.csv");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				// add each line in the file to the list
				String line = null;
				while ((line = reader.readLine()) != null) {
					data.add(line);
				}

				// convert to a simple array so we can pass it to the AsyncTask
				String[] coordinates = new String[data.size()];
				data.toArray(coordinates);

				// create new AsyncTask and pass the list of GPS coordinates
				mMockGpsProviderTask = new MockGpsProvider();
				mMockGpsProviderTask.execute(coordinates);
			} catch (Exception e) {
			}
		}

		radarView = (RadarView) findViewById(R.id.satView);

		// Metric or standard units?
		mPrefs = getSharedPreferences(RADAR, MODE_PRIVATE);
		boolean useMetric = mPrefs.getBoolean(PREF_METRIC, false);
		radarView.setUseMetric(useMetric);

		// Read the target from our intent
		Intent i = getIntent();

		if (Points.getTargetPoint() != null) {// ja marcou um target
			mTargetLat = Points.getTargetPoint().getLatitudeE6()
					/ (double) GeoUtils.MILLION;
			mTargetLon = Points.getTargetPoint().getLongitudeE6()
					/ (double) GeoUtils.MILLION;

		} else {
			int latE6 = (int) (i.getFloatExtra("latitude", (float) -25.113324) * GeoUtils.MILLION);
			int lonE6 = (int) (i.getFloatExtra("longitude", (float) -50.144996) * GeoUtils.MILLION);

			mTargetLat = latE6 / (double) GeoUtils.MILLION;
			mTargetLon = lonE6 / (double) GeoUtils.MILLION;
		}
		ImageView imageSeta = (ImageView) findViewById(R.id.seta);
		imageSeta.setVisibility(View.INVISIBLE);

	}

	/** Define a mock GPS provider as an asynchronous task of this Activity. */
	private class MockGpsProvider extends AsyncTask<String, Integer, Void> {
		public static final String LOG_TAG = "GpsMockProvider";
		public static final String GPS_MOCK_PROVIDER = "GpsMockProvider";

		/** Keeps track of the currently processed coordinate. */
		public Integer index = 0;

		@Override
		protected Void doInBackground(String... data) {
			// process data
			for (String str : data) {
				// skip data if needed (see the Activity's savedInstanceState
				// functionality)
				if (index < mMockGpsProviderIndex) {
					index++;
					continue;
				}

				// let UI Thread know which coordinate we are processing
				publishProgress(index);

				// retrieve data from the current line of text
				Double latitude = null;
				Double longitude = null;
				Double altitude = null;
				try {
					String[] parts = str.split(",");
					latitude = Double.valueOf(parts[0]);
					longitude = Double.valueOf(parts[1]);
					altitude = Double.valueOf(parts[2]);
				} catch (NullPointerException e) {
					break;
				} // no data available
				catch (Exception e) {
					continue;
				} // empty or invalid line

				// translate to actual GPS location
				Location location = new Location(GPS_MOCK_PROVIDER);
				location.setLatitude(latitude);
				location.setLongitude(longitude);
				location.setAltitude(altitude);
				location.setTime(System.currentTimeMillis());

				// show debug message in log
				Log.d(LOG_TAG, location.toString());

				// provide the new location
				LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				locationManager.setTestProviderLocation(GPS_MOCK_PROVIDER,
						location);

				// sleep for a while before providing next location
				try {
					Thread.sleep(1000);

					// gracefully handle Thread interruption (important!)
					if (Thread.currentThread().isInterrupted())
						throw new InterruptedException("");
				} catch (InterruptedException e) {
					break;
				}

				// keep track of processed locations
				index++;
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			Log.d(LOG_TAG, "onProgressUpdate():" + values[0]);
			mMockGpsProviderIndex = values[0];
		}
	}

	@Override
	public void onLocationChanged(Location location) {

		if (Points.getTargetPoint() != null) { // já pegou o target
			mTargetLat = Points.getTargetPoint().getLatitudeE6()
					/ (double) GeoUtils.MILLION;
			mTargetLon = Points.getTargetPoint().getLongitudeE6()
					/ (double) GeoUtils.MILLION;
		}
		mMyLocationLat = location.getLatitude();
		mMyLocationLon = location.getLongitude();
		int mMyLocationLatDeg = (int) Math.abs(mMyLocationLat);
		int mMyLocationLatMin = (int) Math.abs((mMyLocationLat % 1) * 60);
		int mMyLocationLatSec = (int) Math.abs(Math
				.round(((((mMyLocationLat % 1) * 60) % 1) * 60)));
		String StrMyLocationLat = String.format(Locale.getDefault(),
				"%dº%02d'%02d\"", mMyLocationLatDeg, mMyLocationLatMin,
				mMyLocationLatSec);
		String south = mMyLocationLat < 0 ? "S" : "N";

		int mMyLocationLonDeg = (int) Math.abs(mMyLocationLon);
		int mMyLocationLonMin = (int) Math.abs((mMyLocationLon % 1) * 60);
		int mMyLocationLonSec = (int) Math.abs(Math
				.round(((((mMyLocationLon % 1) * 60) % 1) * 60)));
		String StrMyLocationLon = String.format(Locale.getDefault(),
				"%dº%02d'%02d\"", mMyLocationLonDeg, mMyLocationLonMin,
				mMyLocationLonSec);
		String west = mMyLocationLon < 0 ? "O" : "L";

		mDistance = GeoUtils.distanceKm(mMyLocationLat, mMyLocationLon,
				mTargetLat, mTargetLon);

		double ang = Math.round(GeoUtils.bearing(mMyLocationLat,
				mMyLocationLon, mTargetLat, mTargetLon));

		radarView.updateDistance(mDistance);

		radarView.setAngleView(String.valueOf(ang));

		// show the received location in the view

		Typeface LCDTypeface = Typeface.createFromAsset(this.getAssets(),
				"DS-DIGII.TTF");

		TextView textDistance = (TextView) findViewById(R.id.distanceView);
		textDistance.setText(radarView.getDistanceView());

		TextView textAngle = (TextView) findViewById(R.id.angleView);
		textAngle.setText(String.valueOf(Math.round(ang)));

		TextView viewLatitude = (TextView) findViewById(R.id.latitude);
		viewLatitude.setTypeface(LCDTypeface);
		viewLatitude.setTextSize(25);
		viewLatitude.setText(String.valueOf(mMyLocationLat));
		TextView viewSouth = (TextView) findViewById(R.id.south);
		viewSouth.setText(south);

		TextView viewLongitude = (TextView) findViewById(R.id.longitude);
		viewLongitude.setTypeface(LCDTypeface);
		viewLongitude.setTextSize(25);
		viewLongitude.setText(String.valueOf(mMyLocationLon));
		TextView viewWest = (TextView) findViewById(R.id.west);
		viewWest.setText(west);

		TextView viewInfo = (TextView) findViewById(R.id.information);
		viewInfo.setTypeface(LCDTypeface);
		viewInfo.setTextSize(25);

		if (Points.getTargetPoint() != null) {
			// entao temos já marcado o target
			radarView.startSweep();
			if ((Points.getFirstLatitude() == 0)) {
				// marca o primeiro ponto
				Points.setTargetAngle(ang);
				Points.setTargetHipotenusa(mDistance);
				Points.setTargetCatetoOposto(Points.makeTriangle("Oposto",
						mDistance, ang));
				Points.setTargetCatetoAdjacente(Points.makeTriangle("Adjascente",
						mDistance, ang));
				Points.setFirstLatitude(mMyLocationLat);
				Points.setFirstLongitude(mMyLocationLon);
				
				Fuzzy.createRules();
				Points.setIteracao(0);
				Points.setMediaValor(0);

				Toast.makeText(
						getBaseContext(),
						"co" + Points.getTargetCatetoOposto() + "\n ca"
								+ Points.getTargetCatetoAdjacente() + "\n an"
								+ ang + "\n d" + mDistance, Toast.LENGTH_SHORT)
						.show();
				AndroidMapViewActivity.mapView.getOverlays().add(
						new MyOverlayLine(Points.getTargetPoint().getLatitudeE6(),
								Points.getTargetPoint().getLongitudeE6(),(int)(location.getLatitude()*1E6)
										, (int) (location.getLongitude()*1E6)));
				

			} else {
				double newAdj, newOposto;

				double newHipotenusa = (GeoUtils.distanceKm(
						Points.getFirstLatitude(), Points.getFirstLongitude(),
						mMyLocationLat, mMyLocationLon));

				double newAngulo = Math.round(GeoUtils.bearing(
						Points.getFirstLatitude(), Points.getFirstLongitude(),
						mMyLocationLat, mMyLocationLon));
				DecimalFormat deci = new DecimalFormat(Points.getPRECISAO());
				newHipotenusa = Double.parseDouble(deci.format(newHipotenusa)
						.replace(',', '.'));

				newAdj = Points.makeTriangle("Adjascente", newHipotenusa,
						newAngulo);
				newOposto = Points.makeTriangle("Oposto", newHipotenusa,
						newAngulo);

				newAdj = Double.parseDouble(deci.format(newAdj).replace(',',
						'.'));
				newOposto = Double.parseDouble(deci.format(newOposto).replace(
						',', '.'));

				String extersao = Fuzzy.doFuzzy(newAdj, newOposto);
				
				// if (Point.getIteracao() < Point.getMaxIteracoes()) {
				// Point.setMediaValor(Point.getMediaValor()
				// + Double.parseDouble(extersao));
				// Point.setIteracao(Point.getIteracao() + 1);
				// // Toast.makeText(getBaseContext(),
				// // ""+ String.valueOf(Point.getIteracao()),
				// // Toast.LENGTH_SHORT).show();
				//
				// } else {
				// long res = Math.round((Point.getMediaValor() / Point
				// .getIteracao()));
				String dir;
				double min = Points.firstAngle - 90;
				double max = Points.firstAngle + 90;
				if (min < 0)
					min = 360 - Math.abs(min);
				if (max > 360)
					max = max - 360;

				try {
					double res = Math.round(Double.parseDouble(extersao));

					ImageView imageSeta = (ImageView) findViewById(R.id.seta);
					
					viewInfo.setTextSize(25);
					if (res < 0){
						if(res<-15){
							imageSeta.setImageResource(R.drawable.me);
						}else{
							imageSeta.setImageResource(R.drawable.e);
						}
						dir = "  esquerda";					
						imageSeta.setVisibility(1);
					}else{
						if(res>15){
							imageSeta.setImageResource(R.drawable.me);
						}else{
							imageSeta.setImageResource(R.drawable.e);
						}							
						dir = "  direita";					
						imageSeta.setVisibility(1);
					}if (res == 0){
						imageSeta.setImageResource(R.drawable.f);
						viewInfo.setText("Continue em Frente");
					}else
						viewInfo.setText("Vire " + Math.abs(res) + " a " + dir);

					//
				} catch (Exception e) {
					// TODO: handle exception
					viewInfo.setText("erro no calculo da fuzzy");
				}


			}
		} else {
			viewInfo.setText("Nenhum alvo marcado");
		}

	}

	// @Override
	// protected void onResume() {
	// super.onResume(); //
	//
	// mSensorManager.registerListener(mRadar, SensorManager.SENSOR_ORIENTATION,
	// // SensorManager.SENSOR_DELAY_GAME);
	//
	// // Start animating the radar screen satView.startSweep();
	//
	// // Register for location updates
	// mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	// LOCATION_UPDATE_INTERVAL_MILLIS, 1, satView);
	// mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	// LOCATION_UPDATE_INTERVAL_MILLIS, 1, satView); }
	//
	// @Override protected void onPause() { //
	// mSensorManager.unregisterListener(mRadar);
	// mLocationManager.removeUpdates(satView);
	//
	// // Stop animating the radar screen
	// satView.stopSweep();
	// super.onStop();
	// }
	//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_STANDARD, 0, R.string.menu_standard)
				.setIcon(R.drawable.ic_menu_standard)
				.setAlphabeticShortcut('A');
		menu.add(0, MENU_METRIC, 0, R.string.menu_metric)
				.setIcon(R.drawable.ic_menu_metric).setAlphabeticShortcut('C');
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_STANDARD: {
			setUseMetric(false);
			return true;
		}
		case MENU_METRIC: {
			setUseMetric(true);
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUseMetric(boolean useMetric) {
		SharedPreferences.Editor e = mPrefs.edit();
		e.putBoolean(PREF_METRIC, useMetric);
		e.commit();
		radarView.setUseMetric(useMetric);
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
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override protected void onDataReceived(final byte[] buffer, final int
	 * size) { runOnUiThread(new Runnable() { public void run() {
	 * 
	 * } }); }
	 */
}
