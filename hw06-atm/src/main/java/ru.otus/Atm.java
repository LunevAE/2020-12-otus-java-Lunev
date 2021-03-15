package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Atm {
    private final Map<Banknote, Cell> balance;

    public Atm(final Map<Banknote, Cell> balance) {
        this.balance = balance;
    }

    //принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
    public void deposit(Banknote banknote) throws AtmException {
        Cell cell = balance.get(banknote);
        try {
            cell.addBanknote(banknote);
        } catch (IllegalArgumentException e) {
            throw new AtmException(e.getMessage());
        }
    }

    //выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
    public List<Banknote> withdraw(int value) throws AtmException {
        int balance = balanceEnquiry();

        if (value > balance) {
            throw new AtmException("Requested amount is greater than your balance");
        }

        List<Banknote> debitedBanknotes = new ArrayList<>();
        int requestedAmount = value;

        for (Map.Entry<Banknote, Cell> entry : this.balance.entrySet()) {
            int cash = entry.getKey().getValue();
            Cell cell = entry.getValue();

            if (requestedAmount < cash) {
                continue;
            }

            while (Math.abs(requestedAmount / cash) > 0 && requestedAmount > 0 && cell.size() > 0) {
                Banknote banknote = cell.getBanknote();

                debitedBanknotes.add(banknote);
                requestedAmount -= cash;
            }
        }

        if (requestedAmount > 0){
            throw new AtmException("ATM does not have enough money");
        }
        return debitedBanknotes;
    }

    //выдавать сумму остатка денежных средств
    public int balanceEnquiry() {
        return balance.values().stream()
                .mapToInt(Cell::calculateBalance)
                .sum();
    }
}
