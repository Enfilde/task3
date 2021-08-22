package com.epam.ld.module2.testing.template;

import com.epam.ld.module2.testing.Client;
import com.epam.ld.module2.testing.util.IOManager;

import java.util.List;

/**
 * The type Template engine.
 */
public class TemplateEngine {


    private IOManager ioManager;

    public TemplateEngine(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    public IOManager getExpressionTaker() {
        return ioManager;
    }

    public void setExpressionTaker(IOManager ioManager) {
        this.ioManager = ioManager;
    }

    /**
     * Generate message string.
     *
     * @param template the template
     * @param client   the client
     * @return the string
     */
    public String generateMessage(Template template, Client client) {
        String body = template.getMessageBody();
        List<String> inputs = ioManager.read();
        if (inputs.size() < template.getPlaceholders().size()) {
            throw new IllegalArgumentException("Not enough placeholders");
        }
        for (int i = 0; i < template.getPlaceholders().size(); i++) {
            body = body.replace(template.getPlaceholders().get(i), inputs.get(i));
        }
        return body;
    }
}
