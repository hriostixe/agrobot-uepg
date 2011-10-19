/**
 * 
 */
package android.draw;

/**
 * @author Herbert von Broeuschmeul
 *
 */
public interface NmeaListener {
	abstract void onNmeaReceived(long timestamp, String nmea);
}
