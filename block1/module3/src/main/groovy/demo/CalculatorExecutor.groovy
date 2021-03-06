package demo

import container.IOperatorsPriorities
import container.OperatorsPriorities
import iterator.IStringIterator
import iterator.StringIterator
import service.Calculator

class CalculatorExecutor {

    static void main(String[] args) {
        
        println "change"

        enterYourExpression()
        String expression = readLine()
        checkExpressionOnExisting(expression)
        IOperatorsPriorities priorities = new OperatorsPriorities()
        fillOperators(priorities)
        IStringIterator iterator = new StringIterator(expression)

        Calculator calculator = new Calculator(priorities, iterator, { BigDecimal x, BigDecimal y, String operation ->
            BigDecimal result = 0
            switch (operation) {
                case "+":
                    result = x.add(y)
                    logMathOperation(x, operation, y, result)
                    break
                case "-":
                    result = x.subtract(y)
                    logMathOperation(x, operation, y, result)
                    break
                case "*":
                    result = x.multiply(y)
                    logMathOperation(x, operation, y, result)
                    break
                case "/":
                    result = x.divide(y)
                    logMathOperation(x, operation, y, result)
                    break
            }
            return result
        })

        try {
            BigDecimal result = calculator.getResult()
            println("Result = " + result)
        } catch (NullPointerException | NoSuchElementException ignored) {
            println("$expression is incorrect math expression!")
        }
    }

    private static void checkExpressionOnExisting(String expression) {
        if (expression.isEmpty()) {
            println("No math expression specified!")
        }
    }

    private static void enterYourExpression() {
        println "Enter your math expression."
    }

    private static void fillOperators(IOperatorsPriorities priorities) {
        priorities.addOperator("+", 1)
        priorities.addOperator("-", 1)
        priorities.addOperator("*", 2)
        priorities.addOperator("/", 2)
    }

    private static String readLine() {
        return System.in.newReader().readLine()
    }

    private static void logMathOperation(BigDecimal x, String operator, BigDecimal y, BigDecimal result) {
        println("$x $operator $y = $result")
    }
}
