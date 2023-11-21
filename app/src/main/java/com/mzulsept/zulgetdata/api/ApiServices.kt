package com.mzulsept.zulgetdata.api

import com.mzulsept.zulgetdata.model.DataMahasiswa
import com.mzulsept.zulgetdata.model.ResponseMahasiswa
import com.mzulsept.zulgetdata.model.SubmitModel
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServices {
    @GET("mahasiswa")
    fun getMahasiswa():
            Call<ResponseMahasiswa>

    // add data
    @FormUrlEncoded
    @POST("storedata")
    fun addMahasiswa(
        @Field("nim") nim: String,
        @Field("namalengkap") namalengkap: String,
        @Field("usia") usia: String,
        @Field("alamat") alamat: String,
        @Field("gender") gender:String,
    ): Call<SubmitModel>

    @DELETE("delete/{nim}")
    fun deleteMahasiswa(@Path("nim") id: Int): Call<SubmitModel>

    @FormUrlEncoded
    @POST("update")
    fun updateMahasiswa(
        @Field("nim") nim: String,
        @Field("namalengkap") namalengkap: String,
        @Field("usia") usia: String,
        @Field("alamat") alamat: String,
        @Field("gender") gender:String,
    ): Call<ResponseMahasiswa>
}