package com.stiletto.tr.translator.yandex;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * {
 * "code": 200,
 * "lang": "en-uk",
 * "text": [
 * "Проект"
 * ]
 * }
 * <p>
 * Created by yana on 05.01.17.
 */

public class Translation {

    @SerializedName("lang")
    String langunage;
    @SerializedName("text")
    String[] text;

    CharSequence origin;

    public String getLangunage() {
        return langunage;
    }

    public String[] getTranslations() {
        return text;
    }

    public  String getTranslationAsString(){

      if (text == null){
          return "";
      }

        return Arrays.asList(text).toString().replace("[", "").replace("]", "");
    }

    public CharSequence getOrigin(){
        return origin;
    }

    public void setOrigin(CharSequence origin) {
        this.origin = origin;
    }
}
