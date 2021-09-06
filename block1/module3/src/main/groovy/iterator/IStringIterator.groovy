package iterator


interface IStringIterator {

    /**
     * Checks for the next item.
     *
     * @return true if the next element exists, otherwise false.
     */
    boolean hasNext()

    /**
     * Returns the next item.
     *
     * @return next element.
     */
    String next()
}