package com.epam.ld.module2.testing.template;

import java.util.List;

/**
 * The type Template.
 */
public final class Template {

    private final String messageBody;
    private final List<String> placeholders;

    public Template(String body, List<String> placeholders) {
        this.messageBody = body;
        this.placeholders = placeholders;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
