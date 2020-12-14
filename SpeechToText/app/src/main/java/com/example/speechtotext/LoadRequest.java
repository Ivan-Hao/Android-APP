package com.example.speechtotext;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class LoadRequest extends StringRequest {
    private static final String LOADING_REQUEST_URL = "";

    public LoadRequest(Response.Listener<String> listener, Response.ErrorListener errorListener){
        super( LOADING_REQUEST_URL, listener, errorListener);

    }

}
