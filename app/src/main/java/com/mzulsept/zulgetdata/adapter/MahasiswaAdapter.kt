package com.mzulsept.zulgetdata.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mzulsept.zulgetdata.model.DataMahasiswa
import com.mzulsept.zulgetdata.network.NetworkConfig
import com.mzulsept.zulgetdata.R
import com.mzulsept.zulgetdata.databinding.ItemMahasiswaBinding


data class MahasiswaAdapter(
    private val listMahasiswa : List<DataMahasiswa>?,
    private val onListItemClick : (DataMahasiswa?)-> Unit
): RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>(){

    inner class ViewHolder(val ListPostsAdapterBinding : ItemMahasiswaBinding) :
        RecyclerView.ViewHolder(ListPostsAdapterBinding.root) {
        fun onBindItem(dataItem2: DataMahasiswa?){
            Glide.with(ListPostsAdapterBinding.root.context)
                .load(NetworkConfig().GAMBAR_URL + dataItem2?.foto)
                .apply(RequestOptions().placeholder(R.drawable.baseline_person))
                .into(ListPostsAdapterBinding.gambar)
            ListPostsAdapterBinding.txtNim.text = dataItem2?.nim.toString()
            ListPostsAdapterBinding.txtNama.text = dataItem2?.namalengkap
            ListPostsAdapterBinding.txtUsia.text = dataItem2?.usia
            ListPostsAdapterBinding.txtAlamat.text = dataItem2?.alamat
            ListPostsAdapterBinding.txtGender.text = dataItem2?.gender
            ListPostsAdapterBinding.root.setOnClickListener{
                onListItemClick(dataItem2)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMahasiswaBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindItem(listMahasiswa?.get(position))
    }

    override fun getItemCount(): Int {
        return listMahasiswa?.size?:0
    }
}