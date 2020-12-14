package com.example.speechtotext;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "";

    private Map<String,String> params;

    public RegisterRequest(String name, String phone, String email, String password, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
