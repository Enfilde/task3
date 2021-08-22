package com.epam.ld.module2.testing.util;

import java.util.Collections;
import java.util.List;

public class ConsoleIOManager implements IOManager {

    @Override
    public List<String> read() {
        return Collections.emptyList();
    }

    @Override
    public void print(String messageBody) {

    }
}
