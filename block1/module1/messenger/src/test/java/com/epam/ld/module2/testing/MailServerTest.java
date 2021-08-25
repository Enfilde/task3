package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.annotation.ExcludeTest;
import com.epam.ld.module2.testing.annotation.IncludeTest;
import com.epam.ld.module2.testing.extension.CustomExtension;
import com.epam.ld.module2.testing.util.ConsoleIOManager;
import com.epam.ld.module2.testing.util.FileIOManager;
import com.epam.ld.module2.testing.util.IOManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(CustomExtension.class)
@ExtendWith(MockitoExtension.class)
class MailServerTest {

    private static final String ADDRESS = "Vasya@gmail.com";
    private static final String MESSAGE_BODY = "Hello, #{name}" +
            "Today is your birthday, you are now #{age} years old." +
            "We have present to you, so give us your #{address} for sending this gift.";

    @Mock
    private IOManager mockIOManager;

    @ExcludeTest
    void testExcludedMetaAnnotation() {
        fail();
    }

    @IncludeTest
    @DisplayName("mock read method in FileIOManager")
    void partialMockReadInFileIOManager() {
        FileIOManager fileIOManager = createMockBuilder(FileIOManager.class).addMockedMethod("read").createMock();
        expect(fileIOManager.read()).andReturn(Collections.singletonList("ilya"));
        replay(fileIOManager);
        List<String> response = fileIOManager.read();
        assertEquals("ilya", response.get(0));
    }

    @IncludeTest
    @DisplayName("Test successful message sending")
    void testSuccessfulMessageSending() throws UnsupportedEncodingException {
        final MailServer mailServer = new MailServer(new ConsoleIOManager());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream standardOut = System.out;
        System.setOut(new PrintStream(outputStream, false, StandardCharsets.UTF_8));
        mailServer.send(ADDRESS, MESSAGE_BODY);

        System.setOut(standardOut);

        String expected = ADDRESS + ": <<" + MESSAGE_BODY + ">>";

        assertEquals(expected, outputStream.toString(StandardCharsets.UTF_8).trim());
    }
}
