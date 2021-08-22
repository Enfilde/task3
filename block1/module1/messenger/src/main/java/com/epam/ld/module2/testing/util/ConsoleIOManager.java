package com.epam.ld.module2.testing.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleIOManager implements IOManager {

    @Override
    public List<String> read() {
        List<String> inputs = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter info. 0 -> exit");
        while (true) {
            String word = scanner.nextLine();
            if (word.trim().equalsIgnoreCase("0")) {
                break;
            }
            inputs.add(word);
        }
        scanner.close();
        return inputs;
    }

    @Override
    public void print(String messageBody) {
        System.out.println(messageBody);
    }
}