package com.example.speechtotext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText mainAccountEdit;
    private EditText mainPasswordEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
    }

    private void findViews(){
        mainAccountEdit = findViewById(R.id.mainAccountEdit);
        mainPasswordEdit = findViewById(R.id. mainPasswordEdit);
    }

    public void mainClearButtonClick(View v){
        mainAccountEdit.setText("");
        mainPasswordEdit.setText("");
    }

    public void mainLoginButtonClick(View v){

        String phone = mainAccountEdit.getText().toString();
        String password = mainPasswordEdit.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){
                        Intent intent = new Intent(MainActivity.this, MethodActivity.class);
                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("登入失敗")
                                .setNegativeButton("重新嘗試",null)
                                .create()
                                .show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,R.string.msg_ServerError,Toast.LENGTH_LONG).show();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString()+error.getNetworkTimeMs(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        };

        LoginRequest loginRequest = new LoginRequest(phone, password, responseListener,errorListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(loginRequest);
    }

    public void mainRegisterTextClink(View view){
        Intent intent =new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

}