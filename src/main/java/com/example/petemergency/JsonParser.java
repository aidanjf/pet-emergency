package com.example.petemergency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {
    private HashMap<String, String> parseJsonObject(JSONObject o) {
        HashMap<String, String> dataList = new HashMap<>();
        try {
            String name = o.getString("name");
            String lat = o.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String lng = o.getJSONObject("geometry").getJSONObject("location").getString("lng");
            String place_id = o.getString("place_id");
            dataList.put("name", name);
            dataList.put("lat", lat);
            dataList.put("lng", lng);
            dataList.put("place_id", place_id);
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

    public List<HashMap<String, String>> parseResult(JSONObject object) {
        JSONArray jsonArray = null;
        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJsonArray(jsonArray);
    }
}
