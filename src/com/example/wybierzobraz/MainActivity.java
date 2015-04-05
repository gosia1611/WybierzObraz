package com.example.wybierzobraz;

import com.example.wybierzobraz.Algorithm.Answer;
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
	Algorithm algorithm = new Algorithm();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		
		showImages();
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
	}

	private void alert(Choise choise) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog
		// characteristics
		
		Answer result = algorithm.getResult(choise, 0);
		if(result.doContinue == true) {
		builder.setMessage("Wybra³eœ " + choise.toString()).setTitle("Wybrales obraz");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				showImages();
			}
		});
		} else {
			builder.setMessage("Koniec testu");
		}
		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	private void showImages() {
		Integer[] images = algorithm.getImages();
		img1.setImageResource(images[0]);
		img2.setImageResource(images[1]);
	}

}
