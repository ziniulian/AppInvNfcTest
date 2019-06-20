package com.invengo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.invengo.entity.AuthenticationInfoEntity;
import com.invengo.entity.TracingInfoEntity;
import com.invengo.utils.GsonDataParser;
import com.invengo.utils.NFCUtilies;
import com.invengo.utils.VolleyErrorManager;
import com.invengo.utils.VolleyHttpRequestManager;

public class AuthenticateSuccessActivity extends Activity {

	private static final String LOG_TAG = "AuthenticateSuccessActivity";

	private TextView mNameTextView = null;
	private TextView mTypeTextView = null;
	private TextView mBatchTextView = null;
	private TextView mOrginTextView = null;
	private TextView mPhoneTextView = null;
	private TextView mProducerTextView = null;
	private TextView mNumberTextView = null;
	private TextView mTimeTextView = null;
	private TextView mTagUid = null;
	private GsonDataParser mParser = null;
	private VolleyHttpRequestManager mRequestManager = null;
	private VolleyErrorManager mErrorManager = null;

	private ImageView mProductImageView = null;
	private Button mAuthenticateButton = null;
	private AuthenticationInfoEntity mEntity = null;
	private LinearLayout mLinearLayout = null;

	private static final String TRACING_QUERY_TAG = "TRACING_QUERY_TAG";
	private static final String TRACING_QUERY_PIC_TAG = "TRACING_QUERY_PIC_TAG";
	private static final float VIEW_WIDTH = 430;
	private static final float VIEW_HEIGHT = 510;
	private boolean IS_RUNNING = false;
	private static final String PATH = "http://219.134.89.66:7171/InvengoAuthenticationServer/authenticate/tracingQuery";
	private static final String PIC_PATH = "http://219.134.89.66:7171/InvengoAuthenticationServer/authenticate/download";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate_success);
		mParser = GsonDataParser.getInstance();
