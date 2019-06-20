package com.invengo.utils;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

public class GsonDataParser {

	private static Gson mGson;
	private static GsonDataParser mGsonDataParser;
	private GsonDataParser(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Date.class, new JsonDateSerializer()).setDateFormat(DateFormat.LONG);
		builder.registerTypeAdapter(Date.class, new JsonDateDeserializer()).setDateFormat(DateFormat.LONG);

		mGson = builder.create();
	}

	public synchronized static GsonDataParser getInstance(){
		if(null == mGsonDataParser){
			mGsonDataParser = new GsonDataParser();
		}
		return mGsonDataParser;
	}

	/*
	 * gsonString:json格式数据字符串
	 * entity:需要将json格式数据解析成的实体
	 */
	public <T> T parserJsonToEntity(String gsonString, Class<T> cls){

		if("".equals(gsonString) || null == gsonString || null == cls){
			return null;
		}
		T entity = null;
		try{
			entity = mGson.fromJson(gsonString, cls);
		}catch (JsonSyntaxException e) {
			return null;
		}
		return entity;

	}

	public String parserEntityToJson(Object entity){

		if(null == entity){
			return null;
		}

		String jsonString = mGson.toJson(entity);
		return jsonString;

	}

	private class JsonDateSerializer implements JsonSerializer<Date> {

		@Override
		public JsonElement serialize(Date date, Type type,
				JsonSerializationContext context) {
			return new JsonPrimitive(date.getTime());
		}

	}

	private class JsonDateDeserializer implements JsonDeserializer<Date>{

		@Override
		public Date deserialize(JsonElement element, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			return new Date(element.getAsJsonPrimitive().getAsLong());
		}

	}
}
