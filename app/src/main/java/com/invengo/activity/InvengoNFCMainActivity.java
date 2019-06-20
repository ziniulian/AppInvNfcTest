package com.invengo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.invengo.entity.AuthenticationInfoEntity;
import com.invengo.tag.NFCMifareUltralightTagHolder;
import com.invengo.tag.NFCTagRWProcessor;
import com.invengo.utils.GsonDataParser;
import com.invengo.utils.NFCUtilies;
import com.invengo.utils.VolleyErrorManager;
import com.invengo.utils.VolleyHttpRequestManager;

public class InvengoNFCMainActivity extends Activity {

	private static final String LOG_TAG = "MainActivity";

	private NfcAdapter mNfcAdapter = null;
	private PendingIntent mPendingIntent = null;
	private IntentFilter[] mFilter = null;
	private String[][] mTechList = null;
	private NFCTagRWProcessor mTagProcessor = null;
	private GsonDataParser mParser = null;
	private static boolean IS_RUNNING = false;
	private static final int FAILURE_RESPONSE = 0;

	private static final String mRandomHexString = "B1B204C5";
	private static final String URL_PATH = "http://219.134.89.66:7171/InvengoAuthenticationServer/authenticate/authenticate";
	private VolleyHttpRequestManager mRequestManager = null;
	private VolleyErrorManager mErrorManager = null;
	private static final String AUTHENTICATE_QUREY_TAG = "AUTHENTICATE_QUREY_TAG";
	private String uid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTagProcessor = NFCTagRWProcessor.getInstance();
		mParser = GsonDataParser.getInstance();
		mRequestManager= VolleyHttpRequestManager.getInstance(this);
		mErrorManager = VolleyErrorManager.getInstance(this);

		checkNfcEnabled();

		/*
		 * NFC foreground dispatch.Start
		 */

		Intent intent = new Intent(this, getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		mFilter = new IntentFilter[]{filter};

//		mTechList = new String[][]{new String[]{NfcA.class.getName(), NdefFormatable.class.getName(), IsoDep.class.getName()}};
		mTechList = new String[][]{new String[]{NfcA.class.getName(), NdefFormatable.class.getName()}};
		/*
		 * NFC foreground dispatch.End
		 */
	}

