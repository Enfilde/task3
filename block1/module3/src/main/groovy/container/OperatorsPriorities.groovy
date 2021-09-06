package container


class OperatorsPriorities implements IOperatorsPriorities {

    private Map<String, Integer> map

    OperatorsPriorities(HashMap<String, Integer> map) {
        this.map = map
    }

    OperatorsPriorities() {
        map = new HashMap<>()
    }

    /**
     * {@inheritDoc}
     */
    @Override
    Integer getPriority(String operator) {
        return map.get(operator)
    }

    /**
    * {@inheritDoc}
    */
    @Override
    void addOperator(String operator, int priority) {
        map.put(operator, priority)
    }

    /**
    * {@inheritDoc}
    */
    @Override
    void deleteOperator(String operator) {
        map.remove(operator)
    }
}