package ru.otus;

import com.google.common.collect.Lists;
import java.util.List;

public class HelloOtus {

    public static void main(String[] args) {
        final String str = "Hello Otus";
        final List<Character> strAsCharacters = Lists.charactersOf(str);
        strAsCharacters.forEach(System.out::println);
    }
}