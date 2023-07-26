package com.example.storyapp.data.remote

import com.example.storyapp.data.dataClass.FileUploadResponse
import com.example.storyapp.data.dataClass.LoginResponse
import com.example.storyapp.data.dataClass.SignupResponse
import com.example.storyapp.data.dataClass.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun signup(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password : String
    ):Call<SignupResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String
    ):Call<LoginResponse>


    @GET("stories")
    fun getAllStory():Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Part file : MultipartBody.Part,
        @Part ("description") description : RequestBody
    ):Call<FileUploadResponse>
}