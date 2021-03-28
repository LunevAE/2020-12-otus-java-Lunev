package ru.otus;

import java.util.NoSuchElementException;
import java.util.Stack;

public class CellImpl implements Cell{
    private final Stack<Banknote> banknotes;

    public CellImpl(Stack<Banknote> banknotes) {
        this.banknotes = banknotes;
    }

    @Override
    public void removeBanknote() throws NoSuchElementException {
        if (banknotes.isEmpty()) {
            throw new NoSuchElementException();
        }

        banknotes.pop();
    }

    @Override
    public void addBanknote(Banknote banknote) throws IllegalArgumentException {
        banknotes.push(banknote);
    }

    @Override
    public int calculateBalance() {
        return banknotes.stream()
                .mapToInt(Banknote::getValue)
                .sum();
    }

    @Override
    public int size() {
        return banknotes.size();
    }


    @Override
    public Banknote getBanknote () {
        return banknotes.peek();
    }
}
