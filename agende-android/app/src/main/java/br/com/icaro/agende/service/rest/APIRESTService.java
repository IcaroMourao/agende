package br.com.icaro.agende.service.rest;

import android.content.Context;

import java.io.IOException;

import br.com.icaro.agende.utils.SessionUtils;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class APIRESTService<T> {

//    public static final String API_URL = "http://ec2-18-220-95-237.us-east-2.compute.amazonaws.com:8080/api/app/";
//    public static final String API_URL = "http://10.0.2.2:8080/api/app/";
    public static final String API_URL = "http://192.168.0.169:8080/api/app/";

    private final Retrofit retrofit;
    private OkHttpClient okHttpClient;

    public APIRESTService(final Context context) {
        okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String access = "";
                if(SessionUtils.isLogged(context)){
                    access = SessionUtils.readToken(context).getAccess();
                }
                Request newRequest = chain.request().newBuilder().addHeader("Authorization", access).build();
                return chain.proceed(newRequest);
            }
        }).build();
        retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(API_URL).addConverterFactory(JacksonConverterFactory.create()).build();
    }

    public T getRESTService(Class<T> RESTService) {
        return retrofit.create(RESTService);
    }

}
