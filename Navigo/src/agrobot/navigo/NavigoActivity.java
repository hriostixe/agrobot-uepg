package agrobot.navigo;

import com.google.android.maps.GeoPoint;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
/* teste comentário anderson para subversion */
public class NavigoActivity extends TabActivity {

	private TabHost tabHost;
	public static GeoPoint targetPoint;

	public NavigoActivity() {
	}

	public void switchTab(int tab) {
		tabHost.setCurrentTab(tab);
	}

	public void enableTab(int tab, boolean active) {
		tabHost.getTabWidget().getChildTabViewAt(tab).setEnabled(active);
	}

	public void onBackPressed_Activity() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.really_quit)
				.setCancelable(false)
				.setPositiveButton(R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						})
				.setNegativeButton(R.string.no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Resources res = getResources(); // Resource object to get Drawables
		tabHost = getTabHost(); // The activity TabHost

		tabHost.getTabWidget().setDividerDrawable(
				res.getDrawable(R.drawable.tab_divider));

		setupTab("maps", res.getString(R.string.ac_maps),
				res.getDrawable(R.drawable.ic_tab_maps),
				new Intent().setClass(this, AndroidMapViewActivity.class));
		setupTab("navigation", res.getString(R.string.ac_navigation),
				res.getDrawable(R.drawable.ic_tab_navigation),
				new Intent().setClass(this, Navigation.class));
		setupTab("settings", res.getString(R.string.ac_settings),
				res.getDrawable(R.drawable.ic_tab_settings),
				new Intent().setClass(this, Settings.class));
		setupTab("about", res.getString(R.string.ac_about),
				res.getDrawable(R.drawable.ic_tab_about),
				new Intent().setClass(this, Settings.class));
		setupTab("exit", res.getString(R.string.ac_exit),
				res.getDrawable(R.drawable.ic_tab_exit),
				new Intent().setClass(this, Exit.class));

		switchTab(0);
	}

	private void setupTab(final String tag, final String caption,
			final Drawable icon, final Intent intent) {
		View tabView = createTabView(tabHost.getContext(), caption, icon);
		TabHost.TabSpec spec;

		spec = tabHost.newTabSpec(tag);
		spec.setIndicator(tabView);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	private static View createTabView(final Context context, final String text,
			final Drawable icon) {

		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);

		ImageView iv = (ImageView) view.findViewById(R.id.tabsIcon);
		iv.setImageDrawable(icon);

		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

}
