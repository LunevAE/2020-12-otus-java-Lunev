package ru.otus;

import ru.otus.calculator.Calculator;
import ru.otus.calculator.CalculatorImpl;
import ru.otus.proxy.Ioc;

public class Main {
    public static void main(String[] args) {
        final Calculator calculator = Ioc.createCalculator(new CalculatorImpl());

        calculator.calculation(1);
        calculator.calculation(2, 3);
        calculator.calculation(4, 5, "message");
    }
}

