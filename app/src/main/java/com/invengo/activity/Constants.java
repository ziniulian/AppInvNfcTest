package com.invengo.activity;

public class Constants {

	public static final byte READ_CMD = 0x30;
	public static final byte WRITE_CMD = (byte) 0xA2;
	public static final byte AUTHEN_READ_CMD = (byte) 0xF1;
	public static final byte AUTHEN_WRITE_CMD_FIRST = (byte) 0xF0;
	public static final byte AUTHEN_WRITE_CMD_SECOND = (byte) 0xF2;

	public static final byte UN_CONNECTED = 0x0C;
	public static final byte NAK = 0x1C;
	public static final byte ACK = 0x0A;
	public static final byte INDEX_OUT_OF_BOUNDS = 0x1A;
	public static final byte ERROR_RESPONSE = 0x0E;

	public static final int SUCCESS = 1;
	public static final int FAILURE = 0;
	public static final String CONNECT_SERVER_ERROR = "E";//连接服务器失败
	public static final String CONNECT_TIMEOUT_ERROR = "T";//连接服务器超时
	public static final String CONNECT_IO_ERROR = "I";//读取数据失败
	public static final String CONNECT_URL_ERROR = "U";//读取数据失败


	public static final String RESULT = "RESULT";

}
