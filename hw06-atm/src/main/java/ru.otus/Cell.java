package ru.otus;

import java.util.NoSuchElementException;

public interface Cell {
    void removeBanknote() throws NoSuchElementException;
    void addBanknote(Banknote banknote) throws IllegalArgumentException;
    int calculateBalance();
    int size();
    Banknote getBanknote();
}
