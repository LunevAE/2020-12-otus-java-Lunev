package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ProcessorExceptionEvenSecond;
import ru.otus.processor.homework.ProcessorSwapField11Field12;

import java.time.LocalTime;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяться во время выполнения.
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        var processors = List.of(new ProcessorSwapField11Field12(),
                new ProcessorExceptionEvenSecond(LocalTime::now));

        var complexProcessor = new ComplexProcessor(processors, Throwable::printStackTrace);
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        ObjectForMessage field13 = new ObjectForMessage();
        field13.setData(List.of("1", "2", "3"));

        var message = new Message.Builder(1L)
                .field1("field1")
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result: " + result);
        System.out.println("History: " + historyListener.getHistoryList());

        complexProcessor.removeListener(historyListener);
    }
}
