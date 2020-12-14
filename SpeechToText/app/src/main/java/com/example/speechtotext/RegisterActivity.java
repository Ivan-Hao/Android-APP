package com.example.speechtotext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerUserNameEdit, registerUserPhoneEdit, registerUserEmailEdit, registerUserPasswordEdit, registerUserPasswordCheckEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();
    }

    private void findViews(){
        registerUserNameEdit = findViewById(R.id.registerUserNameEdit);
        registerUserPhoneEdit = findViewById(R.id.registerUserPhoneEdit);
        registerUserEmailEdit = findViewById(R.id.registerUserEmailEdit);
        registerUserPasswordEdit = findViewById(R.id.registerUserPasswordEdit);
        registerUserPasswordCheckEdit = findViewById(R.id.registerUserPasswordCheckEdit);
    }

    private boolean passwordCheck(){
        if(registerUserPasswordEdit.getText().toString().equals(registerUserPasswordCheckEdit.getText().toString())){
            return true;
        }else{
            return false;
        }
    }
    public void registerSubmitButtonClick(View view){
        if(!passwordCheck()){
            Toast.makeText(RegisterActivity.this,R.string.msg_PASSWORD,Toast.LENGTH_SHORT).show();
        }else{
            String name = registerUserNameEdit.getText().toString();
            String phone = registerUserPhoneEdit.getText().toString();
            String email = registerUserEmailEdit.getText().toString();
            String password = registerUserPasswordEdit.getText().toString();
            String phone_pattern = "[0-9]{10}";
            String email_pattern ="^[_a-z0-9-]+([.][_a-z0-9-]+)*@[a-z0-9-]+([.][a-z0-9-]+)*$";
            String password_pattern = "[a-zA-Z0-9]{6,10}";

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if(success){
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            RegisterActivity.this.startActivity(intent);
                            RegisterActivity.this.finish();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("註冊失敗")
                                    .setNegativeButton("重新註冊",null)
                                    .create()
                                    .show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this,R.string.msg_ServerError,Toast.LENGTH_LONG).show();

                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(RegisterActivity.this, "註冊錯誤", Toast.LENGTH_LONG).show();
                }
            };


            if(check(phone_pattern,email_pattern,password_pattern,phone,email,password)) {
                RegisterRequest registerRequest = new RegisterRequest(name, phone, email, MD5Util.crypt(registerUserPasswordEdit.getText().toString()), responseListener,errorListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        }
    }
    public void registerBackClick(View v){
        RegisterActivity.this.finish();
    }

    private boolean check(String phone_pattern, String email_pattern, String password_pattern, String phone, String email, String password){
        int check = 0;
        if(!phone.matches(phone_pattern)){
            registerUserPhoneEdit.setError("請輸入正確的手機號碼");
            check++;
        }
        if(!email.matches(email_pattern)){
            registerUserEmailEdit.setError("請輸入正確的email");
            check++;
        }
        if(!password.matches(password_pattern)){
            registerUserPasswordEdit.setError("請輸入6~10位英文數字");
            check++;
        }
        if(check == 0){
            return true;
        }else{
            return false;
        }
    }
}
