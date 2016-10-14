package com.github.malkomich.nanodegree.data;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malkomich on 14/10/2016.
 */
public class HttpClientGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
        new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return builder.baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
            .create(serviceClass);
    }
}
