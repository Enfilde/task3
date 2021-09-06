package container


interface IOperatorsPriorities {
    /**
     * Returns the priority of the operation.
     *
     * @param operator operator
     * @return the priority of the operation associated with the passed operator
     */
    Integer getPriority(String operator)

    /**
     * Adds an operator.
     *
     * @param operator operator
     * @param priority the priority of the operation associated with the passed operator
     */
    void addOperator(String operator, int priority)

    /**
     * Removes the operator.
     *
     * @param operator operator
     */
    void deleteOperator(String operator)
}