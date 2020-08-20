package com.ntnt.helpers;

import com.ntnt.annotations.JsonProperty;
import com.ntnt.annotations.JsonSerializable;
import com.ntnt.exceptions.MissingAnnotationException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONHelper {
    public static String stringify(Object obj) {
        try {
            JsonSerializable jsonSerializable = obj.getClass().getAnnotation(JsonSerializable.class);
            if (jsonSerializable == null)
                throw new MissingAnnotationException();

            StringJoiner joiner = new StringJoiner(",", "{", "}");
            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field f : fields) {
                StringBuilder keyValue = new StringBuilder();

                String methodName = f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                Method getMethod = (f.getType() == Boolean.class || f.getType().getName().equals("boolean"))
                        ? obj.getClass().getMethod("is" + methodName)
                        : obj.getClass().getMethod("get" + methodName);

                JsonProperty property = f.getAnnotation(JsonProperty.class);
                keyValue.append("\"")
                        .append((property != null) ? property.name() : f.getName())
                        .append("\"")
                        .append(":");

                Object value = getMethod.invoke(obj);

                if (value instanceof String) {
                    keyValue.append("\"")
                            .append(value)
                            .append("\"");
                } else if (
                        value instanceof Short
                                || value instanceof Integer
                                || value instanceof Long
                                || value instanceof Double
                                || value instanceof Float
                                || value instanceof Byte
                ) {
                    keyValue.append(value);
                } else if (value instanceof Boolean) {
                    if (((Boolean) value == true))
                        keyValue.append("true");
                    else
                        keyValue.append("false");
                }
                joiner.add(keyValue);
            }
            return joiner.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Still developing
    public static Object parse(String json, Class<?> sourceClass) {

        try {
            Object obj = sourceClass.newInstance();

            Map<String, String> keyValues = getKeyValueArrayFromString(json);
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                JsonProperty property = f.getAnnotation(JsonProperty.class);
                String value = keyValues.get((property == null) ? f.getName() : property.name());
                String methodName = "set" + f.getName().substring(0, 1).toUpperCase()
                        + f.getName().substring(1, f.getName().length());
                Method setMethod = sourceClass.getMethod(methodName, f.getType());

                if(f.getType() == String.class){
                    setMethod.invoke(obj, value);
                } else if(f.getType() == Boolean.class || f.getType().getName().equals("boolean")){
                    setMethod.invoke(obj, Boolean.valueOf(value));
                }else if(f.getType() == Short.class || f.getType().getName().equals("short")){
                    setMethod.invoke(obj, Short.valueOf(value));
                }else if(f.getType() == Integer.class || f.getType().getName().equals("int")){
                    setMethod.invoke(obj, Integer.valueOf(value));
                }else if(f.getType() == Long.class || f.getType().getName().equals("long")){
                    setMethod.invoke(obj, Long.valueOf(value));
                }else if(f.getType() == Float.class || f.getType().getName().equals("float")){
                    setMethod.invoke(obj, Float.valueOf(value));
                }else if(f.getType() == Double.class || f.getType().getName().equals("double")){
                    setMethod.invoke(obj, Double.valueOf(value));
                }else if(f.getType() == Byte.class || f.getType().getName().equals("byte")){
                    setMethod.invoke(obj, Byte.valueOf(value));
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Still developing
    private static Map<String, String> getKeyValueArrayFromString(String str) {
        str = str.substring(1, str.length() - 1);

        Pattern pattern = Pattern.compile(" *\"\\w*\" *:");
        Matcher matcher = pattern.matcher(str);
        ArrayList<String> listKeys = new ArrayList<>();
        ArrayList<String> listValues = new ArrayList<>();
        HashMap<String, String> keyValues = new HashMap<>();
        while (matcher.find()) {
            String key = matcher.group(0);
            listKeys.add(key.substring(1, key.lastIndexOf('\"')));
        }


        String[] tempValue = str.split(" *\"\\w*\" *:");
        for (int i = 1; i < tempValue.length; i++) {
            if (tempValue[i].contains("\""))
                tempValue[i] = tempValue[i].substring(1, tempValue[i].length() - 2);
            else
                tempValue[i] = tempValue[i].substring(0, tempValue[i].length() - 1);
        }
        listValues = new ArrayList<>(Arrays.asList(tempValue));
        listValues.remove(0); //First element is redundant

        if (listKeys.size() == listValues.size()) {
            for (int i = 0; i < listKeys.size(); i++) {
                keyValues.put(listKeys.get(i), listValues.get(i));
            }
        }
        return keyValues;
    }
}
