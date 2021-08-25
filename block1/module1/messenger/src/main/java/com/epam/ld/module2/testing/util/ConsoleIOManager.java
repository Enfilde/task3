package com.epam.ld.module2.testing.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.out;

public class ConsoleIOManager implements IOManager {

    private static final String FILL_IN_PLACEHOLDERS = "Fill in placeholders." + System.lineSeparator() + "0 --> exit";
    private static final String EXIT = "0";

    @Override
    @SuppressWarnings("unchecked")
    public List<String> read() {
        List<String> inputs = new ArrayList<>();
        var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
        out.println(FILL_IN_PLACEHOLDERS);
        while (true) {
            String word = scanner.nextLine();
            if (word.trim().equalsIgnoreCase(EXIT)) {
                break;
            }
            inputs.add(word);
        }
        scanner.close();
        return inputs;
    }

    @Override
    public void print(String messageBody) {
        out.println(messageBody);
    }
}