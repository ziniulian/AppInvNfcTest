package com.invengo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AuthenticateFailureActivity extends Activity {

	private TextView mFailureDetail;
	private Button mBackButton;
	private TextView mTagUid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate_failure);
		
		mTagUid = (TextView) findViewById(R.id.text_view_authenticate_failure_uid_id);
		mFailureDetail = (TextView) findViewById(R.id.text_view_failure_reason_detail_id);
		mBackButton = (Button) findViewById(R.id.button_failure_back_id);
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(AuthenticateFailureActivity.this, InvengoNFCMainActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		String result = intent.getStringExtra(Constants.RESULT);
		String[] temp = result.split("-");
		mFailureDetail.setText(temp[0]);
		mTagUid.setText(temp[1]);
	}

}
