package com.invengo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.invengo.entity.TracingInfoEntity;
import com.invengo.utils.GsonDataParser;
import com.invengo.utils.NFCUtilies;

public class TracingSuccessActivity extends Activity {

	private TextView mSourceProductName = null;
	private TextView mSourceProductOrgin = null;
	private TextView mSourceProducerDate = null;
	private TextView mSourceProductInDate = null;
	private TextView mSourcePersonInCharge = null;
	private TextView mSourcePersonPhone = null;
	
	private TextView mTracingProductName = null;
	private TextView mTracingSalePlace = null;
	private TextView mTracingSaleDate = null;
	private TextView mTracingSeller = null;
	private TextView mTracingSellerPhone = null;
	
	private Button mBackButton = null;
	
	private GsonDataParser mParser = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracing_success);
		mParser = GsonDataParser.getInstance();
		
		mSourceProductName = (TextView) findViewById(R.id.tracing_text_view_name_id);
		mSourceProductOrgin = (TextView) findViewById(R.id.tracing_text_view_address_id);
		mSourcePersonInCharge = (TextView) findViewById(R.id.tracing_text_view_person_in_charge_id);
		mSourcePersonPhone = (TextView) findViewById(R.id.tracing_text_view_person_phone_id);
		mSourceProducerDate = (TextView) findViewById(R.id.tracing_text_view_producer_date_id);
		mSourceProductInDate = (TextView) findViewById(R.id.tracing_text_view_in_date_id);
		mTracingProductName = (TextView) findViewById(R.id.tracing_out_text_view_name_id);
		mTracingSeller = (TextView) findViewById(R.id.tracing_text_view_seller_id);
		mTracingSellerPhone = (TextView) findViewById(R.id.tracing_text_view_seller_phone_id);
		mTracingSalePlace = (TextView) findViewById(R.id.tracing_text_view_sale_place_id);
		mTracingSaleDate = (TextView) findViewById(R.id.tracing_text_view_sale_date_id);
		
		mBackButton = (Button) findViewById(R.id.button_back_id);
		mBackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//back the main view-InvengoNFCMainActivity
				Intent intent = new Intent(TracingSuccessActivity.this, InvengoNFCMainActivity.class);
				startActivity(intent);
			}
		});
		
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		Intent intent = getIntent();
		String info = intent.getStringExtra(Constants.RESULT);
		
		TracingInfoEntity entity = mParser.parserJsonToEntity(info, TracingInfoEntity.class);
		
		mSourceProductName.setText(entity.getProductName());
		mSourceProductOrgin.setText(entity.getProductOrign());
		mSourcePersonInCharge.setText(entity.getPersonInCharge());
		mSourcePersonPhone.setText(entity.getPersonPhone());
		mSourceProducerDate.setText(NFCUtilies.formatDateToString(entity.getProducerDate()));
		mSourceProductInDate.setText(NFCUtilies.formatDateToString(entity.getProductInDate()));
		mTracingProductName.setText(entity.getProductName());
		mTracingSeller.setText(entity.getSeller());
		mTracingSellerPhone.setText(entity.getSellerPhone());
		mTracingSalePlace.setText(entity.getSalePlace());
		mTracingSaleDate.setText(NFCUtilies.formatDateToString(entity.getSaleDate()));
		
	}
	
}
