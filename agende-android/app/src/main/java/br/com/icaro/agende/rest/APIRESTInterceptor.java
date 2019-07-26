package br.com.icaro.agende.rest;


import java.io.IOException;


import br.com.icaro.agende.model.JWToken;
import br.com.icaro.agende.utils.SessionUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class APIRESTInterceptor implements Interceptor {

    private static final String EMPTY = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String requestPath = request.url().encodedPath();

        if (!APIRESTService.noAuthRequired.contains(requestPath)) {
            JWToken jwt = SessionUtils.readToken();
            String accessToken = (jwt != null) ? jwt.getAccess() : EMPTY;
            request = request.newBuilder().addHeader("Authorization", accessToken).build();
        }

        return  chain.proceed(request);
    }
}