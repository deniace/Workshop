package com.deni.workshop;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deni.workshop.model.Mahasiswa;
import com.deni.workshop.model.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by Deni Supriyatna on 08 - Nov - 2019.
 */
public class DetailMahasiswaActivity extends AppCompatActivity {

    private EditText textNpmDetail, textNamaDetail, textJkDetail,
            textJurusanDetail, textNoHpDetail, textEmailDetail, textAgamaDetail;
    Button btnDelete, btnSave;
    private Mahasiswa mahasiswa;
    private int state = 0;

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_mahasiswa);

        sharedPref = new SharedPref(this);

        btnSave = findViewById(R.id.btn_save_detail);
        btnDelete = findViewById(R.id.btn_delete_detail);
        state = getIntent().getIntExtra("STATE", 0);
        Log.d("staeeeee", "state = " + state);

        findId();

        if (state > 0) {
            String npm = getIntent().getStringExtra("npm");
            populateDate(npm);
        } else {
            btnDelete.setVisibility(View.GONE);
            mahasiswa = new Mahasiswa();
        }
    }

    private void findId() {
        textNpmDetail = findViewById(R.id.text_npm_detail);
        textNamaDetail = findViewById(R.id.text_nama_detail);
        textJkDetail = findViewById(R.id.text_jk_detail);
        textJurusanDetail = findViewById(R.id.text_jurusan_detail);
        textNoHpDetail = findViewById(R.id.text_nohp_detail);
        textEmailDetail = findViewById(R.id.text_email_detail);
        textAgamaDetail = findViewById(R.id.text_agama_detail);
    }

    private void populateDate(final String npm) {
        String url = App.API_URL + "mahasiswa/" + npm;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject meta = response.optJSONObject("meta");
                        if (meta.optBoolean("success")) {
                            JSONObject data = response.optJSONObject("data");
                            mahasiswa = new Mahasiswa(data);
                            textNpmDetail.setText(mahasiswa.getNpm());
                            textNamaDetail.setText(mahasiswa.getNama_mahasiswa());
                            textJkDetail.setText(mahasiswa.getJenis_kelamin());
                            textJurusanDetail.setText(mahasiswa.getJurusan());
                            textNoHpDetail.setText(mahasiswa.getNo_hp());
                            textEmailDetail.setText(mahasiswa.getEmail());
                            textAgamaDetail.setText(mahasiswa.getAgama());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        proccessErrorResponse(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + sharedPref.getToken());
                return headers;
            }
        };
        VolleySingleton.getVolley().getRequestQueue().add(request);
    }

    private void proccessErrorResponse(VolleyError error) {
        if ((error.networkResponse != null) && (error.networkResponse.data != null)) {
            try {
                String errorMessage = new String(error.networkResponse.data);
                Log.d("Register error response", errorMessage);
                JSONObject jsonError = new JSONObject(errorMessage);
                JSONObject metadata = jsonError.getJSONObject("metadata");
                String message = metadata.optString("message");
                int status = metadata.optInt("status");

                new AlertDialog.Builder(DetailMahasiswaActivity.this)
                        .setTitle("error")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            } catch (JSONException e) {
                Log.d("error exception", e.getMessage());
            }
        } else {
            Log.d("error 501", "not know error");
        }
    }

    private boolean isDataValid() {
        String error = "";
        if (textNpmDetail.getText().toString().equals("")) {
            error = "NPM wajib di isi";
        } else if (textNamaDetail.getText().toString().equals("")) {
            error = "Nama mahasiswa wajib di isi";
        } else if (textJkDetail.getText().toString().equals("")) {
            error = "Jenis Kelamin wajib di isi";
        } else if (textJurusanDetail.getText().toString().equals("")) {
            error = "Jurusan wajib di isi";
        } else if (textNoHpDetail.getText().toString().equals("")) {
            error = "No HP wajib di isi";
        } else if (textEmailDetail.getText().toString().equals("")) {
            error = "Email wajib di isi";
        } else if (textAgamaDetail.getText().toString().equals("")) {
            error = "Email wajib di isi";
        } else {
            return true;
        }
        if (!error.equals("")) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void save(View view) {
        if (isDataValid()) {
            String url = App.API_URL + "mahasiswa/";
            JSONObject data = getJsonObject();

            JsonObjectRequest request = new JsonObjectRequest(
                    state == 0 ? Request.Method.POST : Request.Method.PUT, url, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONObject meta = response.optJSONObject("meta");
                            if (meta.optBoolean("success")) {
                                Toast.makeText(DetailMahasiswaActivity.this, "Save Data Success",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("npm", mahasiswa.getNpm());
                                intent.putExtra("ACTION", state == 0 ? "NEW" : "UPDATE");
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                String message = meta.optString("message");
                                Toast.makeText(DetailMahasiswaActivity.this, message,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            proccessErrorResponse(error);
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + sharedPref.getToken());
                    return headers;
                }
            };
            VolleySingleton.getVolley().getRequestQueue().add(request);
        }
    }

    private JSONObject getJsonObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("npm", textNoHpDetail.getText().toString());
            obj.put("nama_mahasiswa", textNamaDetail.getText().toString());
            obj.put("jenis_kelamin", textJkDetail.getText().toString());
            obj.put("jurusan", textJurusanDetail.getText().toString());
            obj.put("no_hp", textNoHpDetail.getText().toString());
            obj.put("email", textEmailDetail.getText().toString());
            obj.put("agama", textAgamaDetail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void delete(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        processDelete();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void processDelete() {
        String url = App.API_URL + "mahasiswa/" + mahasiswa.getNpm();
        VolleySingleton.getVolley().getRequestQueue().add(
                new JsonObjectRequest(Request.Method.DELETE, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject res) {
                                JSONObject meta = res.optJSONObject("meta");
                                if (meta.optBoolean("success")) {
                                    Toast.makeText(DetailMahasiswaActivity.this,
                                            "Delete data Success!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("npm", mahasiswa.getNpm());
                                    intent.putExtra("ACTION", "DELETE");
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                } else {
                                    Toast.makeText(DetailMahasiswaActivity.this,
                                            meta.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                proccessErrorResponse(error);
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("Authorization", "Bearer " + sharedPref.getToken());
                        return headers;
                    }
                }
        );
    }

}
