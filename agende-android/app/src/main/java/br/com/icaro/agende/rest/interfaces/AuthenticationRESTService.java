package br.com.icaro.agende.rest.interfaces;


import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationRESTService {

    @POST("login")
    Call<JWToken> login(@Body User user);

    @POST("token/refresh")
    Call<JWToken> refresh(@Body JWToken refreshToken);

}
