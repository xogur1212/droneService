package com.xy124.drone.config;


import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;

import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * json 에 xss 막기 위한 코드
 */
public class HtmlCharcterEscapes extends CharacterEscapes {

    private final int[] asciiEscapes;

    private final CharSequenceTranslator charSequenceTranslator;

    public HtmlCharcterEscapes() {

        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        // 아래 두개 json data 처리 중 에러나는 것들
        //        asciiEscapes['"'] = CharacterEscapes.ESCAPE_CUSTOM;
        //        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;


        /**
         * 여기 아래 코드는 잘 몰루
         */
        Map<CharSequence, CharSequence> charSequenceMap = new HashMap<>();
        charSequenceMap.put("(", "&#40;");
        charSequenceMap.put(")", "&#41;");

        Map<CharSequence, CharSequence> lookupMap = Collections.unmodifiableMap(charSequenceMap);

        charSequenceTranslator = new AggregateTranslator(new LookupTranslator(EntityArrays.BASIC_ESCAPE),
                new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
                new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE),
                new LookupTranslator(lookupMap));

    }

    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int i) {
        return new SerializedString(charSequenceTranslator.translate(Character.toString((char) i)));
    }
}
