package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Answer;
import com.example.wybierzobraz.Algorithm.Choise;

import android.app.Activity;
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
		if(result.doContinue == true) {
			resultText.setText(choise.toString());
			showResult();
			//resultText.setText("DOBRZE");
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
			//resultText.setText("Koniec testu");
		}
	}
	
	private void showResult() {
		resultText.setVisibility(TextView.VISIBLE);
		imgGreenCircle.setVisibility(ImageView.INVISIBLE);
		title.setVisibility(TextView.INVISIBLE);
		img1.setVisibility(ImageView.INVISIBLE);
		img2.setVisibility(ImageView.INVISIBLE);
		}
	
	private void showGreenScreen() {
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
		
		showGreenScreen();
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