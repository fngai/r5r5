package com.example.hp.a05112016workshop;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisActivity extends AppCompatActivity {

    private EditText edtDisplayname;
    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtConfPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        edtDisplayname = (EditText)findViewById(R.id.edtDisp);
        edtUsername = (EditText)findViewById(R.id.edtUser2);
        edtPassword = (EditText)findViewById(R.id.edtPass2);
        edtConfPassword = (EditText)findViewById(R.id.edtConfP);

        btnRegister = (Button)findViewById(R.id.btnRegis);
        setListener();

    }

    private void setListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLogin()){
                    new Register(
                            edtUsername.getText().toString(),
                            edtPassword.getText().toString(),
                            edtConfPassword.getText().toString(),
                            edtDisplayname.getText().toString()).execute();
                }else{
                    Toast.makeText(RegisActivity.this, "กรุณากรอกข้อมูลให้ครบถ้วน!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkLogin() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        String confPassword = edtConfPassword.getText().toString();
        String dispName = edtDisplayname.getText().toString();

        if(username.isEmpty()) return false;

        if(password.isEmpty()) return false;

        if(confPassword.isEmpty()) return false;

        if(!password.equals(confPassword)) return false;

        if(dispName.isEmpty()) return false;

        return true;
    }

    private class Register extends AsyncTask<Void, Void, String>{

        private String username;
        private String password;
        private String confPassword;
        private String displayName;

        public Register(String username, String password, String confPassword, String dispName) {
            this.username = username;
            this.password = password;
            this.confPassword = confPassword;
            this.displayName = dispName;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient Client = new OkHttpClient();
            Request req;
            Response resp;

            RequestBody reqBody = new FormBody.Builder()
                    .add("username",username)
                    .add("password",password)
                    .add("password_con",confPassword)
                    .add("display_name",displayName)
                    .build();

            req = new Request.Builder()
                    .url("http://kimhun55.com/pollservices/signup.php")
                    .post(reqBody)
                    .build();

            try
            {
                resp = Client.newCall(req).execute();
                if(resp.isSuccessful()){
                    return resp.body().string();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            /*{
                "result": {
                "result": 1,
                        "result_desc": " success"
            }
            }*/
            try {
                JSONObject rootObj = new JSONObject(s);
                if(rootObj.has("result")){
                    JSONObject resultObj = rootObj.getJSONObject("result");
                    if(resultObj.getInt("result") == 1){
                        Toast.makeText(RegisActivity.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(RegisActivity.this, resultObj.getString("result_desc"), Toast.LENGTH_SHORT).show();
                    }
                }

            }catch (JSONException jx){

            }
        }
    }

}
