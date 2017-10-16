package com.nandy.reader.translator.yandex;

import android.util.Pair;

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

import io.reactivex.Single;
import io.realm.RealmList;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
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

    public static Single<Word> translate(final CharSequence textToTranslate, Pair<Language, Language> languages) {

        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_TRANSLATOR_KEY);
        map.put("lang", languages.first.toString() + "-" + languages.second.toString());
        map.put("text", textToTranslate.toString());

        return getService(Api.TRANSLATOR_URL).translate(map);
    }

    public static Single<Dictionary> getDictionary(final CharSequence textToTranslate, Pair<Language, Language> languages) {
        Map<String, String> map = new HashMap<>();
        map.put("key", Api.YANDEX_DICTIONARY_KEY);
        map.put("lang", languages.first.toString() + "-" + languages.second.toString());
        map.put("text", textToTranslate.toString());
        return getService(Api.DICTIONARY_URL).lookup(map);

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
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
        Single<Word> translate(@QueryMap Map<String, String> params);

        @GET("lookup?")
        Single<Dictionary> lookup(@QueryMap Map<String, String> params);


    }
}
