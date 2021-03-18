package ru.otus;

import java.util.Stack;

public interface Atm {
    void deposit(Banknote banknote) throws AtmException;
    Stack<Banknote> withdraw(int value) throws AtmException;
    int balanceEnquiry();
}
