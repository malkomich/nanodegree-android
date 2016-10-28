package com.github.malkomich.nanodegree.data.webservice;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory class to get Retrofit instances.
 */
public class HttpClientGenerator {

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder builder =
        new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return builder.baseUrl(baseUrl)
            .client(httpClient.build())
            .build()
            .create(serviceClass);
    }
}
