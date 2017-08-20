package com.nandy.reader.core;

import com.nandy.reader.model.word.Word;

/**
 * Created by yana on 26.02.17.
 */

public interface TranslationCallback {

    void newTranslation(Word word);
}
