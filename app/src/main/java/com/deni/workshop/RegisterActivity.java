package com.deni.workshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNpmRegister, etPasswordRegister1, etPasswordRegister2;
    private Button btnRegister;
    private ProgressBar progressBarRegister;
    private TextView textErrorRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        VolleySingleton.initialize(this);
        etNpmRegister = findViewById(R.id.et_register_npm);
        etPasswordRegister1 = findViewById(R.id.et_register_password1);
        etPasswordRegister2 = findViewById(R.id.et_register_password2);
        progressBarRegister = findViewById(R.id.register_progbar);
        btnRegister = findViewById(R.id.btn_register);
        textErrorRegister = findViewById(R.id.text_error_register);
        btnRegister.setOnClickListener(registerListener);
    }

    View.OnClickListener registerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            etNpmRegister.onEditorAction(EditorInfo.IME_ACTION_DONE);
            etPasswordRegister1.onEditorAction(EditorInfo.IME_ACTION_DONE);
            etPasswordRegister2.onEditorAction(EditorInfo.IME_ACTION_DONE);
            progressBarRegister.setVisibility(View.VISIBLE);
            register();
        }
    };

    private String getNpmRegister() {
        return etNpmRegister.getText().toString();
    }

    private String getPasswordRegister1() {
        return etPasswordRegister1.getText().toString();
    }

    private String getPasswordRegister2() {
        return etPasswordRegister2.getText().toString();
    }

    private boolean isDataValid() {
        String error = "";
        boolean r = false;
        if (getNpmRegister() == null || getNpmRegister() == ""
                || getNpmRegister().equals("")) {
            error = "Username / NPM harus di isi";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            textErrorRegister.setText(error);
            progressBarRegister.setVisibility(View.GONE);
            r = false;
        } else if (getPasswordRegister1() == null || getPasswordRegister1() == ""
                || getPasswordRegister1().equals("")) {
            error = "Password Tidak boleh kosong";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            textErrorRegister.setText(error);
            progressBarRegister.setVisibility(View.GONE);
            r = false;
        } else if (!getPasswordRegister2().equals(getPasswordRegister1())) {
            error = "Password dan Repassword harus sama";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            textErrorRegister.setText(error);
            progressBarRegister.setVisibility(View.GONE);
            r = false;
        } else {
            r = true;
        }
        return r;
    }

    private JSONObject getJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("npm", getNpmRegister());
            obj.put("password", getPasswordRegister1());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void register() {
        if (isDataValid()) {
            String url = App.API_URL + "auth/register";
            JSONObject data = getJsonObject();

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject meta = response.optJSONObject("meta");
                            String message = meta.optString("message");
                            if (meta.optBoolean("success")) {
                                progressBarRegister.setVisibility(View.GONE);
                                textErrorRegister.setText(message);
                                Toast.makeText(RegisterActivity.this, message,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                progressBarRegister.setVisibility(View.GONE);
                                textErrorRegister.setText(message);
                                Toast.makeText(RegisterActivity.this, message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressBarRegister.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Register Failed",
                                    Toast.LENGTH_SHORT).show();
                            textErrorRegister.setText("Register Failed");
                            error.printStackTrace();
                        }
                    });
            VolleySingleton.getVolley().getRequestQueue().add(request);
        }
    }
}
