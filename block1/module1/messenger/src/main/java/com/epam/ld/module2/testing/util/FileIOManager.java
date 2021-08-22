package com.epam.ld.module2.testing.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileIOManager implements IOManager {

    private static Logger logger = Logger.getLogger(FileIOManager.class.getName());
    private File sourceFile;
    private File targetFile;

    public FileIOManager(File sourceFile, File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    public List<String> read() {
        return Collections.emptyList();
    }

    @Override
    public void print(String messageBody) {

    }
}
