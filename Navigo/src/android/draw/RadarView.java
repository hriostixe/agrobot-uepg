package android.draw;

import agrobot.navigo.Application;
import agrobot.navigo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RadarView extends View  {
    private static final long RETAIN_GPS_MILLIS = 10000L;

    private int mLastScale = -1;
    private String[] mDistanceScale = new String[4];
    private Paint mGridPaint;
    private Paint mErasePaint;
  
    private static float KM_PER_METERS = 0.001f;
    private static float METERS_PER_KM = 1000f;

    /**
     * These are the list of choices for the radius of the outer circle on the
     * screen when using metric units. All items are in kilometers. This array is 
     * used to choose the scale of the radar display.
     */
    private static double mMetricScaleChoices[] = { 
        100 * KM_PER_METERS,
        200 * KM_PER_METERS,
        400 * KM_PER_METERS, 
        1, 2, 4, 8, 20, 40, 100, 200, 400, 1000, 
        2000, 4000, 10000, 20000, 40000, 80000 };

    /**
     * Once the scale is chosen, this array is used to convert the number of
     * kilometers on the screen to an integer. (Note that for short distances we
     * use meters, so we multiply the distance by {@link #METERS_PER_KM}. (This
     * array is for metric measurements.)
     */
    private static float mMetricDisplayUnitsPerKm[] = { 
        METERS_PER_KM, METERS_PER_KM, METERS_PER_KM, METERS_PER_KM, METERS_PER_KM, 
        1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f };

    /**
     * This array holds the formatting string used to display the distance to
     * the target. (This array is for metric measurements.)
     */
    private static String mMetricDisplayFormats[] = {
        "%.0fm", "%.0fm", "%.0fm", "%.0fm", "%.0fm", "%.1fkm", "%.1fkm", "%.0fkm", "%.0fkm", "%.0fkm",
        "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm" };
    
    /**
     * This array holds the formatting string used to display the distance on
     * each ring of the radar screen. (This array is for metric measurements.)
     */
    private static String mMetricScaleFormats[] = {
        "%.0fm", "%.0fm", "%.0fm", "%.0fm", "%.0fm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm",
        "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm", "%.0fkm" };
    
    private static float KM_PER_YARDS = 0.0009144f;
    private static float KM_PER_MILES = 1.609344f;
    private static float YARDS_PER_KM = 1093.6133f;
    private static float MILES_PER_KM = 0.621371192f;
    
    /**
     * These are the list of choices for the radius of the outer circle on the
     * screen when using standard units. All items are in kilometers. This array is 
     * used to choose the scale of the radar display.
     */
    private static double mEnglishScaleChoices[] = { 
        100 * KM_PER_YARDS, 200 * KM_PER_YARDS, 400 * KM_PER_YARDS, 1000 * KM_PER_YARDS, 
        1 * KM_PER_MILES, 2 * KM_PER_MILES, 4 * KM_PER_MILES, 8 * KM_PER_MILES, 20 * KM_PER_MILES, 
        40 * KM_PER_MILES, 100 * KM_PER_MILES, 200 * KM_PER_MILES, 400 * KM_PER_MILES, 
        1000 * KM_PER_MILES, 2000 * KM_PER_MILES, 4000 * KM_PER_MILES, 10000 * KM_PER_MILES, 
        20000 * KM_PER_MILES, 40000 * KM_PER_MILES, 80000 * KM_PER_MILES };

    /**
     * Once the scale is chosen, this array is used to convert the number of
     * kilometers on the screen to an integer. (Note that for short distances we
     * use meters, so we multiply the distance by {@link #YARDS_PER_KM}. (This
     * array is for standard measurements.)
     */
    private static float mEnglishDisplayUnitsPerKm[] = { 
        YARDS_PER_KM, YARDS_PER_KM, YARDS_PER_KM, YARDS_PER_KM, MILES_PER_KM, 
        MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, 
        MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM,
        MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM, MILES_PER_KM };

    /**
     * This array holds the formatting string used to display the distance to
     * the target. (This array is for standard measurements.)
     */
    private static String mEnglishDisplayFormats[] = {
        "%.0fyd", "%.0fyd", "%.0fyd", "%.0fyd", "%.1fmi", "%.1fmi", "%.1fmi", "%.1fmi", "%.0fmi", "%.0fmi",
        "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi" };
    
    /**
     * This array holds the formatting string used to display the distance on
     * each ring of the radar screen. (This array is for standard measurements.)
     */
    private static String mEnglishScaleFormats[] = {
        "%.0fyd", "%.0fyd", "%.0fyd", "%.0fyd", "%.2fmi", "%.1fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi",
        "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi", "%.0fmi" };
    
    private String mDistanceView; //The view that will display the distance text
    private Paint mSweepPaint0; //Used to draw the animated ring that sweeps out from the center
    private Paint mSweepPaint1; //Used to draw the animated ring that sweeps out from the center
    private Paint mSweepPaint2; //Used to draw the animated ring that sweeps out from the center
    private float mDistanceRatio; //Ratio of the distance to the target to the radius of the outermost ring on the radar screen
    private long mSweepTime = 1000; //Time in millis when the most recent sweep began
    private boolean mSweepBefore = false; //True if the sweep has not yet intersected the blip  
    private long mBlipTime; //Time in millis when the sweep last crossed the blip
    private Bitmap mBlip; //The bitmap used to draw the target
    private double mBearing = 56; //Bearing to target, in degrees
    private float mOrientation = 25f;
	private String mAngleView; //The view that will display the Angle text
    private double mDistance = 5.75; //Distance to target, in KM
    private boolean mHaveLocation = true; //True when we have know our own location
    private Rect mTextBounds = new Rect(); //Utility rect for calculating the ring labels
    private boolean mUseMetric; //True if the display should use metric units; false if the display should use standard units
    private long mLastGpsFixTime = 0L; //Time in millis for the last time GPS reported a location
    private Location mNetworkLocation; //The last location reported by the network provider. Use this if we can't get a location from GPS

	protected Application mApplication;
    
    private double bearingToTarget  = mBearing - mOrientation;;

    public RadarView(Context context) {
        this(context, null);
    }
    
    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);	

	    // Paint used for the rings and ring text
        mGridPaint = new Paint();
        mGridPaint.setColor(0xFF00FF00);
        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Style.STROKE);
        mGridPaint.setStrokeWidth(1.0f);
        mGridPaint.setTextSize(10.0f);
        mGridPaint.setTextAlign(Align.CENTER);
        
        // Paint used to erase the rectangle behing the ring text
        mErasePaint = new Paint();
        mErasePaint.setColor(0xFF191919);
        mErasePaint.setStyle(Style.FILL);
        
        // Outer ring of the sweep
        mSweepPaint0 = new Paint();
        mSweepPaint0.setColor(0xFF33FF33);
        mSweepPaint0.setAntiAlias(true);
        mSweepPaint0.setStyle(Style.STROKE);
        mSweepPaint0.setStrokeWidth(2f);
        
        // Middle ring of the sweep
        mSweepPaint1 = new Paint();
        mSweepPaint1.setColor(0x7733FF33);
        mSweepPaint1.setAntiAlias(true);
        mSweepPaint1.setStyle(Style.STROKE);
        mSweepPaint1.setStrokeWidth(2f);
        
        // Inner ring of the sweep
        mSweepPaint2 = new Paint();
        mSweepPaint2.setColor(0x3333FF33);
        mSweepPaint2.setAntiAlias(true);
        mSweepPaint2.setStyle(Style.STROKE);
        mSweepPaint2.setStrokeWidth(2f);

        mBlip = BitmapFactory.decodeResource(context.getResources(), R.drawable.blip);
    }

    /**
     * Sets the view that we will use to report Drawing Angle
     * 
     * @param t The text view used to report distance
     */
    public void setAngleView(String value) {
    	mAngleView = value;
    }

    /**
     * Gets the report Drawing Angle
     * 
     */
    public String getAngleView() {
        return mAngleView;
    }

    /**
     * Sets the view that we will use to report distance
     * 
     */
    public void setDistanceView(String t) {
        mDistanceView = t;
    }

    /**
     * Gets the report distance
     * 
     */
    public String getDistanceView() {
        return mDistanceView;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
		
        int centerX = getMeasuredWidth() / 2;
		int centerY = getMeasuredHeight() / 2;
		float factor = 0.2f;
		int radius;
		
		if (centerX > centerY) {
			radius = (int) (centerY * (1 - factor));
		} else {
			radius = (int) (centerX * (1 - factor));
		}

        // Draw the rings
        final Paint gridPaint = mGridPaint;
        canvas.drawCircle(centerX, centerY, radius, gridPaint);
        canvas.drawCircle(centerX, centerY, radius * 3 / 4, gridPaint);
        canvas.drawCircle(centerX, centerY, radius >> 1, gridPaint);
        canvas.drawCircle(centerX, centerY, radius >> 2, gridPaint);
       
        int blipRadius = (int) (mDistanceRatio * radius);
        
        final long now = SystemClock.uptimeMillis();
        if (mSweepTime > 0 && mHaveLocation) {
            // Draw the sweep. Radius is determined by how long ago it started
            long sweepDifference = now - mSweepTime;
            if (sweepDifference < 512L) {
                int sweepRadius = (int) (((radius + 6) * sweepDifference) >> 9);
                canvas.drawCircle(centerX, centerY, sweepRadius, mSweepPaint0);
                canvas.drawCircle(centerX, centerY, sweepRadius - 2, mSweepPaint1);
                canvas.drawCircle(centerX, centerY, sweepRadius - 4, mSweepPaint2);
                
                // Note when the sweep has passed the blip
                boolean before = sweepRadius < blipRadius;
                if (!before && mSweepBefore) {
                    mSweepBefore = false;
                    mBlipTime = now;
                }
            } else {
                mSweepTime = now + 1000;
                mSweepBefore = true;
            }
            postInvalidate();
        }
        
        // Draw horizontal and vertical lines
        canvas.drawLine(centerX, centerY - (radius >> 2) + 6, centerX, centerY - radius - 6, gridPaint);
        canvas.drawLine(centerX, centerY + (radius >> 2) - 6 , centerX, centerY + radius + 6, gridPaint);
        canvas.drawLine(centerX - (radius >> 2) + 6, centerY, centerX - radius - 6, centerY, gridPaint); 
        canvas.drawLine(centerX + (radius >> 2) - 6, centerY, centerX + radius + 6, centerY, gridPaint); 

        // Draw X in the center of the screen
        canvas.drawLine(centerX - 4, centerY - 4, centerX + 4, centerY + 4, gridPaint);
        canvas.drawLine(centerX - 4, centerY + 4, centerX + 4, centerY - 4, gridPaint); 
        
        if (mHaveLocation) {            
            bearingToTarget = mBearing - mOrientation;
            double drawingAngle = (Math.toRadians(bearingToTarget) - (Math.PI / 2));
    
            float cos = (float) Math.cos(drawingAngle);
            float sin = (float) Math.sin(drawingAngle);
    
            // Draw the text for the rings
            final String[] distanceScale = mDistanceScale;
            
            addText(canvas, distanceScale[0], centerX, centerY + (radius >> 2));
            addText(canvas, distanceScale[1], centerX, centerY + (radius >> 1));
            addText(canvas, distanceScale[2], centerX, centerY + radius * 3 / 4);
            addText(canvas, distanceScale[3], centerX, centerY + radius);
    
            // Draw the blip. Alpha is based on how long ago the sweep crossed the blip
            long blipDifference = now - mBlipTime;
            gridPaint.setAlpha(255 - (int)((128 * blipDifference) >> 10));
            canvas.drawBitmap(mBlip, centerX + (cos * blipRadius) - 8 , 
                    centerY + (sin * blipRadius) - 8, gridPaint);
            gridPaint.setAlpha(255);
        }
    }
    
    private void addText(Canvas canvas, String str, int x, int y) {
    	str = str == null ? "-" : str;
        mGridPaint.getTextBounds(str, 0, str.length(), mTextBounds);
        mTextBounds.offset(x - (mTextBounds.width() >> 1), y);
        mTextBounds.inset(-2, -2);
        canvas.drawRect(mTextBounds, mErasePaint);
        canvas.drawText(str, x, y, mGridPaint);
    }

    /**
     * Update our state to reflect a new distance to the target. This may require
     * choosing a new scale for the radar rings.
     * 
     * @param distanceKm The new distance to the target
     */
    public void updateDistance(double distanceKm) {
        final double[] scaleChoices;
        final float[] displayUnitsPerKm;
        final String[] displayFormats;
        final String[] scaleFormats;
        String distanceStr = null;
        if (mUseMetric) {
            scaleChoices = mMetricScaleChoices;
            displayUnitsPerKm = mMetricDisplayUnitsPerKm;
            displayFormats = mMetricDisplayFormats;
            scaleFormats = mMetricScaleFormats;
        } else {
            scaleChoices = mEnglishScaleChoices;
            displayUnitsPerKm = mEnglishDisplayUnitsPerKm;
            displayFormats = mEnglishDisplayFormats;
            scaleFormats = mEnglishScaleFormats;
        }
        
        int count = scaleChoices.length;
        for (int i = 0; i < count; i++) {
            if (distanceKm < scaleChoices[i] || i == (count - 1)) {
                String format = displayFormats[i];
                double distanceDisplay = distanceKm * displayUnitsPerKm[i];
                if (mLastScale != i) {
                    mLastScale = i;
                    String scaleFormat = scaleFormats[i];
                    float scaleDistance = (float) (scaleChoices[i] * displayUnitsPerKm[i]);
                    mDistanceScale[0] = String.format(scaleFormat, (scaleDistance / 4));
                    mDistanceScale[1] = String.format(scaleFormat, (scaleDistance / 2));
                    mDistanceScale[2] = String.format(scaleFormat, (scaleDistance * 3 / 4));
                    mDistanceScale[3] = String.format(scaleFormat, scaleDistance);
                }
                mDistanceRatio = (float) (mDistance / scaleChoices[mLastScale]);
                distanceStr = String.format(format, distanceDisplay);
                break;
            }
        }
        setAngleView(String.format("%.1f�", bearingToTarget));
        setDistanceView(distanceStr);
    }

    /**
     * Update state to reflect whether we are using metric or standard units.
     * 
     * @param useMetric True if the display should use metric units
     */
    public void setUseMetric(boolean useMetric) {
        mUseMetric = useMetric;
        mLastScale = -1;
        if (mHaveLocation) {
            updateDistance(mDistance);
        }
        invalidate();
    }
    
    /**
     * Turn on the sweep animation starting with the next draw
     */
    public void startSweep() {
        mSweepTime = SystemClock.uptimeMillis();
        mSweepBefore = true;
    }
    
    /**
     * Turn off the sweep animation
     */
    public void stopSweep() {
        mSweepTime = 0L;
    }
/*    
	@Override
	public void onLocationChanged(Location location) {
        if (!mHaveLocation) {
            mHaveLocation = true;
        }

        final long now = SystemClock.uptimeMillis();
        boolean useLocation = false;
        final String provider = location.getProvider();
        if (LocationManager.GPS_PROVIDER.equals(provider)) {
            // Use GPS if available
            mLastGpsFixTime = SystemClock.uptimeMillis();
            useLocation = true;
        } else if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
            // Use network provider if GPS is getting stale
            useLocation = now - mLastGpsFixTime > RETAIN_GPS_MILLIS;
            if (mNetworkLocation == null) {
                mNetworkLocation = new Location(location);
            } else {
                mNetworkLocation.set(location);
            }
            
            mLastGpsFixTime = 0L;
        }
        if (useLocation) {
            mMyLocationLat = location.getLatitude();
            mMyLocationLon = location.getLongitude();
    
            mDistance = GeoUtils.distanceKm(mMyLocationLat, mMyLocationLon, mTargetLat,
                    mTargetLon);
    
            mBearing = GeoUtils.bearing(mMyLocationLat, mMyLocationLon, mTargetLat,
                    mTargetLon);
    
            updateDistance(mDistance);
        }
	}
*/

}
