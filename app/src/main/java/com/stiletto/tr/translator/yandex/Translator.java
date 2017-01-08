package com.stiletto.tr.translator.yandex;

import android.util.Log;

import com.stiletto.tr.translator.yandex.model.Dictionary;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


/**
 * Created by yana on 05.01.17.
 */

public class Translator {

    public static void translate(String textToTranslate, Language languageFrom, Language languageTo, Callback<Translation> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_TRANSLATOR_KEY);
        map.put("lang", languageFrom.toString() + "-" + languageTo.toString());
        map.put("text", textToTranslate);
        Call<Translation> call = getService(Api.TRANSLATOR_URL).translate(map);
        call.enqueue(callback);
    }

    public static void getDictionary(String textToTranslate, Language languageFrom,
                                     Language languageTo, Callback<Dictionary> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_DICTIONARY_KEY);
        map.put("lang", languageFrom.toString() + "-" + languageTo.toString());
        map.put("text", textToTranslate);
        Call<Dictionary> call = getService(Api.DICTIONARY_URL).lookup(map);
        Log.d("DICTIONARY_", "call: " + call.request().url().toString() );

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

    private static Retrofit getRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    private static Api getService(String baseUrl) {

        return getRetrofit(baseUrl).create(Api.class);
    }

    private interface Api {
        String YANDEX_TRANSLATOR_KEY = "trnsl.1.1.20170105T192130Z.763c3568bf7993c1.410e91622919218bdb9e9afddfa05f48d5280716";
        String YANDEX_DICTIONARY_KEY = "dict.1.1.20170108T123722Z.2b44f6120ce589e1.d70df55a178cbf9b7ee10b472e9471c5e52d96f0";

        String TRANSLATOR_URL = "https://translate.yandex.net/api/v1.5/tr.json/";
        String DICTIONARY_URL = "https://dictionary.yandex.net/api/v1/dicservice.json/";

        @POST("translate?")
        Call<Translation> translate(@QueryMap Map<String, String> params);

        @GET("lookup?")
        Call<Dictionary> lookup(@QueryMap Map<String, String> params);


    }
}
