package com.deni.workshop.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deni.workshop.R;
import com.deni.workshop.model.Mahasiswa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Deni Supriyatna on 09 - Nov - 2019.
 */
public class MahasiswaAdapter extends RecyclerView.Adapter
        <MahasiswaAdapter.MahasiswaViewHolder> {

    private List<Mahasiswa> list = new ArrayList<>();

    public class MahasiswaViewHolder extends
            RecyclerView.ViewHolder {
        public TextView textNpmMhs, textNamaMhs, textJkMhs,
                textJurusanMhs,
                textNoHpMhs, textEmailMhs, textAgamaMhs;

        public MahasiswaViewHolder(@NonNull View itemView){
            super(itemView);
            textNpmMhs = itemView.findViewById(R.id.text_npm_mhs);
            textNamaMhs = itemView.findViewById(R.id.text_nama_mhs);
            textJkMhs = itemView.findViewById(R.id.text_jk_mhs);
            textJurusanMhs = itemView.findViewById(R.id.text_jurusan_mhs);
            textNoHpMhs = itemView.findViewById(R.id.text_no_hp_mhs);
            textEmailMhs = itemView.findViewById(R.id.text_email_mhs);
            textAgamaMhs = itemView.findViewById(R.id.text_agama_mhs);
        }

        public void bind(final Mahasiswa item,
                         final OnItemClickListener listener){
            textNpmMhs.setText(item.getNpm());
            textNamaMhs.setText(item.getNama_mahasiswa());
            textJkMhs.setText(item.getJenis_kelamin());
            textJurusanMhs.setText(item.getJurusan());
            textNoHpMhs.setText(item.getNo_hp());
            textEmailMhs.setText(item.getEmail());
            textAgamaMhs.setText(item.getAgama());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(Mahasiswa item);
    }

    private OnItemClickListener listener;

    public MahasiswaAdapter(){

    }

    public MahasiswaAdapter(List<Mahasiswa>list){
        this.list = list;
    }

    public MahasiswaAdapter(List<Mahasiswa> list,
                            OnItemClickListener listener){
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MahasiswaAdapter.MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.mahasiswa_item_layout, viewGroup, false);
        MahasiswaViewHolder holder = new MahasiswaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaAdapter.MahasiswaViewHolder mahasiswaViewHolder, int i) {
        Mahasiswa item = list.get(i);
        mahasiswaViewHolder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
