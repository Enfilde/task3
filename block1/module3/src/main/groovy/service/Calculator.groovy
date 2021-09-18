package service


import container.OperatorsPriorities
import iterator.StringIterator

/**
 * A class for evaluating a mathematical expression represented as a string.
 */
class Calculator {

    private static final String LEFT_BRACKET = "("
    private static final String RIGHT_BRACKET = ")"

    /**
     * An object that stores the priorities of operations.
     */
    private OperatorsPriorities priorities

    /**
     * An iterator containing a mathematical expression.
     */
    private StringIterator iterator

    /**
     * Stack for storing operands.
     */
    private Deque<BigDecimal> numbers

    /**
     * Stack for storing operators.
     */
    private Deque<String> operators

    private Closure<BigDecimal> baseCalculations

    /**
     * @param priorities object with operation priority
     * @param iterator iterator which contains a mathematical expression
     */
    Calculator(OperatorsPriorities priorities, StringIterator iterator, Closure<BigDecimal> baseCalculations) {
        this.priorities = priorities
        this.iterator = iterator
        numbers = new LinkedList<>()
        operators = new LinkedList<>()
        this.baseCalculations = baseCalculations
    }

    /**
     * Evaluates a mathematical expression, represented as a string.
     *
     * @return the result of evaluating a mathematical expression
     *
     * @throws NullPointerException if the string is not a valid mathematical expression
     * @throws EmptyStackException if the string is not a valid mathematical expression
     */
    BigDecimal getResult() throws NullPointerException, EmptyStackException {
        while (iterator.hasNext()) {
            String element = iterator.next()

            try {
                BigDecimal number = new BigDecimal(element)
                numbers.push(number)
            } catch (NumberFormatException ignored) {
                if (element == LEFT_BRACKET) {
                    operators.push(element)
                } else if (element == RIGHT_BRACKET) {
                    while (operators.peek() != LEFT_BRACKET) {
                        calculate()
                    }
                    operators.pop()
                } else {
                    if (operators.isEmpty()) {
                        operators.push(element)
                    } else {
                        Integer priority = priorities.getPriority(element)

                        while (!operators.isEmpty() && operators.peek() != LEFT_BRACKET && operators.peek() != RIGHT_BRACKET &&
                                priority <= priorities.getPriority(operators.peek())) {
                            calculate()
                        }

                        operators.push(element)
                    }
                }
            }
        }

        while (!operators.isEmpty()) {
            calculate()
        }

        return numbers.pop()
    }

    /**
     * Performs the operation associated with the first operator on the operator stack
     * on the first two operands on the operand stack.
     *
     * Used operands and an operator are removed from their respective stacks,
     * and the result of the operation is pushed onto the operand stack.
     *
     * @throws NoSuchElementException if the operator or operand stack is empty
     */
    private void calculate() throws NoSuchElementException {
        String operator = operators.pop()
        BigDecimal number2 = numbers.pop()
        BigDecimal number1 = numbers.pop()

        numbers.push(baseCalculations.call(number1, number2, operator))
    }
}