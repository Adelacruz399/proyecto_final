package com.example.project;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login-phone-dni") // La ruta a tu API
    Call<LoginResponse> login(
            @Field("telefono") String telefono,
            @Field("dni") String dni
    );
    @FormUrlEncoded
    @POST("verify-code")
    Call<Void> verifyCode(
            @Field("email") String email,
            @Field("code") String code
    );

}
