package com.stiletto.tr.translator.yandex;

import android.util.Log;

import com.stiletto.tr.model.DictionaryItem;
import com.stiletto.tr.translator.yandex.model.YandexDictionaryResponse;
import com.stiletto.tr.translator.yandex.model.YandexTranslateResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


/**
 * Requests to Yandex Translator and Yandex Dictionary Services.
 * <p>
 * Created by yana on 05.01.17.
 */

public class Translator {

    public static void translate(final CharSequence textToTranslate, final Language languageFrom, final Language languageTo, final TranslatorCallback callback) {

        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_TRANSLATOR_KEY);
        map.put("lang", languageFrom.toString() + "-" + languageTo.toString());
        map.put("text", textToTranslate.toString());
        Call<YandexTranslateResponse> call = getService(Api.TRANSLATOR_URL).translate(map);
        call.enqueue(new Callback<YandexTranslateResponse>() {
            @Override
            public void onResponse(Call<YandexTranslateResponse> call, Response<YandexTranslateResponse> response) {
                if (response.isSuccessful()) {
                    DictionaryItem dictionaryItem = response.body().getAsDictionaryItem(textToTranslate);
                    dictionaryItem.setOriginLanguage(languageFrom);
                    dictionaryItem.setTranslationLanguage(languageTo);

                    DictionaryItem[] items = new DictionaryItem[]{dictionaryItem};
                    callback.translationSuccess(Arrays.asList(items));
                } else {
                    callback.translationFailure(call, response);
                }
            }

            @Override
            public void onFailure(Call<YandexTranslateResponse> call, Throwable t) {
                callback.translationError(call, t);
            }
        });
    }

    public static void getDictionary(final CharSequence textToTranslate, final Language languageFrom,
                                     final Language languageTo, final TranslatorCallback callback) {
        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_DICTIONARY_KEY);
        map.put("lang", languageFrom.toString() + "-" + languageTo.toString());
        map.put("text", textToTranslate.toString());
        final Call<YandexDictionaryResponse> call = getService(Api.DICTIONARY_URL).lookup(map);

        call.enqueue(new Callback<YandexDictionaryResponse>() {
            @Override
            public void onResponse(Call<YandexDictionaryResponse> call, Response<YandexDictionaryResponse> response) {
                if (response.isSuccessful()) {
                    List<DictionaryItem> list = new ArrayList<>();
                    for (DictionaryItem item : response.body().getItems()) {
                        item.setOriginLanguage(languageFrom);
                        item.setTranslationLanguage(languageTo);
                        list.add(item);
                    }
                    callback.translationSuccess(list);
                } else {
                    callback.translationFailure(call, response);
                }
            }

            @Override
            public void onFailure(Call<YandexDictionaryResponse> call, Throwable t) {
                callback.translationError(call, t);
            }
        });
    }

    private static OkHttpClient getClient() {

        return new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .build();
    }

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
        Call<YandexTranslateResponse> translate(@QueryMap Map<String, String> params);

        @GET("lookup?")
        Call<YandexDictionaryResponse> lookup(@QueryMap Map<String, String> params);


    }
}
