package com.invengo.utils;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.invengo.activity.R;

public class VolleyErrorManager {

	private Context mContext = null;
	private static VolleyErrorManager mManager = null;
	private VolleyErrorManager(Context context){
		this.mContext = context;
	}
	
	public synchronized static VolleyErrorManager getInstance(Context context){
		if(null == mManager){
			mManager = new VolleyErrorManager(context);
		}
		return mManager;
	}
	
	public String getErrorMessage(VolleyError error){
		String errorInfo = "";
		
		if(error instanceof NetworkError){
			errorInfo = mContext.getString(R.string.toast_tag_server_connect_url_failure);
		}else if(error instanceof NoConnectionError){
			errorInfo = mContext.getString(R.string.toast_tag_server_connect_failure);
		}else if(error instanceof ParseError){
			errorInfo = mContext.getString(R.string.toast_tag_server_connect_io_failure);
		}else if(error instanceof ServerError){
			errorInfo = mContext.getString(R.string.toast_tag_server_failure);
		}else if(error instanceof TimeoutError){
			errorInfo = mContext.getString(R.string.toast_tag_server_connect_timeout_failure);
		}
		return errorInfo;
	}
}
