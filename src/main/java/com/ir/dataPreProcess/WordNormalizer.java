package com.ir.dataPreProcess;

/**
 * standardize the words by lowercase them
 */
public class WordNormalizer {
    public char[] lowercase(char[] chars){
        // noarmlize all the words into lowercase words
        for (int i = 0; i < chars.length; i++){
            chars[i] = Character.toLowerCase(chars[i]);
        }
        return chars;
    }

    /**
     * stem the input value
     * @param chars
     * @return
     */
    public String stem(char[] chars) {
        Stemmer stemmer = new Stemmer();
        stemmer.add(chars, chars.length);
        stemmer.stem();
        String string = new String(chars);
        return string;
    }
}