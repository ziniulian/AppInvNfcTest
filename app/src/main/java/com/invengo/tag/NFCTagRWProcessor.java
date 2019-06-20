package com.invengo.tag;

import java.util.HashMap;
import java.util.Map;

import com.invengo.activity.Constants;
import com.invengo.activity.UnConnectedException;

public class NFCTagRWProcessor {

	private static NFCTagRWProcessor instance = null;

	private static final String IO_TAG = "TECHTagReaderAndWriter";
	private static final int DEFAULT_PAGE = 0;
	private ITagHolder mTagHolder;

	private NFCTagRWProcessor(){
		//
	}

	public static synchronized NFCTagRWProcessor getInstance(){
		if(instance == null){
			instance = new NFCTagRWProcessor();
		}
		return instance;
	}

	public byte[] readTag(){
		return readTag(DEFAULT_PAGE);
	}

	/*
	 * 读取page位置的数据--XC-IHT1401
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public byte[] readTag(int page) {
		byte[] responseData = null;

		Map mode = new HashMap();
		mode.put(ITagHolder.MODE_KEY, ITagHolder.READ_MODE_VALUE);
		mode.put(ITagHolder.PAGE_KEY, Integer.valueOf(page));

		try {
			mTagHolder.connect();
		}catch(UnConnectedException exception){
			return new byte[]{Constants.UN_CONNECTED};
		}
		if(mTagHolder.isConnected()){
			responseData = mTagHolder.transceive(mode);
		}
		mTagHolder.close();
		return responseData;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public byte[] writeTag(int page, byte[] data){
		byte[] responseData = null;

		Map mode = new HashMap();
		mode.put(ITagHolder.MODE_KEY, ITagHolder.WRITE_MODE_VALUE);
		mode.put(ITagHolder.PAGE_KEY, Integer.valueOf(page));
		mode.put(ITagHolder.DATA_KEY, data);

		try {
			mTagHolder.connect();
		}catch(UnConnectedException exception){
			return new byte[]{Constants.UN_CONNECTED};
		}
		if(mTagHolder.isConnected()){
			responseData = mTagHolder.transceive(mode);
		}
		mTagHolder.close();
		return responseData;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public byte[] transceive(byte[] data){
		byte[] responseData = null;

		Map mode = new HashMap();
		mode.put(ITagHolder.MODE_KEY, ITagHolder.READ_WRITE_MODE_VALUE);
		mode.put(ITagHolder.DATA_KEY, data);

		if(!mTagHolder.isConnected()){
			try {
				mTagHolder.connect();
			}catch(UnConnectedException exception){
				return new byte[]{Constants.UN_CONNECTED};
			}
		}
		if(mTagHolder.isConnected()){
			responseData = mTagHolder.transceive(mode);
		}
		mTagHolder.close();
		return responseData;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public byte[] authenticate(byte[] cmd_data){
		byte[] responseData = null;

		Map mode = new HashMap();
		mode.put(ITagHolder.MODE_KEY, ITagHolder.READ_WRITE_MODE_VALUE);
		mode.put(ITagHolder.DATA_KEY, cmd_data);

		if(mTagHolder.isConnected()){
			responseData = mTagHolder.transceive(mode);
		}else {
			return new byte[]{Constants.UN_CONNECTED};
		}
		return responseData;
	}

	public void close(){
		mTagHolder.close();
	}

	public void connect() throws UnConnectedException{
		mTagHolder.connect();
	}
	
	public void setTimeout(int timeout){
		mTagHolder.setTimeout(timeout);
	}

	public void setTagHolder(ITagHolder tagHolder) {
		mTagHolder = tagHolder;
	}
}
