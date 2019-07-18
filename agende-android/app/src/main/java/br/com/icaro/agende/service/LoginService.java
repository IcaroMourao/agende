package br.com.icaro.agende.service;

import android.content.Context;

import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.listener.AuthenticationListener;
import br.com.icaro.agende.service.rest.APIRESTService;
import br.com.icaro.agende.service.rest.LoginRESTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {

    private static final String FAILED_LOGIN_MESSAGE = "Login ou senha inválidos.";
    private static final String SUCCESS_LOGIN_MESSAGE = "Login efetuado com sucesso.";
    public static final String SERVER_CONNECT_ERROR_MESSAGE = "Erro ao conectar com o servidor";

    private AuthenticationListener authenticationListener;
    private String username;
    private String password;
    private LoginRESTService loginService;

    public LoginService(Context context) {
        loginService = new APIRESTService<LoginRESTService>(context).getRESTService(LoginRESTService.class);
    }


    public void setCredentials(String registration, String password) {
        this.username = registration;
        this.password = password;
    }

    public void setAuthenticationListener(AuthenticationListener listener) {
        this.authenticationListener = listener;
    }


    public void authenticate(Context context) {
        Call<JWToken> tokenCall = loginService.login(new User(this.username, this.password));
        tokenCall.enqueue(new Callback<JWToken>() {
            @Override
            public void onResponse(Call<JWToken> call, Response<JWToken> response) {
                if(response.code() == 200){
                    JWToken token = response.body();
                    notifyAuthenticationResult(token, SUCCESS_LOGIN_MESSAGE);
                }else {
                    notifyAuthenticationResult(null, FAILED_LOGIN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<JWToken> call, Throwable t) {
                notifyAuthenticationResult(null, SERVER_CONNECT_ERROR_MESSAGE);
            }
        });
    }

    private void notifyAuthenticationResult(JWToken token, String notification) {
        if(authenticationListener != null)
            if (token != null)
                authenticationListener.onSuccess(token);
            else
                authenticationListener.onFailure(notification);
    }
}
