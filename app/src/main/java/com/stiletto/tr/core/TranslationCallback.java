package com.stiletto.tr.core;

import com.stiletto.tr.translator.yandex.SimpleTranslation;

/**
 * Created by yana on 26.02.17.
 */

public interface TranslationCallback {

    void newTranslation(CharSequence originText, SimpleTranslation translation);
}
