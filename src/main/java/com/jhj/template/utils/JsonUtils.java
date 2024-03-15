package com.jhj.template.utils;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

	public static String getJsonFromObject(Object obj) {
		// JsonIgnoreProperties / JsonIgnore
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = null;
		try {
			// null 미포함
			// objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
			// null 포함
			objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.ALWAYS);

			jsonString = objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return jsonString;
	}

	/*
	 * null 미포함
	 */
	public static String getJsonFromObject2(Object obj) {
		// null 포함하도록 수정
		return getJsonFromObject(obj);
		/*
		 * Gson에서는 JsonIgnoreProperties / JsonIgnore 안됨
		 * transient는 객체를 직렬화(Serialize) 할 때, 특정 필드를 제외하고 싶을 때 사용합니다.
		 * 
		 * 제외하려고 하는 필드에 transient를 붙이면,
		 * 
		 * 해당 필드를 제외하고 Json 문자열이 생성됩니다.
		 * 출처: https://hianna.tistory.com/629 [어제 오늘 내일]
		 */
		/*
		 * Gson gson = new GsonBuilder().create();
		 * String jsonString = null;
		 * 
		 * try {
		 * jsonString = gson.toJson(obj);
		 * } catch (Exception e) {
		 * log.error(e.getMessage(), e);
		 * }
		 * 
		 * return jsonString;
		 */
	}

	public static <T> T getObjectToJsonString(String jsonString, Class<T> type) {
		ObjectMapper objectMapper = new ObjectMapper();
		T obj = null;
		try {
			obj = objectMapper.readValue(jsonString, type);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}

		return obj;
	}

	public static <T> T getObjectToJsonString2(String jsonString, Class<T> type) {
		Gson gson = new GsonBuilder().create();
		T obj = null;
		try {
			obj = gson.fromJson(jsonString, type);
		} catch (JsonSyntaxException e) {
			log.error(e.getMessage(), e);
		}

		return obj;
	}

	/**
	 * Object -> HashMap<String, Object> 변환
	 * 
	 * @param obj => Java Object
	 * @return HashMap<String, Object>
	 */
	public static HashMap<String, Object> getHashMap(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		HashMap<String, Object> hashMap = objectMapper.convertValue(obj, new TypeReference<HashMap<String, Object>>() {
		});

		return hashMap;
	}

	/**
	 * Object -> JSONObject 변환
	 * 
	 * @param obj => Java Object
	 * @return JSONObject
	 */
	public static JSONObject getJSONObject(Object obj) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = (JSONObject) JSONValue.parse(new ObjectMapper().writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		return jsonObject;
	}

	/**
	 * Workorder.class
	 * 
	 * @param <T>
	 * @param obj
	 * @param type
	 * @return
	 */
	public static <T> T getObjectToClass(Object obj, Class<T> type) {
		T classConvert = null;
		try {
			String jsonString = getJsonFromObject(obj);
			classConvert = JsonUtils.getObjectToJsonString2(jsonString, type);
		} catch (JsonSyntaxException e) {
			log.error(e.getMessage(), e);
		}

		return classConvert;
	}
}
