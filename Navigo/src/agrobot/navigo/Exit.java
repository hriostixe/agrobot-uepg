package agrobot.navigo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Exit extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textview = new TextView(this);
		textview.setText("");
		setContentView(textview);

	}

	@Override
	public void onBackPressed() {
        NavigoActivity ParentActivity = (NavigoActivity) this.getParent();
        ParentActivity.onBackPressed_Activity();
	} 
	
    protected void onResume() {
        super.onResume();		
        // Ask the user if they want to quit
        onBackPressed();
	}

}
