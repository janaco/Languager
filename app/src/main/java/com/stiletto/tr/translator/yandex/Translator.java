package com.stiletto.tr.translator.yandex;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


/**
 * Created by yana on 05.01.17.
 */

public class Translator {

    public static void translate(String textToTranslate, Language languageFrom, Language languageTo, Callback<Translation> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_API_KEY);
        map.put("lang", languageFrom.toString() + "-" + languageTo.toString());
        map.put("text", textToTranslate);
        Call<Translation> call = getService().translate(map);
        call.enqueue(callback);
    }

    private static OkHttpClient getClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .build();
    }

    //    https://translate.yandex.net/api/v1.5/tr.json/translate?
    // key=trnsl.1.1.20170105T192130Z.763c3568bf7993c1.410e91622919218bdb9e9afddfa05f48d5280716
    // &lang=en-uk&text=Project

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Api.URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    private static Api getService() {

        return getRetrofit().create(Api.class);
    }

    private interface Api {
        String YANDEX_API_KEY = "trnsl.1.1.20170105T192130Z.763c3568bf7993c1.410e91622919218bdb9e9afddfa05f48d5280716";
        String URL = "https://translate.yandex.net/api/v1.5/tr.json/";

        @POST("translate?")
        Call<Translation> translate(@QueryMap Map<String, String> params);


    }
}
