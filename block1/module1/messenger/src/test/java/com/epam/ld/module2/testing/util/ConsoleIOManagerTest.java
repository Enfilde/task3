package com.epam.ld.module2.testing.util;


import com.epam.ld.module2.testing.annotation.IncludeTest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConsoleIOManagerTest {

    private ConsoleIOManager consoleIOManager;

    @IncludeTest
    void read() {
        String expressionInputs = "#{name}" + System.lineSeparator()
                + "#{age}" + System.lineSeparator()
                + "#{address}" + System.lineSeparator()
                + "0";
        consoleIOManager = new ConsoleIOManager();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(expressionInputs.getBytes(StandardCharsets.UTF_8));
        InputStream systemIn = System.in;
        System.setIn(byteArrayInputStream);
        List<String> inputs = consoleIOManager.read();
        String[] expressions = expressionInputs.split(System.lineSeparator());
        for (int i = 0; i < inputs.size(); i++) {
            assertEquals(expressions[i], inputs.get(i));
        }
        System.setIn(systemIn);
    }

    @IncludeTest
    void print() {
        PrintStream systemOut = System.out;
        String placeholder = "#{test}";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(byteArrayOutputStream, false, StandardCharsets.UTF_8));
        consoleIOManager = new ConsoleIOManager();
        consoleIOManager.print(placeholder);
        System.setOut(systemOut);
        assertEquals(placeholder, byteArrayOutputStream.toString(StandardCharsets.UTF_8).trim());
    }
}