	private void checkNfcEnabled() {
		NfcManager nfcManager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
		mNfcAdapter = nfcManager.getDefaultAdapter();
		if(null == mNfcAdapter){//无NFC功能
			Toast.makeText(this, R.string.toast_nfc_disabled, Toast.LENGTH_LONG).show();
			return;
		}else{
			if(!mNfcAdapter.isEnabled()){//NFC功能关闭
				FragmentManager manager = this.getFragmentManager();
				DialogNoticeFragment noticeFragment = new DialogNoticeFragment();
				noticeFragment.show(manager, null);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(null != mNfcAdapter){
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilter, mTechList);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(null != mNfcAdapter){
			mNfcAdapter.disableForegroundDispatch(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mRequestManager.cancleSingleRequest(AUTHENTICATE_QUREY_TAG);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(NfcAdapter.ACTION_TECH_DISCOVERED == intent.getAction()){
			setIntent(intent);
			processIntent();
		}
	}

	private void processIntent() {

		Log.i(LOG_TAG, getIntent().getAction());
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
			while (IS_RUNNING) {

				Intent intent = getIntent();
				NFCMifareUltralightTagHolder tagHolder = new NFCMifareUltralightTagHolder(intent);
				mTagProcessor.setTagHolder(tagHolder);

				String tagUid = NFCUtilies.convertByteArrayToHexString(tagHolder.getUid());//For authenticate
				uid = tagUid;
				byte[] randOne = NFCUtilies.convertHexStringToByteArray(mRandomHexString);//For tag

				try{
					//Connect the nfc tag
					mTagProcessor.connect();
					mTagProcessor.setTimeout(3 * 1000);

					//consult the F0 command
					byte[] commandWrite = new byte[2 + randOne.length];
					commandWrite[0] = Constants.WRITE_CMD;
					commandWrite[1] = Constants.AUTHEN_WRITE_CMD_FIRST;
					System.arraycopy(randOne, 0, commandWrite, 2, randOne.length);

					byte[] responseData = mTagProcessor.authenticate(commandWrite);
					if(responseData[0] == Constants.ACK){
						//consult the F1 command
						byte[] cmdRead_one = new byte[]{0x30, (byte) 0xF1};
						responseData = mTagProcessor.authenticate(cmdRead_one);

						if(responseData.length == 1){//Error occured when send command-0xF1
							if(responseData[0] == Constants.UN_CONNECTED){//Disconnect when send command-0xF1
								sendHandlerMessage(getString(R.string.toast_tag_unconnected), FAILURE_RESPONSE);
								return;
							}else if(responseData[0] == Constants.ERROR_RESPONSE){//Error occured when send command-0xF1
								sendHandlerMessage(getString(R.string.toast_tag_send_failure), FAILURE_RESPONSE);
								return;
							}
						}else{//normal,HttpRequest
							String content = NFCUtilies.convertByteArrayToHexString(responseData).substring(0, 16);

							String urlPath = "http://www.baidu.com";
//							urlPath = "http://219.134.89.66:9191/InvengoAuthenticationServer/test/query";

							String path = mRequestManager.getURL(URL_PATH, tagUid, mRandomHexString, content);
//							String path = mRequestManager.getURL(urlPath, tagUid, mRandomHexString, content);

							mRequestManager.doStringRequest(AUTHENTICATE_QUREY_TAG, path, stringListener, stringErrorListener);
//							String response = HttpRequest.processGetHttpRequest(urlPath);
//							Log.i(LOG_TAG, response);
						}

					}else if(responseData[0] == Constants.ERROR_RESPONSE){//Error occured when send command-0xF0
						sendHandlerMessage(getString(R.string.toast_tag_send_failure), FAILURE_RESPONSE);
						return;
					}else if(responseData[0] == Constants.UN_CONNECTED){//Disconnected when send command-0xF0
						sendHandlerMessage(getString(R.string.toast_tag_unconnected), FAILURE_RESPONSE);
						return;
					}
				}catch(UnConnectedException e){//Error occured when connecting tag
					sendHandlerMessage(getString(R.string.toast_tag_unconnected), FAILURE_RESPONSE);
					return;
				}finally{
					IS_RUNNING = false;
					mTagProcessor.close();
				}
			}
		}
	};

	private void sendHandlerMessage(String message, int what){
		Message msg = new Message();
		msg.what = what;
		msg.obj = message;
		mHandler.sendMessage(msg);
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FAILURE_RESPONSE:
				String failureInfo = (String) msg.obj + "-" + uid;
				startResultActivity(failureInfo, AuthenticateFailureActivity.class);
				break;
			}
		};
	};

	private void startResultActivity(String result, Class<?> cls) {
		Intent intent = new Intent(InvengoNFCMainActivity.this, cls);
		intent.putExtra(Constants.RESULT, result);
		startActivity(intent);
	};

	private Response.Listener<String> stringListener = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			Log.i(LOG_TAG, response);
			AuthenticationInfoEntity entity = mParser.parserJsonToEntity(response, AuthenticationInfoEntity.class);
			if(null != entity){
				//验证通过后根据验证结果进行后续处理。1.成功-查询相关信息；2.失败-进入失败界面

				int resultData = entity.getResult();
				if(Constants.SUCCESS == resultData){//成功
					startResultActivity(response, AuthenticateSuccessActivity.class);
					return;
				}else if(Constants.FAILURE== resultData){//失败,后台认证未通过
					startResultActivity(getString(R.string.toast_tag_authenticate_failure) + "-" + uid, AuthenticateFailureActivity.class);
					return;
				}
			}else {
				startResultActivity(getString(R.string.toast_tag_authenticate_failure) + "-" + uid, AuthenticateFailureActivity.class);
				return;
			}
		}
	};

	private Response.ErrorListener stringErrorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			String errorInfo = mErrorManager.getErrorMessage(error);
			startResultActivity(errorInfo + "-" + uid, AuthenticateFailureActivity.class);
		}

	};

//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	};
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case R.id.menu_about:
//			Log.i(LOG_TAG, "About Menu clicked");
//			//版本信息界面
//			Intent versionIntent = new Intent(this, VersionShowActivity.class);
//			startActivity(versionIntent);
//			break;
//
//		case R.id.menu_common:
//			Log.i(LOG_TAG, "Common Menu clicked");
//			//非加密标签演示界面
//			Intent commonIntent = new Intent(this, CommonNFCActivity.class);
//			startActivity(commonIntent);
//			break;
//		}
//
//		return true;
//	}

}
