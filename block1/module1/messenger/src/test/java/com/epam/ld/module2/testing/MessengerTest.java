package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.annotation.IncludeTest;
import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessengerTest {

    private static final String MESSAGE_BODY = "Hello, #{name}" +
            "Today is your birthday, you are now #{age} years old." +
            "We have present to you, so give us your #{address} for sending this gift.";
    private static final String ADDRESS = "Vasya@gmail.com";

    @Mock
    private MailServer mockMailServer;
    @Mock
    private TemplateEngine mockTemplateEngine;

    @InjectMocks
    private Messenger messenger;

    private Client client;
    private List<String> placeholders;

    @BeforeEach
    void setup() {
        placeholders = Arrays.asList("#{name}", "#{age}", "#{address}");
        client = new Client();
        client.setAddress(ADDRESS);
    }

    @IncludeTest
    @DisplayName("should send message to a specific address")
    void testSendMessage() {
        final Template template = new Template(MESSAGE_BODY, placeholders);

        when(mockTemplateEngine.generateMessage(template, client)).thenReturn(MESSAGE_BODY);
        doNothing().when(mockMailServer).send(client.getAddress(), MESSAGE_BODY);

        messenger.sendMessage(client, template);

        verify(mockTemplateEngine).generateMessage(template, client);
        verify(mockMailServer).send(client.getAddress(), MESSAGE_BODY);
    }
}