package demo

import container.IOperatorsPriorities
import container.OperatorsPriorities
import iterator.IStringIterator
import iterator.StringIterator
import service.Calculator


class CalculatorExecutor {

    static void main(String[] args) {

        IOperatorsPriorities priorities = new OperatorsPriorities();
        fillOperators(priorities)

        enterYourExpression()
        IStringIterator iterator = new StringIterator(readLine())
        Calculator calculator = new Calculator(priorities, iterator)

        double result = calculator.getResult()

        println "Result of calculations is ${result}."
    }

    private static void enterYourExpression(){
        println "Enter your math expression."
    }

    private static void fillOperators(IOperatorsPriorities priorities) {
        priorities.addOperator("+", 1);
        priorities.addOperator("-", 1);
        priorities.addOperator("*", 2);
        priorities.addOperator("/", 2);
    }

    private static String readLine() {
        return System.in.newReader().readLine()
    }
}
