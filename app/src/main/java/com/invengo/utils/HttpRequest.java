package com.invengo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.invengo.activity.Constants;

public class HttpRequest {

	/*
	 * 该方法必须运行于子Thread中，不能运行于主线程中
	 * 此处不做线程处理
	 */
	public static String processGetHttpRequest(String path){
		StringBuffer mBuffer = new StringBuffer();

		URL url = null;
		HttpURLConnection urlConnection = null;

//		StringBuffer path = new StringBuffer();
//		path.append(URL_PATH).append(uid).append("/").append(randomOne).append("/").append(content);

		try {
			url = new URL(path.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(2000);
			urlConnection.setReadTimeout(3 * 1000);
			urlConnection.setDoInput(true);
//			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Charset", "UTF-8");

			urlConnection.connect();

			int responseCode = urlConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				InputStream in = urlConnection.getInputStream();
				BufferedReader reader =  new BufferedReader(new InputStreamReader(in, "UTF-8"));
//				BufferedReader reader =  new BufferedReader(new InputStreamReader(in));

				String readLine = null;
				while((readLine = reader.readLine()) != null){
					String responseByte = readLine;
					mBuffer.append(responseByte);
				}
			}

		}catch (SocketException e) {
			e.printStackTrace();
			//连接失败，属于网络问题
			return Constants.CONNECT_SERVER_ERROR;
		}catch (SocketTimeoutException e) {
			e.printStackTrace();
			//超时，属于网络问题
			return Constants.CONNECT_TIMEOUT_ERROR;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			//解析地址错误，属于网络问题
			return Constants.CONNECT_URL_ERROR;
		} catch (IOException e) {
			e.printStackTrace();
			//网络IO操作错误，属于网络问题
			return Constants.CONNECT_IO_ERROR;
		}finally{
			if(null != urlConnection){
				urlConnection.disconnect();
			}
		}
		return mBuffer.toString();
	}

}
