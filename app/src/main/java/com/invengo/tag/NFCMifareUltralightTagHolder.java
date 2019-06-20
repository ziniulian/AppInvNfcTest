package com.invengo.tag;

import java.io.IOException;
import java.util.Map;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.util.Log;

import com.invengo.activity.Constants;
import com.invengo.activity.UnConnectedException;

public class NFCMifareUltralightTagHolder implements ITagHolder{

//	private MifareUltralight mNfcA = null;
	private NfcA mNfcA = null;
	private static final String HOLDER_IO_TAG = "NFCMifareUltralightTagHolder";
	private static final int MAX_PAGES = 256;
	private static final int MIN_PAGES = 0;
	private byte[] uid;

	public NFCMifareUltralightTagHolder(Intent intent){
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		uid = tag.getId();
		mNfcA = mNfcA.get(tag);
	}

	@Override
	public void connect() {
		try {
			mNfcA.connect();
		} catch (IOException e) {
			Log.e(HOLDER_IO_TAG, "IOException when NFCMifareUltralightTagHolder.connect().");
			throw new UnConnectedException();
		}
	}

	@Override
	public void close() {
		try {
			mNfcA.close();
		} catch (IOException e) {
			Log.e(HOLDER_IO_TAG, "IOException when NFCMifareUltralightTagHolder.close().");
		}
	}

	@Override
	public void setTimeout(int timeout){
		mNfcA.setTimeout(timeout);
	}

	/*
	 * 根据MifareUltralight类型标签特点，
	 * READ_MODE & WRITE_MODE
	 * mode = TagHolder.READ_MODE --Read mode，即page=mode.get(PAGE)；
	 * mode = TagHolder.WRITE_MODE --Write mode，即page=mode.get(PAGE),data=mode.get(DATA)；
	 * mode = TagHolder.READ_WRITE_MODE --Read_Write mode，即data=mode.get(DATA)；
	 */
	@Override
	public byte[] transceive(Map mode) {
		int modeType = (Integer) mode.get(MODE_KEY);
		byte[] responseData = null;

		try {
//			if(modeType == READ_MODE_VALUE){
//				int page = (Integer) mode.get(PAGE_KEY);
//				if(page < MIN_PAGES || page > MAX_PAGES){
//					return new byte[]{Constants.INDEX_OUT_OF_BOUNDS};
//				}
//				responseData = mNfcA.readPages(page);
//			}else if(modeType == WRITE_MODE_VALUE){//写入数据返回值(responseData)还不确定
//				int page = (Integer) mode.get(PAGE_KEY);
//				byte[] data = (byte[]) mode.get(DATA_KEY);
//				if(page < MIN_PAGES || page > MAX_PAGES){
//					return new byte[]{Constants.INDEX_OUT_OF_BOUNDS};
//				}
//				mNfcA.writePage(page, data);
//				responseData = new byte[]{Constants.ACK};
//			}else
			if(modeType == READ_WRITE_MODE_VALUE){
				byte[] data = (byte[]) mode.get(DATA_KEY);
				responseData = mNfcA.transceive(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Log.e(HOLDER_IO_TAG, "IOException when NFCMifareUltralightTagHolder.transceive().");
			return new byte[]{Constants.ERROR_RESPONSE};
		}

		return responseData;
	}

	@Override
	public boolean isConnected() {
		return mNfcA.isConnected();
	}

	public byte[] getUid(){
		return uid;
	}
}
