package com.mzulsept.zulgetdata

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mzulsept.zulgetdata.model.DataMahasiswa
import com.mzulsept.zulgetdata.adapter.MahasiswaAdapter
import com.mzulsept.zulgetdata.model.ResponseMahasiswa
import com.mzulsept.zulgetdata.network.NetworkConfig
import com.mzulsept.zulgetdata.InputActivity
import com.mzulsept.zulgetdata.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import android.app.AlertDialog
import android.widget.Toast
//import com.mzulsept.zulgetdata.UbahActivity

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        // Panggil metode getMahasiswa() untuk memuat data dari server
        getMahasiswa()
        binding.btnTambah.setOnClickListener {
            // Membuat intent untuk pindah ke TambahActivity
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRefresh() {
        // Panggil metode getMahasiswa() untuk memuat ulang data dari server
        getMahasiswa()
    }

    private fun getMahasiswa() {
        NetworkConfig().getService()
            .getMahasiswa()
            .enqueue(object : Callback<ResponseMahasiswa> {
                override fun onResponse(call: Call<ResponseMahasiswa>, response: retrofit2.Response<ResponseMahasiswa>) {
                    binding.progressIndicator.visibility = View.GONE
                    if (response.isSuccessful) {
                        val receivedDatas = response.body()?.data
                        setToAdapter(receivedDatas as List<DataMahasiswa>?)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false // Beritahu bahwa proses refresh selesai
                }

                @SuppressLint("SuspiciousIndentation")
                override fun onFailure(call: Call<ResponseMahasiswa>, t: Throwable) {
                    this@MainActivity.binding.progressIndicator.visibility = View.GONE
                    Log.d("Retrofit failed", "onFailure : ${t.stackTrace}")
                    binding.swipeRefreshLayout.isRefreshing = false // Beritahu bahwa proses refresh selesai
                }
            })
    }

    private fun setToAdapter(receivedDatas: List<DataMahasiswa>?) {
        binding.newsList.adapter = null
        val adapter = MahasiswaAdapter(receivedDatas) {
            showOptionsDialog(it)
        }
        val lm = LinearLayoutManager(this)
        binding.newsList.layoutManager = lm
        binding.newsList.itemAnimator = DefaultItemAnimator()
        binding.newsList.adapter = adapter
    }

    private fun showOptionsDialog(dataItem: DataMahasiswa?) {
        val options = arrayOf("Edit Data", "Delete Data")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> editData(dataItem)
                1 -> deleteData(dataItem)
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun editData(dataItem: DataMahasiswa?) {
//        val intent = Intent(this, UbahActivity::class.java)
        intent.putExtra("nim", dataItem?.nim.toString())
        intent.putExtra("nama", dataItem?.namalengkap)
        intent.putExtra("usia", dataItem?.usia)
        intent.putExtra("alamat", dataItem?.alamat)
        intent.putExtra("gender", dataItem?.gender)
        intent.putExtra("foto", dataItem?.foto)
        startActivity(intent)
    }

    private fun deleteData(dataItem: DataMahasiswa?) {
        val apiServices = NetworkConfig().getService()
        dataItem?.nim?.let {
            apiServices.deleteMahasiswa(it).enqueue(object : Callback<ResponseMahasiswa> {
                override fun onResponse(call: Call<ResponseMahasiswa>, response: retrofit2.Response<ResponseMahasiswa>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                        getMahasiswa() // Refresh data setelah penghapusan
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseMahasiswa>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            })
            }
        }

}