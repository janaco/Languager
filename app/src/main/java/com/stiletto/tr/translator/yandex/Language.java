package com.stiletto.tr.translator.yandex;

import java.util.Locale;

/**
 * Created by yana on 05.01.17.
 */
public enum Language {

    ALBANIAN("sq"),
    ARMENIAN("hy"),
    AZERBAIJANI("az"),
    BELARUSIAN("be"),
    BULGARIAN("bg"),
    CATALAN("ca"),
    CROATIAN("hr"),
    CZECH("cs"),
    DANISH("da"),
    DUTCH("nl"),
    ENGLISH("en"),
    ESTONIAN("et"),
    FINNISH("fi"),
    FRENCH("fr"),
    GERMAN("de"),
    GEORGIAN("ka"),
    GREEK("el"),
    HUNGARIAN("hu"),
    ITALIAN("it"),
    LATVIAN("lv"),
    LITHUANIAN("lt"),
    MACEDONIAN("mk"),
    NORWEGIAN("no"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    ROMANIAN("ro"),
    RUSSIAN("ru"),
    SERBIAN("sr"),
    SLOVAK("sk"),
    SLOVENIAN("sl"),
    SPANISH("es"),
    SWEDISH("sv"),
    TURKISH("tr"),
    UKRAINIAN("uk");


    private final String code;

    Language(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    public static Language getLanguage(String code) {

        switch (code.toLowerCase()) {

            case "sq":
                return ALBANIAN;

            case "hy":
                return ARMENIAN;

            case "az":
                return AZERBAIJANI;

            case "be":
                return BELARUSIAN;

            case "bg":
                return BULGARIAN;

            case "ca":
                return CATALAN;

            case "hr":
                return CROATIAN;

            case "cs":
                return CZECH;

            case "da":
                return DANISH;

            case "nl":
                return DUTCH;

            case "en":
                return ENGLISH;

            case "et":
                return ESTONIAN;

            case "fi":
                return FINNISH;

            case "fr":
                return FRENCH;

            case "de":
                return GERMAN;

            case "ka":
                return GEORGIAN;

            case "el":
                return GREEK;

            case "hu":
                return HUNGARIAN;

            case "it":
                return ITALIAN;

            case "lv":
                return LATVIAN;

            case "lt":
                return LITHUANIAN;

            case "mk":
                return MACEDONIAN;

            case "no":
                return NORWEGIAN;

            case "pl":
                return POLISH;

            case "pt":
                return PORTUGUESE;

            case "ro":
                return ROMANIAN;

            case "ru":
                return RUSSIAN;

            case "sr":
                return SERBIAN;

            case "sk":
                return SLOVAK;

            case "sl":
                return SLOVENIAN;

            case "es":
                return SPANISH;

            case "sv":
                return SWEDISH;

            case "tr":
                return TURKISH;

            case "uk":
                return UKRAINIAN;

            default:
                return null;
        }
    }


    public static Language getLanguageForCountry(String countryCode) {

        countryCode = countryCode.toLowerCase();

        for (Locale locale : Locale.getAvailableLocales()) {
            String country = locale.getCountry();

            if (country.toLowerCase().equalsIgnoreCase(countryCode)) {
                String langCode = locale.getLanguage();
                return getLanguage(langCode);
            }
        }
        return null;

    }
}
