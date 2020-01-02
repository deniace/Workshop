package com.deni.workshop.model;

import org.json.JSONObject;

/**
 * Created by Deni Supriyatna on 08 - Nov - 2019.
 */
public class Mahasiswa {
    public Mahasiswa(){}

    private String npm;
    private String nama_mahasiswa;
    private String jenis_kelamin;
    private String jurusan;
    private String no_hp;
    private String email;
    private String agama;

    public Mahasiswa(JSONObject object){
        npm = object.optString("npm");
        nama_mahasiswa = object.optString("nama_mahasiswa");
        jenis_kelamin = object.optString("jenis_kelamin");
        jurusan = object.optString("jurusan");
        no_hp = object.optString("no_hp");
        email = object.optString("email");
        agama = object.optString("agama");
    }

    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama_mahasiswa() {
        return nama_mahasiswa;
    }

    public void setNama_mahasiswa(String nama_mahasiswa) {
        this.nama_mahasiswa = nama_mahasiswa;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }
}
