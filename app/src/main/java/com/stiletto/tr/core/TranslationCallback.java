package com.stiletto.tr.core;

import com.stiletto.tr.translator.yandex.Translation;

/**
 * Created by yana on 26.02.17.
 */

public interface TranslationCallback {

    void newTranslation(CharSequence originText, Translation translation);
}