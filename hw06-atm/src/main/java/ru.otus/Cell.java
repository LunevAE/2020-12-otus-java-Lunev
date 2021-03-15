package ru.otus;

import java.util.List;
import java.util.NoSuchElementException;

public class Cell {
    private final List<Banknote> banknotes;
    private final int nominal;

    public Cell(List<Banknote> banknotes, int nominal) {
        this.banknotes = banknotes;
        this.nominal = nominal;
    }

    public Banknote getBanknote() throws NoSuchElementException {
        if (banknotes.isEmpty()) {
            throw new NoSuchElementException();
        }

        int idx = banknotes.size() - 1;
        Banknote banknote = banknotes.get(idx);
        banknotes.remove(idx);
        return banknote;
    }

    public void addBanknote(Banknote banknote) throws IllegalArgumentException {
        if (banknote.getValue() != nominal) {
            throw new IllegalArgumentException("Illegal nominal");
        }
        banknotes.add(banknote);
    }

    public int calculateBalance() {
        return banknotes.stream()
                .mapToInt(Banknote::getValue)
                .sum();
    }

    public int size() {
        return banknotes.size();
    }
}
