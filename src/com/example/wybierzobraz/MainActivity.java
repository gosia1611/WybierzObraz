package com.example.wybierzobraz;

import java.util.Random;

import com.example.wybierzobraz.Algorithm.State;
import com.example.wybierzobraz.Algorithm.Choice;
import com.example.wybierzobraz.Algorithm.Phase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
	int i=0;
	Phase phase2;
	private ImageView imgGreenCircle;
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
		final Context context = this;
		handler = new Handler();
		init(context);

		showImages();
	}

	private void init(final Context context) {
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
		resultText = (TextView) findViewById(R.id.textView2);
		text = (TextView) findViewById(R.id.textView3);

		newPhaseButtonOk = (Button) findViewById(R.id.button_ok);
		newPhaseButtonOk.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if ((phase2 == Phase.TEST_PHASE) && (i == 0)) {
					i=1;
					showPhaseIntroductionView(phase2);
				} else if (phase2 == Phase.PROBABILITY_PHASE){
					algorithm.writeFile();
					Intent intent = new Intent(context, ProbabilityActivity.class);
		    		startActivity(intent);
		    		finish();
				} else {
					hidePhaseIntroductionView();
					showImages();
				}
				
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

		int delay = 0;
		boolean answer = algorithm.isGoodAnswer(choice, chosenImage);
		if (phase2 == Phase.TRIAL_PHASE || phase2 == Phase.LEARNING_PHASE) {
			showResult(answer, choice);
			delay = 3000;
		}
		
		if (phase2 == Phase.LEARNING_PHASE || phase2 == Phase.TEST_PHASE) {
		algorithm.addToFile(image1, image2, choice, answer);
		}

		final State result = algorithm.getUpdatedState();
		phase2 = algorithm.getCurrentPhase();
		
		/*if (result.doContinue == true) {*/
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					hideResultView();
					if (result.ifPhaseChanged == true) {
						showPhaseIntroductionView(phase2);
					}
					else
						showImages();		
				}
			}, delay);
	}

	private void showResult(boolean isGoodAnswer, Choice choice) {
		if (choice == Choice.NOTHING) {
			resultText.setText("NIE WYBRANO");
			resultText.setTextColor(Color.RED);
		} else if (isGoodAnswer == true) {
			resultText.setText("DOBRZE");
			resultText.setTextColor(Color.BLUE);
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
		
		phase2 = algorithm.getCurrentPhase();
		if (phase2 != Phase.ZERO_PHASE) {
		showGreenCircleView();
		Random rand = new Random(System.currentTimeMillis());
		int randomNumber = rand.nextInt(2)*1000+1000;
		handler.postDelayed(new Runnable() {
		  @Override
		  public void run() {
			  hideGreenCircleView();
			  showImagesView();
			  
			  handler.postDelayed(notingChoosenRunnable, 3000);
		  }
		}, randomNumber);	
		} else {
			showImagesView();
		}
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
		img1.setVisibility(View.VISIBLE);
		img2.setVisibility(View.VISIBLE);
	}
	
	private void hideImagesView() {
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
	}
	
	private void showPhaseIntroductionView(Algorithm.Phase phase) {
		text.setVisibility(View.VISIBLE);
		newPhaseButtonOk.setVisibility(View.VISIBLE);
		switch (phase) {
		case TRIAL_PHASE:
			text.setText(R.string.tekst5);
			break;
		case LEARNING_PHASE:
			text.setText(R.string.tekst6);
			break;
		case TEST_PHASE:
			if (i == 0) {
				text.setText(R.string.tekst7);
			} else if (i == 1) {
				text.setText(R.string.tekst8);
			}
			break;
		case PROBABILITY_PHASE:
			text.setText(R.string.tekst10);
			break;
		case END_PHASE:
			text.setText(R.string.tekst9);
			newPhaseButtonOk.setVisibility(View.INVISIBLE);
			handler.postDelayed(new Runnable() {
				  @Override
				  public void run() {
					  System.exit(0);
				  }
				}, 10000);
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