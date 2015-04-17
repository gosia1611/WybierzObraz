package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Choise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private ImageView img1;
	private ImageView img2;
	private ImageView imgGreenCircle;
	Algorithm algorithm = new Algorithm();

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
		
		imgGreenCircle = (ImageView) findViewById(R.id.imageView2);
		imgGreenCircle.setImageResource(R.drawable.green_circle);
	}

	private void alert(Choise choise) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog
		// characteristics
		algorithm.getAnswer(choise);
		builder.setMessage("Wybra³eœ " + choise.toString()).setTitle("Wybrales obraz");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				try {
					showImages();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		dialog.show();
	}

	private void showImages() throws InterruptedException {
		Integer[] images = algorithm.getImages();
		imgGreenCircle.setVisibility(View.VISIBLE);
		img1.setVisibility(View.INVISIBLE);
		img2.setVisibility(View.INVISIBLE);
		Thread.sleep(3000);
		imgGreenCircle.setVisibility(View.INVISIBLE);
		img1.setVisibility(View.VISIBLE);
		img2.setVisibility(View.VISIBLE);
		img1.setImageResource(images[0]);
		img2.setImageResource(images[1]);
	}
}
