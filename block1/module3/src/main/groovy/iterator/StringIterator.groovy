package iterator

class StringIterator implements IStringIterator {

    private static final char LEFT_BRACKET = '(' as char
    private static final char DASH = '-' as char
    private static final char POINT = '.' as char
    private static final String EMPTY = ""
    private static final String SPACE = " "

    private String string

    private int currentIndex

    StringIterator(String string) {
        this.string = string.replace(SPACE, EMPTY)
        currentIndex = 0
    }

    @Override
    boolean hasNext() {
        return string != null && !string.isEmpty() && currentIndex < string.length()
    }

    @Override
    String next() {
        StringBuilder operand = new StringBuilder();
        char element = string.charAt(currentIndex);

        while (Character.isDigit(element) || element == POINT || (currentIndex == 0 && element == DASH) ||
                (currentIndex > 0 && string.charAt(currentIndex - 1) == LEFT_BRACKET as char && element == DASH)) {
            operand.append(element);
            currentIndex++;

            if (currentIndex == string.length())
                return operand.toString();

            element = string.charAt(currentIndex);
        }

        if (operand.length() != 0)
            return operand.toString();

        currentIndex++;
        return Character.toString(element);
    }
}
