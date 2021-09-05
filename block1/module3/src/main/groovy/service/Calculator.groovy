package service

import container.IOperatorsPriorities
import iterator.IStringIterator

class Calculator {

    private static final String LEFT_BRACKET = "("
    private static final String RIGHT_BRACKET = ")"

    private IOperatorsPriorities priorities
    private IStringIterator iterator

    private Stack<Double> numbers
    private Stack<String> operators

    Calculator(IOperatorsPriorities priorities, IStringIterator iterator) {
        this.priorities = priorities
        this.iterator = iterator
        numbers = new Stack<>()
        operators = new Stack<>()
    }

    Double getResult() throws NullPointerException, EmptyStackException {
        while(iterator.hasNext()) {
            String element = iterator.next()

            try {
                double number = Double.parseDouble(element)
                numbers.push(number);
            } catch(NumberFormatException e) {
                if(element == LEFT_BRACKET) {
                    operators.push(element)
                } else if(element == RIGHT_BRACKET) {
                    while(operators.peek() != LEFT_BRACKET)
                        calculate()
                    operators.pop()
                } else {
                    if(operators.empty()) {
                        operators.push(element)
                    } else {
                        Integer priority = priorities.getPriority(element)

                        while(!operators.empty() && operators.peek() != LEFT_BRACKET && operators.peek() != RIGHT_BRACKET &&
                                priority <= priorities.getPriority(operators.peek())) {
                            calculate()
                        }

                        operators.push(element)
                    }
                }
            }
        }

        while(!operators.empty()) {
            calculate()
        }

        return numbers.pop()
    }


    private void calculate() throws EmptyStackException {
        String operator = operators.pop()
        Double n2 = numbers.pop()
        Double n1 = numbers.pop()
        Double result = null

        switch (operator) {
            case "+":
                result = n1 + n2
                break
            case "-":
                result = n1 - n2
                break
            case "*":
                result = n1 * n2
                break
            case "/":
                result = n1 / n2
        }

        numbers.push(result)
    }
}