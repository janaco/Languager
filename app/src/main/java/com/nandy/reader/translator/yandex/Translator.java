package com.nandy.reader.translator.yandex;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nandy.reader.model.word.Dictionary;
import com.nandy.reader.model.word.RealmString;
import com.nandy.reader.model.word.Word;
import com.nandy.reader.translator.yandex.utils.RealmStringListTypeAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.realm.RealmList;
import okhttp3.OkHttpClient;
import retrofit2.Call;
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

    public interface Callback<T> {

        void translationSuccess(T item);

        void translationFailure(Call call, Response response);

        void translationError(Call call, Throwable error);
    }


    public static void translate(final CharSequence textToTranslate, final Language[] languages,
                                 final Callback<Word> callback) {

        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_TRANSLATOR_KEY);
        map.put("lang", languages[0].toString() + "-" + languages[1].toString());
        map.put("text", textToTranslate.toString());

        Log.d("TRANSLATOR_", "map: " + map);
        Call<Word> call = getService(Api.TRANSLATOR_URL).translate(map);

        Log.d("TRANSLATOR_", "call.url: " + call.request().url());

        call.enqueue(new retrofit2.Callback<Word>() {
            @Override
            public void onResponse(Call<Word> call, Response<Word> response) {
                Log.d("TRANSLATOR_", "onResponse: " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Word word = response.body();
                    word.setText(textToTranslate.toString());
                    word.setOriginLanguage(languages[0]);
                    word.setTranslationLanguage(languages[1]);
                    callback.translationSuccess(word);
                } else {
                    callback.translationFailure(call, response);
                }
            }

            @Override
            public void onFailure(Call<Word> call, Throwable t) {
                Log.d("TRANSLATOR_", "onFailure: " + t.getMessage());
                callback.translationError(call, t);
            }
        });
    }

    public static void getDictionary(final CharSequence textToTranslate, final Language[] languages,
                                     final Callback<Dictionary> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_DICTIONARY_KEY);
        map.put("lang", languages[0].toString() + "-" + languages[1].toString());
        map.put("text", textToTranslate.toString());
        final Call<Dictionary> call = getService(Api.DICTIONARY_URL).lookup(map);

        Log.d("TRANSLATOR", "dictionary.url: " + call.request().url());

        call.enqueue(new retrofit2.Callback<Dictionary>() {
            @Override
            public void onResponse(Call<Dictionary> call, Response<Dictionary> response) {
                if (response.isSuccessful()) {

                    callback.translationSuccess(response.body());
                } else {
                    callback.translationFailure(call, response);
                }
            }

            @Override
            public void onFailure(Call<Dictionary> call, Throwable t) {
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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<RealmList<RealmString>>() {
                        }.getType(),
                        RealmStringListTypeAdapter.INSTANCE)
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
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
        Call<Word> translate(@QueryMap Map<String, String> params);

        @GET("lookup?")
        Call<Dictionary> lookup(@QueryMap Map<String, String> params);


    }
}