//		mRequestManager = VolleyHttpRequestManager.getInstance(getApplicationContext());
		mRequestManager = VolleyHttpRequestManager.getInstance(this);
		mErrorManager = VolleyErrorManager.getInstance(this);

		mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout_image_id);

		mNameTextView = (TextView) findViewById(R.id.text_view_name_id);
		mTypeTextView = (TextView) findViewById(R.id.text_view_type_id);
		mBatchTextView = (TextView) findViewById(R.id.text_view_tax_id);
		mOrginTextView = (TextView) findViewById(R.id.text_view_address_id);
		mPhoneTextView = (TextView) findViewById(R.id.text_view_phone_id);
		mProducerTextView = (TextView) findViewById(R.id.text_view_producer_id);
		mNumberTextView = (TextView) findViewById(R.id.text_view_authenticate_number_id);
		mTimeTextView = (TextView) findViewById(R.id.text_view_authenticate_time_id);
		mTagUid = (TextView) findViewById(R.id.text_view_authenticate_uid_id);

		mAuthenticateButton = (Button) findViewById(R.id.button_query_id);
		mAuthenticateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				tracingQuery();
			}
		});

		mProductImageView = (ImageView) findViewById(R.id.image_view_product_id);
		mProductImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Log.i(LOG_TAG, "Refresh Product Image.");
				tracingQueryPic();
			}
		});
	}

	private void tracingQueryPic(){
		if(mRequestManager.checkNetState(this)){
			if(!IS_RUNNING){
				IS_RUNNING = true;
				new Thread(imageRunnable).start();
			}
		}else{
			Toast.makeText(this, R.string.toast_network_disabled, Toast.LENGTH_SHORT).show();
		}
	}

	private void tracingQuery() {
		if(mRequestManager.checkNetState(this)){
			if(!IS_RUNNING){
				IS_RUNNING = true;
				new Thread(runnable).start();
			}
		}else{
			Toast.makeText(this, R.string.toast_network_disabled, Toast.LENGTH_SHORT).show();
		}
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			while(IS_RUNNING == true){
				try{
					Intent intent = getIntent();
					String successInfo = intent.getStringExtra(Constants.RESULT);
					AuthenticationInfoEntity entity = mParser.parserJsonToEntity(successInfo, AuthenticationInfoEntity.class);

					String tagUid = entity.getUid();
					String URL_PATH = mRequestManager.getURL(PATH, tagUid);

					mRequestManager.doStringRequest(TRACING_QUERY_TAG, URL_PATH, stringListener, stringErrorListener);
				}finally{
					IS_RUNNING = false;
				}
			}
		}
	};

	private Response.Listener<String> stringListener = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			TracingInfoEntity tracingEntity = mParser.parserJsonToEntity(response, TracingInfoEntity.class);
			if(null != tracingEntity){
				int result = tracingEntity.getResult();
				if(Constants.SUCCESS == result){
					startResultActivity(response, TracingSuccessActivity.class);
					return;
				}else if(Constants.FAILURE == result){
					startResultActivity(getString(R.string.toast_tag_tracing_failure), TracingFailureActivity.class);
					return;
				}
			}else {
				startResultActivity(getString(R.string.toast_tag_tracing_failure), TracingFailureActivity.class);
				return;
			}
		}
	};

	private Response.ErrorListener stringErrorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			String errorInfo = mErrorManager.getErrorMessage(error);
			startResultActivity(errorInfo, TracingFailureActivity.class);
		}

	};

	private void startResultActivity(String result, Class<?> cls) {
		Intent intent = new Intent(AuthenticateSuccessActivity.this, cls);
		intent.putExtra(Constants.RESULT, result);
		startActivity(intent);
	};

	@Override
	protected void onResume() {
		super.onResume();

		Intent intent = getIntent();
		String successInfo = intent.getStringExtra(Constants.RESULT);

		mEntity = mParser.parserJsonToEntity(successInfo, AuthenticationInfoEntity.class);

		mNameTextView.setText(mEntity.getName());
		mTypeTextView.setText(mEntity.getType());
		mBatchTextView.setText(mEntity.getBatch());
		mOrginTextView.setText(mEntity.getOrgin());
		mPhoneTextView.setText(mEntity.getPhone());
		mProducerTextView.setText(mEntity.getProducer());
		mNumberTextView.setText(String.valueOf(mEntity.getNumber()));
		mTimeTextView.setText(NFCUtilies.formatDateToString(mEntity.getTime()));
		mTagUid.setText(mEntity.getUid());

		//加载产品图片
        tracingQueryPic();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mRequestManager.cancleSingleRequest(TRACING_QUERY_PIC_TAG);
		mRequestManager.cancleSingleRequest(TRACING_QUERY_TAG);
	}

	private Runnable imageRunnable = new Runnable() {
		public void run() {
			while(IS_RUNNING == true){
				try{
					Intent intent = getIntent();
					String successInfo = intent.getStringExtra(Constants.RESULT);
					AuthenticationInfoEntity entity = mParser.parserJsonToEntity(successInfo, AuthenticationInfoEntity.class);

					String tagUid = entity.getUid();
					String URL_PATH = mRequestManager.getURL(PIC_PATH, tagUid);
					mRequestManager.doImageRequest(TRACING_QUERY_PIC_TAG, URL_PATH, imageSuccessListener, imageErrorListener);
				}finally{
					IS_RUNNING = false;
				}
			}
		}
	};

	private Response.Listener<Bitmap> imageSuccessListener = new Response.Listener<Bitmap>() {

		@Override
		public void onResponse(Bitmap bitmap) {
			int imageWidth = bitmap.getWidth();
			int imageHeight = bitmap.getHeight();
			if(VIEW_WIDTH < imageWidth & VIEW_HEIGHT < imageHeight){
				mProductImageView.setScaleType(ScaleType.FIT_CENTER);
				mProductImageView.setImageBitmap(bitmap);
				return;
			}

			Matrix matrix = new Matrix();
			float scaleWidth = 1.1f;
			float scaleHeight = 1.1f;
			if(VIEW_WIDTH > imageWidth){
				scaleWidth = scaleWidth * (VIEW_WIDTH / imageWidth);
			}
			if(VIEW_HEIGHT > imageHeight){
				scaleHeight = scaleHeight * (VIEW_HEIGHT / imageHeight);
			}
			matrix.postScale(scaleWidth, scaleHeight);
			try{
				Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, imageWidth, imageHeight, matrix, true);
				mProductImageView.setScaleType(ScaleType.FIT_CENTER);
				mProductImageView.setImageBitmap(newBitmap);
			}catch (IllegalArgumentException  e) {
				e.printStackTrace();
			}
		}

	};

	private Response.ErrorListener imageErrorListener = new Response.ErrorListener() {
		public void onErrorResponse(VolleyError error) {
			String errorInfo = mErrorManager.getErrorMessage(error);
			Log.i(LOG_TAG, errorInfo);
			Toast.makeText(AuthenticateSuccessActivity.this, errorInfo, Toast.LENGTH_SHORT).show();
		};
	};
}
