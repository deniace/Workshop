package com.deni.workshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deni.workshop.model.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Deni Supriyatna on 20 - Nov - 2019.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etNPM, etPassword;
    private Button btn_login;
    private SharedPref sharedPref;
    private TextView textErorLogin, textRegister;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        VolleySingleton.initialize(this);
        sharedPref = new SharedPref(this);
        etNPM = findViewById(R.id.et_login_user);
        etPassword = findViewById(R.id.et_login_password);
        textErorLogin = findViewById(R.id.text_error_login);
        textRegister = findViewById(R.id.text_register);
        textRegister.setOnClickListener(textRegisterListener);
        btn_login = findViewById(R.id.button_login);
        btn_login.setOnClickListener(btnLoginListener);
        progressBar = findViewById(R.id.login_progbar);
    }

    View.OnClickListener btnLoginListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etPassword.onEditorAction(EditorInfo.IME_ACTION_DONE);
            etNPM.onEditorAction(EditorInfo.IME_ACTION_DONE);
            progressBar.setVisibility(View.VISIBLE);
            Login();
        }
    };

    View.OnClickListener textRegisterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    };

    private boolean isDataValid() {
        String error = "";
        boolean r = false;
        if (etNPM.getText().toString() == null || etNPM.getText().toString() == ""
                || etNPM.getText().toString().equals("")) {
            error = "Username / NPM harus di isi";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            textErorLogin.setText(error);
            r = false;
        } else if (etPassword.getText().toString() == null || etPassword.getText().toString() == ""
                || etPassword.getText().toString().equals("")) {
            error = "Password Tidak boleh kosong";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            textErorLogin.setText(error);
            progressBar.setVisibility(View.GONE);
            r = false;
        } else {
            r = true;
        }
        return r;
    }

    private void setPrev(String token) {
        sharedPref.setToken(token);
    }

    private String getEncode(String npm, String password) {
        String value = npm + ":" + password;
        byte[] encodedValue = Base64.encode(value.getBytes(), Base64.DEFAULT);
        return new String(encodedValue);
    }

    private String getNPM() {
        return etNPM.getText().toString();
    }

    private String getPassword() {
        return etPassword.getText().toString();
    }

    public void Login() {
        if (isDataValid()) {
            String url = App.API_URL + "auth/login";
            final String userlogin = getEncode(getNPM(), getPassword());
            JSONObject data = new JSONObject();
            try {
                data.put("npm", getNPM());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject meta = response.optJSONObject("meta");
                            if (meta.optBoolean("success")) {
                                progressBar.setVisibility(View.GONE);

                                Toast.makeText(LoginActivity.this, "Login Success",
                                        Toast.LENGTH_SHORT).show();
                                JSONObject data = response.optJSONObject("data");

                                String token = data.optString("token");

                                setPrev(token);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);

                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                JSONObject data = response.optJSONObject("data");
                                String metaResponse = meta.optString("message");
                                String dataResponse = data.optString("Error");
                                Toast.makeText(LoginActivity.this, metaResponse + " "
                                        + dataResponse, Toast.LENGTH_SHORT).show();
                                textErorLogin.setText(dataResponse);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login Failed",
                                    Toast.LENGTH_SHORT).show();
                            textErorLogin.setText("Login Failed");
                            error.printStackTrace();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Basic " + userlogin);
                    return headers;
                }
            };
            VolleySingleton.getVolley().getRequestQueue().add(request);
        }
    }
}
