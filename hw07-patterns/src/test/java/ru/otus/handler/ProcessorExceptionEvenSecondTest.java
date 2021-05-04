package ru.otus.handler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorExceptionEvenSecond;
import ru.otus.processor.homework.ProcessorSwapField11Field12;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ProcessorExceptionEvenSecondTest {
    @Test
    @DisplayName("Exception on even second")
    void exceptionOnEvenSecondTest() {
        var processors = List.of(new ProcessorSwapField11Field12(),
                new ProcessorExceptionEvenSecond(() -> LocalTime.of(10, 10, 4)));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            if (ex.getClass() == DateTimeException.class && ex.getMessage().equals("Even second exception") ) {
                throw new TestException("Even second exception");
            }
        });

        var message = new Message.Builder(1L)
                .build();

        assertThatExceptionOfType(TestException.class)
                .isThrownBy(() -> complexProcessor.handle(message));
    }

    @Test
    @DisplayName("No exception on odd second")
    void noExceptionOnOddSecondTest() {
        var processors =
                List.of(new ProcessorSwapField11Field12(),
                        new ProcessorExceptionEvenSecond(() -> LocalTime.of(10, 10, 5)));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            if (ex.getClass() == DateTimeException.class && ex.getMessage().equals("Even second exception")) {
                throw new TestException("Even second exception");
            }
        });

        var message = new Message.Builder(1L)
                .build();

        complexProcessor.handle(message);
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}
