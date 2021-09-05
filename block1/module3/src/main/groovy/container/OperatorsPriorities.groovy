package container

class OperatorsPriorities implements IOperatorsPriorities {

    private Map<String, Integer> map;

    OperatorsPriorities() {
        this.map = new HashMap<>()
    }

    OperatorsPriorities(HashMap<String, Integer> map) {
        this.map = map
    }


    @Override
    int getPriority(String operator) {
        return map.get(operator)
    }

    @Override
    void addOperator(String operator, int priority) {
        map.put(operator,priority)
    }

    @Override
    void deleteOperator(String operator) {
        map.remove(operator)
    }
}
