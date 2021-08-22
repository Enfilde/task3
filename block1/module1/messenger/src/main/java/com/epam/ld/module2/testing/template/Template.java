package com.epam.ld.module2.testing.template;

import java.util.List;

/**
 * The type Template.
 */
public class Template {

    private String messageBody;
    private List<String> placeholders;

    public Template(String body, List<String> placeholders) {
        this.messageBody = body;
        this.placeholders = placeholders;
    }

    public List<String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<String> placeholders) {
        this.placeholders = placeholders;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
