package com.epam.ld.module2.testing.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
        List<String> inputs = new ArrayList<>();
        try (Scanner scanner = new Scanner(sourceFile)) {
            while (scanner.hasNextLine()) {
                inputs.add(scanner.nextLine());
            }
        } catch (FileNotFoundException ex) {
            logger.severe("File : " + sourceFile + " not found");
        }
        return inputs;
    }

    @Override
    public void print(String messageBody) {
        FileWriter fileWriter = null;
        try {
            Files.deleteIfExists(targetFile.toPath());
            fileWriter = new FileWriter(targetFile);
            fileWriter.write(messageBody);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
        } finally {
            if (Objects.nonNull(fileWriter)) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.severe(e.getMessage());
                }
            }
        }
    }
}
