package br.com.icaro.agende.rest;


import androidx.annotation.Nullable;

import java.io.IOException;


import br.com.icaro.agende.utils.SessionUtils;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Route;

public class APIRESTAuthenticator implements Authenticator {

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, okhttp3.Response response) throws IOException {
        String requestPath = response.request().url().encodedPath();

        if (!APIRESTService.noAuthRequired.contains(requestPath) && response.code() == 401 && SessionUtils.isLogged()) {
            String access = SessionUtils.refreshToken();
            if (access != null)
                return response.request().newBuilder().header("Authorization", access).build();
        }
        return null;
    }
}
