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

    /**
     * @param args application arguments
     */
    public static void main(String[] args) {
        String body = "Hello, #{name}" + System.lineSeparator() +
                "Today is your birthday, you are now #{age} years old." + System.lineSeparator() +
                "We have present to you, so give us your #{address} for sending this gift.";
        List<String> expessions = Arrays.asList("#{name}", "#{age}", "#{address}");
        Template template = new Template(body, expessions);
        IOManager ioManager = args.length == 0 ? new ConsoleIOManager() : new FileIOManager(new File(args[0]), new File(args[1]));
        TemplateEngine templateEngine = new TemplateEngine(ioManager);
        MailServer mailServer = new MailServer(ioManager);
        Messenger messenger = new Messenger(mailServer, templateEngine);
        Client client = new Client();
        client.setAddress("Vasya@gmail.com");
        messenger.sendMessage(client, template);

    }
}
