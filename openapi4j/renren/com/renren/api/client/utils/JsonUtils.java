package com.renren.api.client.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author 李勇(yong.li@opi-corp.com) 2011-3-7
 */
@SuppressWarnings("unchecked")
public final class JsonUtils {

    /**
     * 从JSONArray中取出第index个JSONObject.
     * 
     * @param jsonObjects
     * @param index
     * @return 返回JSONObject或null
     */
    public static JSONObject getIndexJSONObject(JSONArray jsonObjects, int index) {
        if (jsonObjects == null || jsonObjects.size() <= index) {
            return null;
        }
        return (JSONObject) jsonObjects.get(index);
    }

    /**
     * 从JSONObject对象中提取key的值。
     * 
     * @param <T>
     * @param jsonObj
     * @param propName
     * @param propType
     * @return 返回T，或null。
     */
    public static <T> T getValue(JSONObject jsonObj, String propName, Class<T> propType) {
        Object obj = jsonObj.get(propName);
        if (obj == null) {
            return null;
        }
        return (T) obj;
    }

    /**
     * 从JSONObject对象中提取key的值。
     * 
     * @param <T>
     * @param jsonObj
     * @param propName
     * @param propType
     * @param defValue
     * @return 返回T，或defValue。
     */
    public static <T> T getValue(JSONObject jsonObj, String propName, Class<T> propType, T defValue) {
        Object obj = jsonObj.get(propName);
        if (obj == null) {
            return defValue;
        }
        return (T) obj;
    }
}
