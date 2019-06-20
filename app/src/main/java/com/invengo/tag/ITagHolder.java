package com.invengo.tag;

import java.util.Map;

public interface ITagHolder {

	public static final String PAGE_KEY = "READ";
	public static final String DATA_KEY = "DATA";
	public static final String MODE_KEY = "MODE";
	
	public static final int READ_MODE_VALUE = 0;
	public static final int WRITE_MODE_VALUE = 1;
	public static final int READ_WRITE_MODE_VALUE = 2;
	
	public void connect();
	public void close();
	public byte[] transceive(Map mode);
	public boolean isConnected();
	public void setTimeout(int timeout);
	
}
