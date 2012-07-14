package agrobot.navigo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView textview = new TextView(this);
		textview.setText("Navigo é um projeto desenvolvido no laboratório de Robótica da Universidade Estadual de Ponta Grossa" +
				"	É um sistema de navegação com correção de rota utilizando Lógica Fuzzy");
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
