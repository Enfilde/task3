package com.epam.ld.module2.testing.template;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.annotation.IncludeTest;
import com.epam.ld.module2.testing.util.FileIOManager;
import com.epam.ld.module2.testing.util.IOManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TemplateEngineTest {

    private static final String ADDRESS = "Vasya@gmail.com";
    private static final String MESSAGE_BODY = "Hello, #{name}" +
            "Today is your birthday, you are now #{age} years old." +
            "We have present to you, so give us your #{address} for sending this gift.";
    private static final String NOT_ENOUGH_PLACEHOLDERS ="Not enough placeholders";
    private static final String TEMP_SOURCE_FILE = "tempSourceFile.txt";
    private static final String TEMP_TARGET_FILE = "tempTargetFile.txt";

    @TempDir
    public File tempFolder;
    @Mock
    private IOManager mockIOManager;
    @Spy
    private IOManager spyIOManager;

    private Client client;
    private List<String> placeholders;

    @Tag("beforeEach")
    @BeforeEach
    void setup() {
        client = new Client();
        client.setAddress(ADDRESS);
        placeholders = Arrays.asList("#{name}", "#{age}", "#{address}");
    }

    @IncludeTest
    @DisplayName("Test successful message generation")
    void testSuccessfulMessageGeneration() {
        TemplateEngine templateEngine = new TemplateEngine(mockIOManager);
        Template template = new Template(MESSAGE_BODY, placeholders);

        when(mockIOManager.read()).thenReturn(Arrays.asList("#{name}", "#{age}", "#{address}"));

        assertEquals(MESSAGE_BODY,  templateEngine.generateMessage(template, client));

        verify(mockIOManager).read();
        verify(mockIOManager, never()).print(anyString());
    }


    @Tag("parametrizedTest")
    @ParameterizedTest
    @ValueSource(strings = {"#{name}", "#{surname}", "#{nickname}"})
    @DisplayName("Test successful single expression parametrized message generation")
    void testSingleExpressionParametrizedMessageGeneration(String placeholder) {
        TemplateEngine templateEngine = new TemplateEngine(mockIOManager);
        String body = String.format("Hello, %s", placeholder);
        List<String> placeholders = Collections.singletonList(placeholder);
        Template template = new Template(body, placeholders);

        when(mockIOManager.read()).thenReturn(Collections.singletonList(placeholder));

        String messageContent = templateEngine.generateMessage(template, client);

        assertEquals(body, messageContent);
        verify(mockIOManager).read();
        verify(mockIOManager, never()).print(anyString());
    }

    @Tag("parametrizedTest")
    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"#{name}", "#{surname}", "#{nickname}"})
    @DisplayName("Test successful single expression (values sources and empty) parametrized message generation")
    void testSingleValueAndEmptyExpressionParametrizedMessageGeneration(String placeholder) {
        TemplateEngine templateEngine = new TemplateEngine(mockIOManager);
        String messageBody = String.format("Hello, %s", placeholder);
        List<String> placeholders = Collections.singletonList(placeholder);
        Template template = new Template(messageBody, placeholders);

        when(mockIOManager.read()).thenReturn(Collections.singletonList(placeholder));

        assertEquals(messageBody, templateEngine.generateMessage(template, client));

        verify(mockIOManager).read();
        verify(mockIOManager, never()).print(anyString());
    }

    @Tag("dynamicTest")
    @TestFactory
    @DisplayName("Dynamic tests")
    Collection<DynamicTest> dynamicTests() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Test empty expression",
                        () -> assertEquals("Hello, ", generateMessage(""))),
                DynamicTest.dynamicTest("Single expression",
                        () -> assertEquals("Hello, #{Vasya}", generateMessage("#{Vasya}"))));
    }

    @IncludeTest
    @DisplayName("Test template engine with FileIOManager")
    void testTemplateEngineWithFileIOManager() throws IOException {

        File tempSourceFile = new File(tempFolder, TEMP_SOURCE_FILE);
        File tempTargetFile = new File(tempFolder, TEMP_TARGET_FILE);

        Files.write(tempSourceFile.toPath(), placeholders);

        Template template = new Template(MESSAGE_BODY, placeholders);
        IOManager ioManager = new FileIOManager(tempSourceFile, tempTargetFile);
        TemplateEngine templateEngine = new TemplateEngine(ioManager);

        String messageContent = templateEngine.generateMessage(template, client);
        assertEquals(MESSAGE_BODY, messageContent);
    }

    @IncludeTest
    @DisplayName("Test template engine with spy")
    void testTemplateEngineWithSpy() {
        Template template = new Template(MESSAGE_BODY, placeholders);
        TemplateEngine templateEngine = new TemplateEngine(spyIOManager);

        doReturn(placeholders).when(spyIOManager).read();

        assertEquals(MESSAGE_BODY, templateEngine.generateMessage(template, client));
    }


    @IncludeTest
    @DisplayName("should throw an exception when passed not enough expressions")
    void testThrowExceptionWhenPassedNotEnoughExpressions() {
        List<String> expressions = Arrays.asList("#{name}", "#{age}", "#{address}");
        Template template = new Template(MESSAGE_BODY, expressions);
        TemplateEngine templateEngine = new TemplateEngine(spyIOManager);

        doReturn(Arrays.asList("#{name}", "#{address}")).when(spyIOManager).read();
        Throwable exception = assertThrows(IllegalArgumentException.class,
                () -> templateEngine.generateMessage(template, client));

        assertEquals(exception.getMessage(), NOT_ENOUGH_PLACEHOLDERS);
    }

    private String generateMessage(String placeholder) {
        TemplateEngine templateEngine = new TemplateEngine(mockIOManager);
        String body = String.format("Hello, %s", placeholder);
        List<String> placeholders = Collections.singletonList(placeholder);
        Template template = new Template(body, placeholders);

        when(mockIOManager.read()).thenReturn(Collections.singletonList(placeholder));

        return templateEngine.generateMessage(template, client);
    }

    @IncludeTest
    @Disabled
    void testDisabled() {
        fail();
    }
}
