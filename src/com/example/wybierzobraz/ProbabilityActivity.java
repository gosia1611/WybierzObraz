package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Phase;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ProbabilityActivity extends Activity {
	private SeekBar setProbability = null;
	private TextView probabilityValue;
	private ImageView image;
	private TextView endText;
	private Button button = null;
	Algorithm algorithm = new Algorithm();
	Integer[] images;
	int imageNumber = 0;
	int progressChanged = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_probability);
		
		algorithm.setPhase(Phase.PROBABILITY_PHASE);
    	images = algorithm.getImages();
    	
    	endText = (TextView) findViewById(R.id.textView3);
    	
    	image = (ImageView) findViewById(R.id.imageView1);
    	image.setImageResource(images[0]);

		probabilityValue = (TextView) findViewById(R.id.textView2);
		probabilityValue.setText("0");
		
		setProbability = (SeekBar) findViewById(R.id.seekBar1);

		setProbability.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
				progressChanged = progress;
				probabilityValue.setText(Integer.toString(progress));
			}
	
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
	
			public void onStopTrackingTouch(SeekBar seekBar) {
				probabilityValue.setText(Integer.toString(progressChanged));
			}
		});
		
        button = (Button) findViewById(R.id.button_ok);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	algorithm.addProbabilityToFile(imageNumber+1, progressChanged);
            	performOk();
            }
        });
	}
	
    private void performOk() {
    imageNumber++;
    setProbability.setProgress(0);
    probabilityValue.setText("0");
    
   	switch (imageNumber) {
    	case 1: 
    		image.setImageResource(images[imageNumber]); break;
    	case 2: 
    		image.setImageResource(images[imageNumber]); break;
    	case 3: 
    		image.setImageResource(images[imageNumber]); break;
    	case 4: 
    		image.setImageResource(images[imageNumber]); break;
    	case 5:
    		image.setImageResource(images[imageNumber]); break;
    	case 6:
    		algorithm.writeProbabilitiesFile();
    		((TextView) findViewById(R.id.textView1)).setVisibility(View.INVISIBLE);
    		button.setVisibility(View.INVISIBLE);
    		setProbability.setVisibility(View.INVISIBLE);
    		probabilityValue.setVisibility(View.INVISIBLE);
    		image.setVisibility(View.INVISIBLE);
    		endText.setVisibility(View.VISIBLE);
    		Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
					  System.exit(0);
				  }
				}, 10000)
			; break;
    	}
    	
    }
}
