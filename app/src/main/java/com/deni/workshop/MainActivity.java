package com.deni.workshop;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deni.workshop.adapter.MahasiswaAdapter;
import com.deni.workshop.model.Mahasiswa;
import com.deni.workshop.model.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Deni Supriyatna on 09 - Nov - 2019.
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private List<Mahasiswa> list = new ArrayList<>();
    private FloatingActionButton floatButton;
    private int position = -1;

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = new SharedPref(this);

        VolleySingleton.initialize(MainActivity.this);
        recyclerView = findViewById(R.id.recycler_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        floatButton = findViewById(R.id.buttonAdd);
        floatButton.setOnClickListener(addListener);
        populateData();
    }

    private void populateData() {
        String url = App.API_URL + "mahasiswa/";
        adapter = new MahasiswaAdapter();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        proccessResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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

    private void proccessResponse(JSONObject response){
        try {
            Mahasiswa mahasiswa = null;
            JSONArray jsonArray = response.getJSONArray("data");
            JSONObject jsObj = null;
            list = new ArrayList<>();
            for(int i = 0; i<jsonArray.length(); i++){
                jsObj = jsonArray.getJSONObject(i);
                mahasiswa = new Mahasiswa(jsObj);
                list.add(mahasiswa);
            }
            adapter = new MahasiswaAdapter(list, listener);
            recyclerView.setAdapter(adapter);
            adapter.notifyItemRangeInserted(adapter.getItemCount(), list.size());
        }catch (Exception ex){
            Log.d("Log Exception",ex.getMessage());
        }
    }

    private void proccessErrorResponse(VolleyError error){
        if((error.networkResponse != null) && (error.networkResponse.data != null)){
            try {
                String errorMessage = new String(error.networkResponse.data);
                Log.d("Register error response",errorMessage);
                JSONObject jsonError = new JSONObject(errorMessage);
                JSONObject metadata = jsonError.getJSONObject("metadata");
                String message = metadata.optString("message");
                int status = metadata.optInt("status");

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("error")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }catch (JSONException e){
                Log.d("error exception", e.getMessage());
            }
        }else {
            Log.d("error 501", "not know error");
        }
    }

    private MahasiswaAdapter.OnItemClickListener listener = new MahasiswaAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Mahasiswa item) {
            Intent intent = new Intent(MainActivity.this, DetailMahasiswaActivity.class);
            intent.putExtra("npm", item.getNpm());
            intent.putExtra("STATE", 1);
            startActivityForResult(intent, 1);
        }
    };

    private FloatingActionButton.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = -1;
            Intent intent = new Intent(MainActivity.this, DetailMahasiswaActivity.class);
            intent.putExtra("STATE", 0);
            startActivityForResult(intent, 1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("Looooooog","Activity result invoked!");
        if(resultCode == Activity.RESULT_OK){
            populateData();
        }
    }
}
