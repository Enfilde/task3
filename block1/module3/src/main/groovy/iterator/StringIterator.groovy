package iterator


class StringIterator implements IStringIterator {

    private static final char LEFT_BRACKET = '(' as char
    private static final char DASH = '-' as char
    private static final char POINT = '.' as char
    private static final String EMPTY = ""
    private static final String SPACE = " "

    private String stringExpression

    private int currentIndex

    /**
     * @param stringExpression String that contains a mathematical expression
     */
    StringIterator(String stringExpression) {
        println "text
        this.stringExpression = stringExpression?.replace(SPACE, EMPTY)
        currentIndex = 0
    }

    /**
     * {@inheritDoc}
     */
    @Override
    boolean hasNext() {
        return stringExpression != null && !stringExpression.isEmpty() && currentIndex < stringExpression.length()
    }

    /**
     * {@inheritDoc}
     *
     * @return number or operator
     */
    @Override
    String next() {
        StringBuilder operand = new StringBuilder()
        char element = stringExpression?.charAt(currentIndex)

        while (Character.isDigit(element) || element == POINT || (currentIndex == 0 && element == DASH) ||
                (currentIndex > 0 && stringExpression.charAt(currentIndex - 1) == LEFT_BRACKET as char && element == DASH)) {
            operand.append(element)
            currentIndex++

            if (currentIndex == stringExpression.length()) {
                return operand.toString()
            }

            element = stringExpression?.charAt(currentIndex)
        }

        if (operand.length() != 0) {
            return operand.toString()
        }

        currentIndex++
        return Character.toString(element)
    }
}
