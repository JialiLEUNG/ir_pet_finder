package com.ir.service;

import java.util.Collections;
import java.util.List;

/**

 * @param <T>
 */

public class Page<T> {

    public static final Page EMPTY = new Page(Collections.emptyList(), null, 0, 0);

    private final List<T> pet;
    private final String input;
    private final int from;
    private final int size;

    public Page(List<T> pet, String input, int from, int size) {
        this.pet = pet;
        this.input = input;
        this.from = from;
        this.size = size;
    }

    List<T> get() {
        return Collections.unmodifiableList(pet);
    }

    public String getInput() {
        return input;
    }

    public int getFrom() {
        return from;
    }

    public int getSize() {
        return size;
    }
}