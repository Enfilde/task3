package container

interface IOperatorsPriorities {

    int getPriority(String operator)

    void  addOperator(String operator,int priority)

    void deleteOperator(String operator)
}