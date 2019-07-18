package br.com.icaro.agende.service.rest;


import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRESTService {

    @POST("login")
    Call<JWToken> login(@Body User user);

}
