package com.mzulsept.zulgetdata.model

import com.google.gson.annotations.SerializedName

data class ResponseMahasiswa(

	@field:SerializedName("data")
	val data: List<DataMahasiswa?>? = null
)

data class DataMahasiswa(

	@field:SerializedName("usia")
	val usia: String? = null,

	@field:SerializedName("nim")
	val nim: Int? = null,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("namalengkap")
	val namalengkap: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("alamat")
	val alamat: String? = null
)
