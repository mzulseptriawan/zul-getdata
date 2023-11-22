package com.mzulsept.zulgetdata

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mzulsept.zulgetdata.model.ResponseMahasiswa
import com.mzulsept.zulgetdata.model.SubmitModel
import com.mzulsept.zulgetdata.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditActivity : AppCompatActivity() {
    private lateinit var namaEditText: EditText
    private lateinit var usiaEditText: EditText
    private lateinit var nimEditText: EditText
    private lateinit var alamatEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var simpanButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        namaEditText = findViewById(R.id.txtNama)
        usiaEditText = findViewById(R.id.txtUsia)
        nimEditText = findViewById(R.id.txtNim)
        alamatEditText = findViewById(R.id.txtAlamat)
        genderRadioGroup = findViewById(R.id.txtGender)
        simpanButton = findViewById(R.id.btn_simpan)

        // Ambil data yang akan diubah dari Intent
        val intent = intent
        val nama = intent.getStringExtra("nama")
        val nim = intent.getStringExtra("nim")
        val usia = intent.getStringExtra("usia")
        val alamat = intent.getStringExtra("alamat")
        val gender = intent.getStringExtra("gender")

        // Isi EditText dengan data yang diambil
        namaEditText.setText(nama)
        usiaEditText.setText(usia)
        nimEditText.setText(nim)
        alamatEditText.setText(alamat)

        // Implementasikan logika untuk menyimpan perubahan data
        simpanButton.setOnClickListener {
            val updatedNama = namaEditText.text.toString()
            val updatedNim = nimEditText.text.toString()
            val updatedUsia = usiaEditText.text.toString()
            val updatedAlamat = alamatEditText.text.toString()

            // Mendapatkan ID radio button yang dipilih
            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            val selectedGender = if (selectedGenderId == R.id.radioButtonMale) "Laki-laki" else "Perempuan"

            val apiServices = NetworkConfig().getService()
            apiServices.updateMahasiswa(updatedNim, updatedNama, updatedUsia, updatedAlamat, selectedGender)
                .enqueue(object : Callback<SubmitModel> {
                    override fun onResponse(
                        call: Call<SubmitModel>,
                        response: Response<SubmitModel>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@EditActivity,
                                "Data berhasil diubah",
                                Toast.LENGTH_SHORT
                            ).show()
                            setResult(RESULT_OK)
                            finish()
                        } else {
                            // Gagal memperbarui data
                            Toast.makeText(
                                this@EditActivity,
                                "Gagal mengubah data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<SubmitModel>, t: Throwable) {
                        // Gagal melakukan permintaan ke server
                        Toast.makeText(
                            this@EditActivity,
                            "Terjadi kesalahan jaringan",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }
}
