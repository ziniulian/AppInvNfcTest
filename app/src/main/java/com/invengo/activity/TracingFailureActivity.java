package com.invengo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TracingFailureActivity extends Activity {

	private TextView mFailureDetail;
	private Button mBackButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracing_failure);
		
		mFailureDetail = (TextView) findViewById(R.id.text_view_tracing_failure_reason_detail_id);
		mBackButton = (Button) findViewById(R.id.button_tracing_failure_back_id);
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(TracingFailureActivity.this, InvengoNFCMainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String result = intent.getStringExtra(Constants.RESULT);
		mFailureDetail.setText(result);
	}
	
}
