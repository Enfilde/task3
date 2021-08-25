package com.epam.ld.module2.testing.demo;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.MailServer;
import com.epam.ld.module2.testing.Messenger;
import com.epam.ld.module2.testing.template.Template;
import com.epam.ld.module2.testing.template.TemplateEngine;
import com.epam.ld.module2.testing.util.ConsoleIOManager;
import com.epam.ld.module2.testing.util.FileIOManager;
import com.epam.ld.module2.testing.util.IOManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MessengerRunner {

    private static final String MESSAGE_BODY = "Hello, #{name}" + System.lineSeparator() +
            "Today is your birthday, you are now #{age} years old." + System.lineSeparator() +
            "We have present to you, so give us your #{address} for sending this gift.";
    private static final List<String> PLACEHOLDERS = Arrays.asList("#{name}", "#{age}", "#{address}");

    /**
     * @param args application arguments
     */
    public static void main(String[] args) {
        var messengerRunner = new MessengerRunner();
        messengerRunner.run(MESSAGE_BODY, PLACEHOLDERS, args);
    }

    /**
     * @param messageBody          message body
     * @param placeholders         message placeholders
     * @param applicationArguments application arguments
     */
    public void run(String messageBody, List<String> placeholders, String[] applicationArguments) {
        final var template = new Template(messageBody, placeholders);
        final var client = new Client();

        IOManager ioManager = applicationArguments.length == 0 ? new ConsoleIOManager() :
                new FileIOManager(new File(applicationArguments[0]),
                        new File(applicationArguments[1]));

        var templateEngine = new TemplateEngine(ioManager);
        final var mailServer = new MailServer(ioManager);
        var messenger = new Messenger(mailServer, templateEngine);

        client.setAddress("Vasya@gmail.com");
        messenger.sendMessage(client, template);
    }
}
