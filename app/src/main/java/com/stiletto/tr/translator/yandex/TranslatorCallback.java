package com.stiletto.tr.translator.yandex;

import com.stiletto.tr.model.DictionaryItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by yana on 11.03.17.
 */

public interface TranslatorCallback {

    void translationSuccess(List<DictionaryItem> items);

    void translationFailure(Call call, Response response);

    void translationError(Call call, Throwable error);
}
