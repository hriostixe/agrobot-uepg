package agrobot.navigo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textview = new TextView(this);
		textview.setText("Navigo � um projeto desenvolvido no laborat�rio de Rob�tica da Universidade Estadual de Ponta Grossa" +
				"	� um sistema de navega��o com corre��o de rota utilizando L�gica Fuzzy");
		setContentView(textview);

	}

	@Override
	public void onBackPressed() {
        NavigoActivity ParentActivity = (NavigoActivity) this.getParent();
        ParentActivity.onBackPressed_Activity();
	} 
	
    protected void onResume() {
        super.onResume();		
	}

}
