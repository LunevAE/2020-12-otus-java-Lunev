package ru.otus.calculator;

import ru.otus.annotations.Log;

public class CalculatorImpl implements Calculator{
    @Log
    @Override
    public void calculation(int number) {
        System.out.println("Method: calculation(int number)");
    }

    @Log
    @Override
    public void calculation(int numberOne, int numberTwo) {
        System.out.println("Method: calculation(int numberOne, int numberTwo)");
    }

    @Log
    @Override
    public void calculation(int numberOne, int numberTwo, String message) {
        System.out.println("Method: calculation(int numberOne, int numberTwo, String message)");
    }
}
