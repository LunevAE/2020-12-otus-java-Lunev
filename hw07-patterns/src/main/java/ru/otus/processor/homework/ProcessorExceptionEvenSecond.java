package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.function.Supplier;

public class ProcessorExceptionEvenSecond implements Processor {
    private final Supplier<LocalTime> timeSupplier;

    public ProcessorExceptionEvenSecond(Supplier<LocalTime> timeSupplier) {
        this.timeSupplier = timeSupplier;
    }

    @Override
    public Message process(Message message) {
        if (timeSupplier.get().getSecond() % 2 == 0) {
            throw new DateTimeException("Even second exception");
        }

        return message;
    }
}

