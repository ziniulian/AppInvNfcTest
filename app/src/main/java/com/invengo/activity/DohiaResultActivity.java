package com.invengo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class DohiaResultActivity extends Activity {

	private ImageView mDohiaResultImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dohia_result);
		setTitle(R.string.title_activity_dohia_nfc);

		mDohiaResultImageView = (ImageView) findViewById(R.id.image_view_dohia_result);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent dohiaIntent = getIntent();

		mDohiaResultImageView.setScaleType(ImageView.ScaleType.FIT_XY);
		mDohiaResultImageView.setImageDrawable(getResources().getDrawable(
				dohiaIntent.getIntExtra("DOHIA", R.drawable.dohia_vip)));
	}

}
