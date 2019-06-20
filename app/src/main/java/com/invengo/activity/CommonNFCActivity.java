package com.invengo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.invengo.utils.NFCUtilies;

public class CommonNFCActivity extends ListActivity {

	private NfcAdapter mNfcAdapter = null;
	private PendingIntent mPendingIntent = null;
	private IntentFilter[] mFilter = null;
	private String[][] mTechList = null;
	private ArrayList<TagEntity> mTagList = new ArrayList<TagEntity>();
	private static final String LOG_TAG = "CommonNFCActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_nfc);

		checkNfcEnabled();

		Intent intent = new Intent(this, getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
		mFilter = new IntentFilter[]{filter};

		mTechList = new String[][]{new String[]{NfcA.class.getName(), NdefFormatable.class.getName()}};

		CommonListAdapter listAdapter = new CommonListAdapter(mTagList);
		this.setListAdapter(listAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(null != mNfcAdapter){
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilter, mTechList);
		}
		((CommonListAdapter)getListAdapter()).notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(null != mNfcAdapter){
			mNfcAdapter.disableForegroundDispatch(this);
		}
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
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())){
			Toast.makeText(this, R.string.toast_tag_detected, Toast.LENGTH_SHORT).show();
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String tagId = NFCUtilies.convertByteArrayToHexString(tag.getId());

			StringBuffer stringBuffer = new StringBuffer();
			for(String type : tag.getTechList()){
				stringBuffer.append("[" + type.substring(type.lastIndexOf(".") + 1, type.length())+ "]");
			}

			TagEntity entity = new TagEntity();
			entity.setTagId(tagId);
			entity.setTagType(stringBuffer.toString());
			if(mTagList.size() == 5){
				mTagList.remove(4);
			}
			mTagList.add(0, entity);
		}

	}

	private class CommonListAdapter extends ArrayAdapter<TagEntity>{

		public CommonListAdapter(List<TagEntity> list){
			super(getApplication(), 0, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = getLayoutInflater().inflate(R.layout.activity_nfc_discover_view, null);
			}

			TagEntity entity = getItem(position);

			TextView tagId = (TextView) convertView.findViewById(R.id.list_tech_tag_id);
			tagId.setText(entity.getTagId());

			TextView tagType = (TextView) convertView.findViewById(R.id.list_tech_tag_type_id);
			tagType.setText(entity.getTagType());

			return convertView;
		}

	}

	private class TagEntity{
		private String mTagId;
		private String mTagType;

		public String getTagId() {
			return mTagId;
		}
		public void setTagId(String tagId) {
			mTagId = tagId;
		}
		public String getTagType() {
			return mTagType;
		}
		public void setTagType(String tagType) {
			mTagType = tagType;
		}

		@Override
		public String toString() {
			return this.mTagId;
		}
	}

}
