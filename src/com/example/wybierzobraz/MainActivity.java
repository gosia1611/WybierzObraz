package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Answer;
import com.example.wybierzobraz.Algorithm.Choise;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ImageView img1;
	private ImageView img2;
	private ImageView imgGreenCircle;
	private TextView title;
	private TextView resultText;
	Algorithm algorithm = new Algorithm();
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();

		try {
			showImages();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
		img1 = (ImageView) findViewById(R.id.imageView1);
		img1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				alert(Choise.LEFT);
			}
		});

		img2 = (ImageView) findViewById(R.id.imageView2);
		img2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				alert(Choise.RIGHT);
			}
		});
		
		imgGreenCircle = (ImageView) findViewById(R.id.imageView3);
		imgGreenCircle.setImageResource(R.drawable.green_circle);
		title = (TextView) findViewById(R.id.textView1);
		resultText = (TextView) findViewById(R.id.textView2);
	}

	private void alert(Choise choise) {
		Answer result = algorithm.getResult(choise, 0);
		if (result.getPhaseChanged() == true) {
			switch (algorithm.phase) {
			case ZERO_PHASE:
				((TextView) findViewById(R.id.textView2)).setText(R.string.tekst7);
				break;
			case TRIAL_PHASE:
				((TextView) findViewById(R.id.textView2)).setText(R.string.tekst7);
				break;
			case LEARNING_PHASE:
				((TextView) findViewById(R.id.textView2)).setText(R.string.tekst7);
				break;
			case TEST_PHASE:
				((TextView) findViewById(R.id.textView2)).setText(R.string.tekst7);
				break;
			case END_PHASE:
				((TextView) findViewById(R.id.textView2)).setText(R.string.tekst7);
				break;
			default:
				((TextView) findViewById(R.id.textView2)).setText(R.string.error);
			}
		} else {
			if (result.getDoContinue() == true) {
				if (result.getGoodAnswer() == true) {
					resultText.setText("DOBRZE");
					resultText.setTextColor(Color.GREEN);
				} else {
					resultText.setText("èLE");
					resultText.setTextColor(Color.RED);
				}
				showResult();
				handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						try {
							showImages();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 3000);
			} else {
				// resultText.setText("Koniec testu");
			}
		}
	}
	
	/*private void showScreen() {
		if(algorithm.getResult().
	}*/
	
	private void showResult() {
		resultText.setVisibility(TextView.VISIBLE);
		imgGreenCircle.setVisibility(ImageView.INVISIBLE);
		title.setVisibility(TextView.INVISIBLE);
		img1.setVisibility(ImageView.INVISIBLE);
		img2.setVisibility(ImageView.INVISIBLE);
		}
	
	private void showGreenCircle() {
		imgGreenCircle.setVisibility(ImageView.VISIBLE);
		title.setVisibility(TextView.INVISIBLE);
		img1.setVisibility(ImageView.INVISIBLE);
		img2.setVisibility(ImageView.INVISIBLE);
		resultText.setVisibility(TextView.INVISIBLE);
	}

	private void showImages() throws InterruptedException {
		Integer[] images = algorithm.getImages();
		img1.setImageResource(images[0]);
		img2.setImageResource(images[1]);
		
		showGreenCircle();
		handler = new Handler();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  imgGreenCircle.setVisibility(ImageView.INVISIBLE);
			  title.setVisibility(TextView.VISIBLE);
			  img1.setVisibility(ImageView.VISIBLE);
			  img2.setVisibility(ImageView.VISIBLE);
		  }
		}, 3000);
	}
}