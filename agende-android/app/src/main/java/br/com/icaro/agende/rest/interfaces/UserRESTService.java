package br.com.icaro.agende.rest.interfaces;

import java.util.List;

import br.com.icaro.agende.model.User;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserRESTService {
    @GET("user")
    Call<List<User>> getUsers();
}
