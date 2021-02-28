import ru.otus.TestRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        TestRunner testRunner = new TestRunner("ru.otus.TestClass");
        testRunner.run();
    }
}
