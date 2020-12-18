package com.example.testretro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface urlService
{
    @POST("/predict1")
    Call<url_model> predict(@Body url_model m_url_model);
}
