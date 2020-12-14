package com.example.speechtotext;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "";
    private Map<String,String> params;

    public LoginRequest(String phone, String password, Response.Listener<String> listener,Response.ErrorListener errorListener) {
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", MD5Util.crypt(password));
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
