package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Answer;
import com.example.wybierzobraz.Algorithm.Choice;
import com.example.wybierzobraz.Algorithm.Phase;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ImageView img1;
	private ImageView img2;
	int image1;
	int image2;
	int chosenImage;
	private ImageView imgGreenCircle;
	private TextView title;
	private TextView resultText;
	private TextView text;
	private Button newPhaseButtonOk;
	Algorithm algorithm = new Algorithm();
	Algorithm.Choice lastChoice;
	Runnable notingChoosenRunnable = new Runnable() {
		  public void run() {
			  onImageClicked(Choice.NOTHING);
		  }
	  };
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		handler = new Handler();
		init();

		showImages();
	}

	private void init() {
		img1 = (ImageView) findViewById(R.id.imageView1);
		img1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onImageClicked(Choice.LEFT);
			}
		});

		img2 = (ImageView) findViewById(R.id.imageView2);
		img2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onImageClicked(Choice.RIGHT);
			}
		});

		imgGreenCircle = (ImageView) findViewById(R.id.imageView3);
		imgGreenCircle.setImageResource(R.drawable.green_circle);
		title = (TextView) findViewById(R.id.textView1);
		resultText = (TextView) findViewById(R.id.textView2);
		text = (TextView) findViewById(R.id.textView3);

		newPhaseButtonOk = (Button) findViewById(R.id.button_ok);
		newPhaseButtonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hidePhaseIntroductionView();
				showImages();
			}
		});
	}

	private void onImageClicked(Choice choice) {
		handler.removeCallbacks(notingChoosenRunnable);
		lastChoice = choice;
		hideImagesView();

		if (choice == Choice.LEFT) {
			chosenImage = image1;
		} else if (choice == Choice.RIGHT) {
			chosenImage = image2;
		}
		
		final Answer result = algorithm.getResult(choice, chosenImage);
		int delay = 0;
		if (result.phase == Phase.LEARNING_PHASE) {
			showResult(result.goodAnswer, choice);
			delay = 3000;
		}
		if (result.doContinue == true) {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					hideResultView();
					if (result.ifPhaseChanged == true) {
						showPhaseIntroductionView(result.phase);
					}
					else
						showImages();				
				}
			}, delay);
		} else {
			resultText.setText("Koniec testu");
			resultText.setTextColor(Color.BLUE);
			resultText.setVisibility(View.VISIBLE);
		}
	}

	private void showResult(boolean isGoodAnswer, Choice choice) {
		if (choice == Choice.NOTHING) {
			resultText.setText("NIE WYBRANO");
			resultText.setTextColor(Color.BLUE);
		} else if (isGoodAnswer == true) {
			resultText.setText("DOBRZE");
			resultText.setTextColor(Color.GREEN);
		} else if (isGoodAnswer == false) {
			resultText.setText("èLE");
			resultText.setTextColor(Color.RED);
		}
		showResultView();
	}	

	private void showImages() {
		Integer[] images = algorithm.getImages();
		img1.setImageResource(images[0]);
		img2.setImageResource(images[1]);
		image1 = images[0];
		image2 = images[1];
		
		showGreenCircleView();
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  hideGreenCircleView();
			  showImagesView();
			  
			  handler.postDelayed(notingChoosenRunnable, 3000);
		  }
		}, 3000);	
	}

	private void showGreenCircleView() {
		imgGreenCircle.setVisibility(View.VISIBLE);
	}
	
	private void hideGreenCircleView() {
		imgGreenCircle.setVisibility(View.INVISIBLE);
	}
	
	private void showResultView() {
		resultText.setVisibility(View.VISIBLE);
	}
	
	private void hideResultView() {
		resultText.setVisibility(View.INVISIBLE);
	}
	
	private void showImagesView() {
		title.setVisibility(View.VISIBLE);
		img1.setVisibility(View.VISIBLE);
		img2.setVisibility(View.VISIBLE);
	}
	
	private void hideImagesView() {
		title.setVisibility(View.INVISIBLE);
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
	}
	
	private void showPhaseIntroductionView(Algorithm.Phase phase) {
		text.setVisibility(View.VISIBLE);
		newPhaseButtonOk.setVisibility(View.VISIBLE);
		switch (phase) {
		case ZERO_PHASE:
			text.setText(R.string.tekst7);
			break;
		case TRIAL_PHASE:
			text.setText(R.string.tekst7);
			break;
		case LEARNING_PHASE:
			text.setText(R.string.tekst7);
			break;
		case TEST_PHASE:
			text.setText(R.string.tekst7);
			break;
		case END_PHASE:
			text.setText(R.string.tekst7);
			break;
		default:
			text.setText(R.string.error);
		}
	}
	
	private void hidePhaseIntroductionView() {
		text.setVisibility(View.INVISIBLE);
		newPhaseButtonOk.setVisibility(View.INVISIBLE);
	}
}