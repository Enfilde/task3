package com.epam.ld.module2.testing.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileIOManager implements IOManager {

    private static final Logger LOGGER = Logger.getLogger(FileIOManager.class.getName());
    private final File sourceFile;
    private final File targetFile;

    public FileIOManager(File sourceFile, File targetFile) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
    }

    @Override
    public List<String> read() {
        List<String> inputs = new ArrayList<>();
        try (var scanner = new Scanner(sourceFile, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                inputs.add(scanner.nextLine());
            }
        } catch (IOException ex) {
            LOGGER.severe("File : " + sourceFile + " not found");
        }
        return inputs;
    }

    @Override
    public void print(String messageBody) {
        try (var fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(targetFile, true),
                StandardCharsets.UTF_8))) {
            Files.deleteIfExists(targetFile.toPath());
            fileWriter.write(messageBody);
        } catch (IOException exception) {
            LOGGER.severe(exception.getMessage());
        }
    }
}
