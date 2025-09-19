import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyDoublyLinkedList<T> implements Iterable<T> {
    private DoublyLinkedNode<T> head;
    private DoublyLinkedNode<T> tail;
    private int size;


    public MyDoublyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void add(T data) {
        addLast(data);
    }

    public void addLast(T data) {
        DoublyLinkedNode<T> newNode = new DoublyLinkedNode<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        size++;
    }

    public void addFirst(T data) {
        DoublyLinkedNode<T> newNode = new DoublyLinkedNode<>(data);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            head.setPrev(newNode);
            newNode.setNext(head);
            head = newNode;
        }
        size++;
    }

    public void removeFirst() {
        if (head == null) {
            return;
        }
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            head = head.getNext();
            head.setPrev(null);
        }
        size--;
    }

    public void removeLast() {
        if (tail == null) {
            return;
        }
        if (head == tail) {
            head = null;
            tail = null;
        } else {
            tail = tail.getPrev();
            tail.setNext(null);
        }
        size--;
    }

    public int getSize() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public T getFirst() {
        if (head == null) {
            return null;
        }
        return head.getData();
    }
    
    public T getLast() {
        if (tail == null) {
            return null;
        }
        return tail.getData();
    }
    
    public boolean contains(T data) {
        DoublyLinkedNode<T> current = head;
        while (current != null) {
            if (current.getData().equals(data)) {
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        DoublyLinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        return current.getData();
    }
    
    public void set(int index, T data) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        DoublyLinkedNode<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }
        current.setData(data);
    }
    
    /**
     * Removes the first occurrence of the specified element from the list
     * @param data The element to remove
     * @return true if the element was found and removed, false otherwise
     */
    public boolean remove(T data) {
        DoublyLinkedNode<T> current = head;
        
        while (current != null) {
            if (current.getData().equals(data)) {
                removeNode(current);
                return true;
            }
            current = current.getNext();
        }
        return false;
    }
    
    /**
     * Removes the element at the specified index
     * @param index The index of the element to remove
     * @return The data that was removed
     */
    public T removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        DoublyLinkedNode<T> nodeToRemove;
        
        if (index == 0) {
            nodeToRemove = head;
            removeFirst();
        } else if (index == size - 1) {
            nodeToRemove = tail;
            removeLast();
        } else {
            nodeToRemove = head;
            for (int i = 0; i < index; i++) {
                nodeToRemove = nodeToRemove.getNext();
            }
            removeNode(nodeToRemove);
        }
        
        return nodeToRemove.getData();
    }
    
    /**
     * Helper method to remove a specific node from the list
     * @param nodeToRemove The node to remove
     */
    private void removeNode(DoublyLinkedNode<T> nodeToRemove) {
        if (nodeToRemove == head && nodeToRemove == tail) {
            // Only one element
            head = null;
            tail = null;
        } else if (nodeToRemove == head) {
            // Removing first element
            head = head.getNext();
            head.setPrev(null);
        } else if (nodeToRemove == tail) {
            // Removing last element
            tail = tail.getPrev();
            tail.setNext(null);
        } else {
            // Removing middle element
            nodeToRemove.getPrev().setNext(nodeToRemove.getNext());
            nodeToRemove.getNext().setPrev(nodeToRemove.getPrev());
        }
        size--;
    }
    
    /**
     * Inserts an element at the specified index
     * @param index The index at which to insert the element
     * @param data The element to insert
     */
    public void insert(int index, T data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        if (index == 0) {
            addFirst(data);
        } else if (index == size) {
            addLast(data);
        } else {
            DoublyLinkedNode<T> newNode = new DoublyLinkedNode<>(data);
            DoublyLinkedNode<T> current = head;
            
            for (int i = 0; i < index; i++) {
                current = current.getNext();
            }
            
            newNode.setNext(current);
            newNode.setPrev(current.getPrev());
            current.getPrev().setNext(newNode);
            current.setPrev(newNode);
            size++;
        }
    }
    
    /**
     * Returns the index of the first occurrence of the specified element
     * @param data The element to search for
     * @return The index of the element, or -1 if not found
     */
    public int indexOf(T data) {
        DoublyLinkedNode<T> current = head;
        int index = 0;
        
        while (current != null) {
            if (current.getData().equals(data)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }
    
    /**
     * Returns the index of the last occurrence of the specified element
     * @param data The element to search for
     * @return The index of the element, or -1 if not found
     */
    public int lastIndexOf(T data) {
        DoublyLinkedNode<T> current = tail;
        int index = size - 1;
        
        while (current != null) {
            if (current.getData().equals(data)) {
                return index;
            }
            current = current.getPrev();
            index--;
        }
        return -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DoublyLinkedNode<T> current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(" <-> ");
            }
            current = current.getNext();
        }
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new MyDoublyLinkedListIterator();
    }
    private class MyDoublyLinkedListIterator implements Iterator<T> {
        private DoublyLinkedNode<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.getData();
            current = current.getNext();
            return data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation not supported");
        }
    }
}