package com.example.speechtotext;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertRequest extends StringRequest {
    private static final String INSERT_REQUEST_URL = "";

    private Map<String,String> params;

    public InsertRequest(String product_id, String timestamp, String task, String location, String tool, String tool_type, String input_method, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, INSERT_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("product_id", product_id);
        params.put("timestamp", timestamp);
        params.put("task", task);
        params.put("location", location);
        params.put("tool", tool);
        params.put("tool_type", tool_type);
        params.put("input_method",input_method);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}