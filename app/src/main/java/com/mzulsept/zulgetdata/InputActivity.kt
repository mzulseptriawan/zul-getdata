package com.mzulsept.zulgetdata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.mzulsept.zulgetdata.network.NetworkConfig
import com.mzulsept.zulgetdata.model.SubmitModel
import com.mzulsept.zulgetdata.databinding.ActivityInputBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import cn.pedant.SweetAlert.SweetAlertDialog

class InputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSimpan.setOnClickListener {
            saveData()
        }
//        setContentView(R.layout.activity_main)
    }

    private fun saveData() {
        val nim = binding.txtNim.text.toString()
        val namaLengkap = binding.txtNama.text.toString()
        val usia = binding.txtUsia.text.toString()
        val alamat = binding.txtAlamat.text.toString()
//        val gender = binding.txtGender.text.toString()
        // radio button
        val rbPria: RadioButton = binding.rbPria
        val rbWanita: RadioButton = binding.rbWanita

        // validasi radiobutton pada jenisKelamin
        val gender = when {
            rbPria.isChecked -> "Pria"
            rbWanita.isChecked -> "Wanita"
            else -> "" // Jika tidak ada yang dipilih
        }

        val retrofit = NetworkConfig().getService()

        if (namaLengkap.isNotEmpty() || usia.isNotEmpty()) {
            retrofit.addMahasiswa(nim, namaLengkap, usia, alamat, gender)
                .enqueue(object : Callback<SubmitModel>{
                    override fun onResponse (
                        call: Call<SubmitModel>,
                        response: Response<SubmitModel>
                    ) {
                        if (response.isSuccessful) {
                            val hasil = response.body()
                            SweetAlertDialog(this@InputActivity, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data berhasil disimpan!")
                                .setConfirmClickListener {
                                    // tutup dialog ketika OK ditekan
                                    it.dismissWithAnimation()
                                }
                                .show()
                            binding.txtNim.text.clear()
                            binding.txtNama.text.clear()
                            binding.txtUsia.text.clear()
                            binding.txtAlamat.text.clear()
//                            binding.txtGender.text.clear()
                            rbPria.isChecked = false
                            rbWanita.isChecked = false
                        }
                    }

                    override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                        Toast.makeText(applicationContext, "Data gagal disimpan.", Toast.LENGTH_SHORT).show()
                    }
                })
        } else {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("Data tidak boleh kosong!")
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                }
                .show()
        }
    }
}