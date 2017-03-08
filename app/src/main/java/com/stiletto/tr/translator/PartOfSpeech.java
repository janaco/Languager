package com.stiletto.tr.translator;

/**
 * Created by yana on 08.03.17.
 */

public enum PartOfSpeech {

    VERB("verb"),
    NOUN("noun"),
    ADJECTIVE("adjective"),
    DETERMINER("determiner"),
    ADVERB("adverb"),
    PRONOUN("pronoun"),
    PREPOSITION("preposition"),
    CONJUNCTION("conjunction"),
    INTERJECTION("interjection"),
    NUMERAL("numeral"),
    UNKNOWN("");

    private String name;

    PartOfSpeech(String pos) {
        this.name = pos;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PartOfSpeech getPartOfSpeech(String pos) {

        for (PartOfSpeech partOfSpeech : values()) {

            if (partOfSpeech.getName().equalsIgnoreCase(pos)) {
                return partOfSpeech;
            }
        }

        return UNKNOWN;
    }
}
