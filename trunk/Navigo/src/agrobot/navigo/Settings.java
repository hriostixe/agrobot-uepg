package agrobot.navigo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.serialport.SerialPortFinder;

public class Settings extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;
	
	public void enableTabInActivity(int indexTabToSwitchTo, boolean status){
        NavigoActivity ParentActivity;
        ParentActivity = (NavigoActivity) this.getParent();
        ParentActivity.enableTab(indexTabToSwitchTo, status);
	}

	@Override
	public void onBackPressed() {
        NavigoActivity ParentActivity = (NavigoActivity) this.getParent();
        ParentActivity.onBackPressed_Activity();
	} 
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		final SharedPreferences sp = getSharedPreferences("agrobot.navigo_preferences", MODE_PRIVATE);

		addPreferencesFromResource(R.xml.serial_port_preferences);

		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				enableTabInActivity(0, ! ((newValue.toString().length() == 0) || 
										  (Integer.decode(sp.getString("BAUDRATE", "-1")) == -1)));	
				return true;
			}
		});

		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
		baudrates.setSummary(baudrates.getValue());
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				enableTabInActivity(0, ! ((newValue.toString().length() == 0) || 
										  (sp.getString("DEVICE", "").length() == 0)));
				return true;
			}
		});

	}
}
