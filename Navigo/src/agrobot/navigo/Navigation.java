package agrobot.navigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

		if (Point.getTargetPoint() != null) {// ja marcou um target
			mTargetLat = Point.getTargetPoint().getLatitudeE6()
					/ (double) GeoUtils.MILLION;
			mTargetLon = Point.getTargetPoint().getLongitudeE6()
					/ (double) GeoUtils.MILLION;

		} else {
			int latE6 = (int) (i.getFloatExtra("latitude", (float) -25.113324) * GeoUtils.MILLION);
			int lonE6 = (int) (i.getFloatExtra("longitude", (float) -50.144996) * GeoUtils.MILLION);

			mTargetLat = latE6 / (double) GeoUtils.MILLION;
			mTargetLon = lonE6 / (double) GeoUtils.MILLION;
		}

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

		if (Point.getTargetPoint() != null) { // já pegou o target
			mTargetLat = Point.getTargetPoint().getLatitudeE6()
					/ (double) GeoUtils.MILLION;
			mTargetLon = Point.getTargetPoint().getLongitudeE6()
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
		DecimalFormat f = new DecimalFormat("0.####");

		if (Point.getTargetPoint() != null) {
			// entao temos já marcado o target
			// viewInfo.setText("TESTE.: " +
			// Point.getTargetPoint().getLatitudeE6());
			if ((Point.getFirstLatitude() == 0)) {
				// marca o primeiro ponto
				Point.setTargetAngle(ang);
				Point.setTargetHipotenusa(mDistance);
				Point.setTargetCatetoOposto(Point.makeTriangle("Oposto",
						mDistance, ang));
				Point.setTargetCatetoAdjacente(Point.makeTriangle("Adjascente",
						mDistance, ang));
				Point.setFirstLatitude(mMyLocationLat);
				Point.setFirstLongitude(mMyLocationLon);
				try {
					String lstrNomeArq = "trianguloPrin.txt";

					File arq = new File(
							Environment.getExternalStorageDirectory(),
							lstrNomeArq);
					FileOutputStream fos;

					// transforma o texto digitado em array de bytes
					byte[] dados;
					String texto;
					Date date = new Date();
					DateFormat formato = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss.SSS");
					String formattedDate = formato.format(date);

					texto = formattedDate
							+ "Angulo:"
							+ String.valueOf(Point.getTargetAngle())
							+ " || hipotenusa:"
							+ String.valueOf(Point.getTargetHipotenusa())
							+ " || Oposto:"
							+ String.valueOf(Point.getTargetCatetoOposto())
							+ " || Adjacente:"
							+ String.valueOf(Point.getTargetCatetoAdjacente()
									+ "\n");
					dados = texto.getBytes();
					fos = new FileOutputStream(arq, true);

					// escreve os dados e fecha o arquivo
					fos.write(dados);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					System.out.println("Erro : " + e.getMessage());
				}

				Fuzzy.createRules();

				TextView viewAng = (TextView) findViewById(R.id.firstAngle);
				viewAng.setTypeface(LCDTypeface);
				viewAng.setTextSize(20);
				viewAng.setText(f.format(ang));
				Point.setIteracao(0);
				Point.setMediaValor(0);

				Toast.makeText(
						getBaseContext(),
						"co" + Point.getTargetCatetoOposto() + "\n ca"
								+ Point.getTargetCatetoAdjacente() + "\n an"
								+ ang + "\n d" + mDistance, Toast.LENGTH_SHORT)
						.show();

				// Toast.makeText(getBaseContext(),
				// "entrou"+ang,
				// Toast.LENGTH_SHORT).show();

			} else {
				double newAdj, newOposto;

				double newHipotenusa = (GeoUtils.distanceKm(
						Point.getFirstLatitude(), Point.getFirstLongitude(),
						mMyLocationLat, mMyLocationLon));

				double newAngulo = Math.round(GeoUtils.bearing(
						Point.getFirstLatitude(), Point.getFirstLongitude(),
						mMyLocationLat, mMyLocationLon));
				DecimalFormat deci = new DecimalFormat(Point.getPRECISAO());
				newHipotenusa = Double.parseDouble(deci.format(newHipotenusa).replace(',', '.'));

				newAdj = Point.makeTriangle("Adjascente", newHipotenusa,
						newAngulo);
				newOposto = Point.makeTriangle("Oposto", newHipotenusa,
						newAngulo);
				
				newAdj = Double.parseDouble(deci.format(newAdj).replace(',', '.'));
				newOposto = Double.parseDouble(deci.format(newOposto).replace(',', '.'));

				String extersao = Fuzzy.doFuzzy(newAdj, newOposto);
				try {
					String lstrNomeArq = "trianguloSec.txt";

					File arq = new File(
							Environment.getExternalStorageDirectory(),
							lstrNomeArq);
					FileOutputStream fos;

					// transforma o texto digitado em array de bytes
					byte[] dados;
					String texto;
					Date date = new Date();
					DateFormat formato = new SimpleDateFormat(
							"dd/MM/yyyy HH:mm:ss.SSS");
					String formattedDate = formato.format(date);
					texto = formattedDate
							+ " Angulo:"
							+ String.valueOf(newAngulo)
							+ " || hipotenusa:"
							+ String.valueOf(newHipotenusa)
							+ " || Oposto:"
							+ String.valueOf(newOposto)
							+ " || Adjacente:"
							+ String.valueOf(newAdj + " || Fuzzy:"
									+ String.valueOf(extersao) + "\n");
					dados = texto.getBytes();
					fos = new FileOutputStream(arq, true);

					// escreve os dados e fecha o arquivo
					fos.write(dados);
					fos.flush();
					fos.close();
				} catch (Exception e) {
					System.out.println("Erro : " + e.getMessage());
				}
				if (Point.getIteracao() < Point.getMaxIteracoes()) {
					Point.setMediaValor(Point.getMediaValor()
							+ Double.parseDouble(extersao));
					Point.setIteracao(Point.getIteracao() + 1);
					// Toast.makeText(getBaseContext(),
					// ""+ String.valueOf(Point.getIteracao()),
					// Toast.LENGTH_SHORT).show();

				} else {
					long res = Math.round((Point.getMediaValor() / Point
							.getIteracao()));
					String dir;
					if (res < 0)
						dir = " <- esquerda";
					else
						dir = " -> direita";
					if (res == 0)
						viewInfo.setText("Continue em Frente");
					else
						viewInfo.setText("Vire " + String.valueOf(res) + dir);
					Point.setIteracao(0);
					Point.setMediaValor(0);
				}

				TextView viewHip = (TextView) findViewById(R.id.firstHip);
				viewHip.setTypeface(LCDTypeface);
				viewHip.setTextSize(20);
				viewHip.setText(String.valueOf(newHipotenusa));
				// viewHip.setText(String.valueOf(Point.getIteracao()));

				TextView viewAdj = (TextView) findViewById(R.id.iteracao);
				viewAdj.setTypeface(LCDTypeface);
				viewAdj.setTextSize(20);
				viewAdj.setText(String.valueOf(Point.getIteracao()));

				TextView viewAng = (TextView) findViewById(R.id.firstAngle);
				viewAng.setTypeface(LCDTypeface);
				viewAng.setTextSize(20);
				viewAng.setText(String.valueOf(newAngulo));

				//
				// TextView viewOpo= (TextView) findViewById(R.id.firstOposto);
				// viewOpo.setTypeface(LCDTypeface);
				// viewOpo.setTextSize(20);
				// //viewOpo.setText(String.valueOf(newOposto));

			}
		} else {
			viewInfo.setText("Nenhum alvo marcado");
		}
		// Point.setAngle(ang);
		// Point.setHipotenusa(mDistance);
		// Point.setCatetoOposto((int)Point.makeTriangle("Oposto",
		// mDistance,(int) ang));
		// Point.setCatetoAdjacente((int)Point.makeTriangle("Adjascente",
		// mDistance,(int)ang));
		// Ranges r = new Ranges();
		// String rX[] = {"ME", "E", "N", "D", "MD"};
		// String rY[] = {"MB", "B", "Ne", "A", "MA"};
		// ArrayList<VariavelLinguistica> dirX =
		// r.createRanges(mDistance*Math.sin(ang) , 5, rX);
		// ArrayList<VariavelLinguistica> dirY =
		// r.createRanges(mDistance*Math.cos(ang), 5, rY);
		// String a=Fuzzy2.doFuzzy(dirX,dirY);
		// Toast.makeText(getBaseContext(),
		// "co"+(mDistance*Math.sin(ang))+"\n ca"+(mDistance*Math.cos(ang))
		// +"\n an"+ang+"\n d"+mDistance,
		// Toast.LENGTH_SHORT).show();

		// viewAng.setText(String.valueOf(Math.round(Point.getFirstAngle())));

		// Toast.makeText(getBaseContext(),
		// ""+Fuzzy2.doFuzzy(),
		// Toast.LENGTH_SHORT).show();
		//

	}

	/*
	 * @Override protected void onResume() { super.onResume(); //
	 * mSensorManager.registerListener(mRadar, SensorManager.SENSOR_ORIENTATION,
	 * // SensorManager.SENSOR_DELAY_GAME);
	 * 
	 * // Start animating the radar screen satView.startSweep();
	 * 
	 * // Register for location updates
	 * mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	 * LOCATION_UPDATE_INTERVAL_MILLIS, 1, satView);
	 * mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
	 * LOCATION_UPDATE_INTERVAL_MILLIS, 1, satView); }
	 * 
	 * @Override protected void onPause() { //
	 * mSensorManager.unregisterListener(mRadar);
	 * mLocationManager.removeUpdates(satView);
	 * 
	 * // Stop animating the radar screen satView.stopSweep(); super.onStop(); }
	 */

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
