package fall2018.csc2017.GameCentre.data;

import java.io.Serializable;
import java.util.ArrayList;

public class StateStack<E> implements Serializable {

    /**
     * The stack for information storage.
     */
    private ArrayList<E> stack;

    /**
     * The capacity of the stack.
     */
    private int capacity;

    /**
     * Constructor for the StateStack class.
     *
     * @param capacity: The user's setting for capacity.
     */
    public StateStack(int capacity) {
        this.stack = new ArrayList<E>();
        this.capacity = capacity;
    }

    /**
     * Return the capacity of stack.
     *
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * set performUndo limit (capacity for this stack)
     *
     * @param capacity the capacity of the stateStack.
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Puts an item into the stack.
     *
     * @param item The item to be put into the stack.
     */
    public void put(E item) {
        if (stack.size() == capacity) {
            stack.remove(0);
            stack.add(item);
        } else {
            stack.add(item);
        }
    }

    /**
     * Gets the item on the top of the stack.
     *
     * @return the top item
     */
    public E get() {
        return stack.get(stack.size() - 1);
    }

    /**
     * Gets the item on the top of the stack and remove it from stack.
     *
     * @return the top item
     */
    public E pop() {
        return stack.remove(stack.size() - 1);
    }


    /**
     * Returns if the StateStack is Empty.
     *
     * @return if the stack is empty
     */
    public boolean isEmpty() {
        return stack.size() == 0;
    }

    /**
     * Returns the size of the StateStack.
     *
     * @return size of the stack
     */
    public int size() {
        return stack.size();
    }
}
