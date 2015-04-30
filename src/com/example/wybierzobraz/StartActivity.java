package com.example.wybierzobraz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends Activity {
	private int pageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        
        final Button button = (Button) findViewById(R.id.button_ok);
        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	performOk(context);
            }
        });
    }
   
    private void performOk(Context context) {
    	pageNumber++;
    	switch (pageNumber) {
    	case 2: 
    		((TextView)findViewById(R.id.textView1)).setText(R.string.tekst2); break;
    	case 3: 
    		((TextView)findViewById(R.id.textView1)).setText(R.string.tekst3); break;
    	case 4: 
    		((TextView)findViewById(R.id.textView1)).setText(R.string.tekst4); break;
    	case 5:
    		Intent intent = new Intent(context, MainActivity.class);
    		startActivity(intent);
    		finish(); break;
    }
    }
}