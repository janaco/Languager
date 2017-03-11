package com.stiletto.tr.core;

import com.stiletto.tr.model.DictionaryItem;

/**
 * Created by yana on 26.02.17.
 */

public interface TranslationCallback {

    void newTranslation(CharSequence originText, DictionaryItem item);
}
