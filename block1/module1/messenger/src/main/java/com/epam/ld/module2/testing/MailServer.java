package com.epam.ld.module2.testing;

import com.epam.ld.module2.testing.util.IOManager;

/**
 * Mail server class.
 */
public class MailServer {

    private final IOManager ioManager;

    public MailServer(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    /**
     * Send notification.
     *
     * @param addresses      the addresses
     * @param messageContent the message content
     */
    public void send(String addresses, String messageContent) {
        ioManager.print(addresses + ": <<" + messageContent + ">>");
    }
}
