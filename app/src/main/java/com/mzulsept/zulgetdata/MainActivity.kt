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
import com.mzulsept.zulgetdata.model.SubmitModel
import retrofit2.Response

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        binding.btnTambah.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }

        getMahasiswa()
    }

    override fun onRefresh() {
        getMahasiswa()
    }

    private fun getMahasiswa() {
        val networkService = NetworkConfig().getService()

        networkService.getMahasiswa().enqueue(object : Callback<ResponseMahasiswa> {
            override fun onResponse(
                call: Call<ResponseMahasiswa>,
                response: retrofit2.Response<ResponseMahasiswa>
            ) {
                binding.progressIndicator.visibility = View.GONE
                if (response.isSuccessful) {
                    val receivedDatas = response.body()?.data
                    setToAdapter(receivedDatas as List<DataMahasiswa>?)
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onFailure(call: Call<ResponseMahasiswa>, t: Throwable) {
                binding.progressIndicator.visibility = View.GONE
                Log.d("Retrofit failed", "onFailure : ${t.stackTrace}")
                binding.swipeRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun setToAdapter(receivedDatas: List<DataMahasiswa>?) {
        binding.newsList.adapter = MahasiswaAdapter(receivedDatas) {
            showOptionsDialog(it)
        }.apply {
            val lm = LinearLayoutManager(this@MainActivity)
            binding.newsList.layoutManager = lm
            binding.newsList.itemAnimator = DefaultItemAnimator()
            binding.newsList.adapter = this
        }
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
        val intent = Intent(this, EditActivity::class.java).apply {
            putExtra("nim", dataItem?.nim.toString())
            putExtra("nama", dataItem?.namalengkap)
            putExtra("usia", dataItem?.usia)
            putExtra("alamat", dataItem?.alamat)
            putExtra("gender", dataItem?.gender)
            putExtra("foto", dataItem?.foto)
        }
        startActivity(intent)
    }

    private fun deleteData(dataItem: DataMahasiswa?) {
        val apiServices = NetworkConfig().getService()
        dataItem?.nim?.let {
            apiServices.deleteMahasiswa(it).enqueue(object : Callback<SubmitModel> {
                override fun onResponse(call: Call<SubmitModel>, response: Response<SubmitModel>) {
                    handleDeleteResponse(response)
                }

                override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                    handleDeleteFailure()
                }
            })
        }
    }

    private fun handleDeleteResponse(response: Response<SubmitModel>) {
        if (response.isSuccessful) {
            Toast.makeText(this@MainActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            getMahasiswa()
        } else {
            Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleDeleteFailure() {
        Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
    }
}
