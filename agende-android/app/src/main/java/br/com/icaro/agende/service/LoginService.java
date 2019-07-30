package br.com.icaro.agende.service;

import br.com.icaro.agende.listener.AuthenticationListener;
import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.model.User;
import br.com.icaro.agende.rest.APIRESTService;
import br.com.icaro.agende.rest.interfaces.AuthenticationRESTService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginService {

    private static final String FAILED_LOGIN_MESSAGE = "Login ou senha inválidos.";
    private static final String SUCCESS_LOGIN_MESSAGE = "Login efetuado com sucesso.";
    private static final String SERVER_CONNECTION_ERROR_MESSAGE = "Erro ao conectar com o servidor";

    private AuthenticationListener authenticationListener;

    private AuthenticationRESTService loginRESTService;

    public LoginService() {
        this.loginRESTService = APIRESTService.getRESTService(AuthenticationRESTService.class);
    }

    public void setAuthenticationListener(AuthenticationListener listener) {
        this.authenticationListener = listener;
    }


    public void authenticate(String username, String password) {
        Call<JWToken> tokenCall = loginRESTService.login(new User(username, password));
        tokenCall.enqueue(new Callback<JWToken>() {
            @Override
            public void onResponse(Call<JWToken> call, Response<JWToken> response) {
                if(response.code() == 200){
                    JWToken token = response.body();
                    notifyAuthenticationResult(token, SUCCESS_LOGIN_MESSAGE);
                } else {
                    notifyAuthenticationResult(null, FAILED_LOGIN_MESSAGE);
                }
            }

            @Override
            public void onFailure(Call<JWToken> call, Throwable t) {
                notifyAuthenticationResult(null, SERVER_CONNECTION_ERROR_MESSAGE);
            }
        });
    }

    private void notifyAuthenticationResult(JWToken token, String message) {
        if(authenticationListener != null)
            if (token != null)
                authenticationListener.onSuccess(token);
            else
                authenticationListener.onFailure(message);
    }
}
