package br.com.icaro.agende.utils;


import java.io.IOException;

import br.com.icaro.agende.R;
import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.rest.APIRESTService;
import br.com.icaro.agende.rest.interfaces.AuthenticationRESTService;
import retrofit2.Call;
import retrofit2.Response;

public class SessionUtils {

    private static final String EMPTY = "";
    private static AuthenticationRESTService authRESTService = APIRESTService.getRESTService(AuthenticationRESTService.class);

    public static void writeToken(JWToken token) {
        String access, refresh;

        if (token != null) {
            access = token.getAccess();
            refresh = token.getRefresh();
        }
        else {
            access = EMPTY;
            refresh = EMPTY;
        }
        SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.access_token), access);
        SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.refresh_token), refresh);
    }

    public static JWToken readToken(){
        String access = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.access_token));
        String refresh = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.refresh_token));
        return (access.equals(EMPTY) && refresh.equals(EMPTY))? null : new JWToken(access, refresh);
    }

    public static void writeUser(String username){
        if(username != null){
            SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.logged_user), username);
        }
    }

    public static String readUser(){
        String username = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.logged_user));
        return (username.equals(EMPTY))? null : username;
    }

    public static boolean isLogged(){
        return SessionUtils.readToken() != null;
    }

    public static String refreshToken() throws IOException {
        JWToken oldToken = readToken();
        Call<JWToken> refreshCall = authRESTService.refresh(oldToken);

        Response<JWToken> refreshResponse = refreshCall.execute();
        if (refreshResponse.code() == 200) {
            JWToken newAccessToken = refreshResponse.body();
            if (newAccessToken != null && oldToken != null) {
                JWToken newToken = new JWToken(newAccessToken.getAccess(), oldToken.getRefresh());
                writeToken(newToken);
                return newToken.getAccess();
            }
        }

        return null;
    }
}
