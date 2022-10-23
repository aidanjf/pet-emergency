package com.example.petemergency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonDetParser {
    private HashMap<String, String> parseJsonObject(JSONObject o) {
        HashMap<String, String> dataList = new HashMap<>();
        try {
            String phone = o.getString("formatted_phone_number");
            dataList.put("phone", phone);
            System.out.println("phone: " + phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    private List<HashMap<String, String>> parseJsonArray(JSONArray jsonArray) {
        List<HashMap<String, String>> dataList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                HashMap<String, String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    public HashMap<String, String> parseResult(JSONObject object) {
        JSONObject jsonObject = null;
        try {
            jsonObject = object.getJSONObject("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJsonObject(jsonObject);
    }
}
