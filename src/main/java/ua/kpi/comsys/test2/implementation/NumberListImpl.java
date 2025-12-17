/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import ua.kpi.comsys.test2.NumberList;

/**
 * Custom implementation of INumberList interface.
 * Has to be implemented by each student independently.
 *
 * @author Khomenko Dariia IS-33, variant 23
 *
 */
public class NumberListImpl implements NumberList {

    private static class Node {
        Byte value;
        Node next;
        Node prev;

        Node(Byte value) {
            this.value = value;
        }
    }

    private Node head;
    private int size;
    private int scale = 10;

    /**
     * Default constructor. Returns empty <tt>NumberListImpl</tt>
     */
    public NumberListImpl() {
        head = null;
        size = 0;
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * from file, defined in string format.
     *
     * @param file - file where number is stored.
     */
    public NumberListImpl(File file) {
        this();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) {
                for (char c : line.trim().toCharArray()) {
                    add((byte) (c - '0'));
                }
            }
        } catch (IOException e) {
            // ignore, create empty list
        }
    }


    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * in string notation.
     *
     * @param value - number in string notation.
     */
    public NumberListImpl(String value) {
        this();
        if (!value.matches("\\d+"))
            return;

        for (char c : value.toCharArray()) {
            add((byte) (c - '0'));
        }
    }


    /**
     * Saves the number, stored in the list, into specified file
     * in <b>decimal</b> scale of notation.
     *
     * @param file - file where number has to be stored.
     */
    public void saveList(File file) {
    }


    /**
     * Returns student's record book number, which has 4 decimal digits.
     *
     * @return student's record book number.
     */
    public static int getRecordBookNumber() {
        return 23;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the same number
     * in other scale of notation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @return <tt>NumberListImpl</tt> in other scale of notation.
     */
    public NumberListImpl changeScale() {
        
        String dec = this.toDecimalString();

        java.math.BigInteger value = new java.math.BigInteger(dec, 10);
        java.math.BigInteger sixteen = java.math.BigInteger.valueOf(16);

        NumberListImpl result = new NumberListImpl();
        result.scale = 16;

        if (value.equals(java.math.BigInteger.ZERO)) {
            result.add((byte) 0);
            return result;
        }

        while (value.compareTo(java.math.BigInteger.ZERO) > 0) {
            java.math.BigInteger[] divRem = value.divideAndRemainder(sixteen);
            result.addFirst(divRem[1].byteValue());
            value = divRem[0];
        }

        return result;
    }


    /**
     * Returns new <tt>NumberListImpl</tt> which represents the result of
     * additional operation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @param arg - second argument of additional operation
     *
     * @return result of additional operation.
     */
    public NumberListImpl additionalOperation(NumberList arg) {

        NumberListImpl other = (NumberListImpl) arg;

        java.math.BigInteger a = new java.math.BigInteger(this.toDecimalString(), 10);

        java.math.BigInteger b = new java.math.BigInteger(other.toDecimalString(), 10);

        java.math.BigInteger res = a.multiply(b);

        return new NumberListImpl(res.toString());
    }


    /**
     * Returns string representation of number, stored in the list
     * in <b>decimal</b> scale of notation.
     *
     * @return string representation in <b>decimal</b> scale.
     */
    public String toDecimalString() {
        if (isEmpty())
            return "";

        java.math.BigInteger value = java.math.BigInteger.ZERO;
        java.math.BigInteger base = java.math.BigInteger.valueOf(scale);

        for (Byte b : this) {
            value = value.multiply(base)
                    .add(java.math.BigInteger.valueOf(b));
        }

        return value.toString();
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Byte b : this) {
            sb.append(Character.toUpperCase(
                    Character.forDigit(b, scale)));
        }
        return sb.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NumberListImpl))
            return false;
        NumberListImpl other = (NumberListImpl) o;

        if (size != other.size)
            return false;

        Node a = head;
        Node b = other.head;

        for (int i = 0; i < size; i++) {
            if (a.value != b.value)
                return false;
            a = a.next;
            b = b.next;
        }
        return true;
    }


    @Override
    public int size() {
        return size;
    }


    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Byte))
            return false;
        for (Byte b : this) {
            if (b.equals(o))
                return true;
        }
        return false;
    }


    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private int count = 0;
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null && count < size;
            }

            @Override
            public Byte next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                byte val = current.value;
                current = current.next;
                count++;
                return val;
            }
        };
    }


    public void addFirst(Byte e) {
        if (head == null) {
            add(e);
            return;
        }

        Node n = new Node(e);
        Node tail = head.prev;

        n.next = head;
        n.prev = tail;

        tail.next = n;
        head.prev = n;

        head = n;
        size++;
    }


    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        int i = 0;
        for (Byte b : this) {
            arr[i++] = b;
        }
        return arr;
    }


    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }


    @Override
    public boolean add(Byte e) {
        if (e == null)
            return false;

        Node n = new Node(e);

        if (head == null) {
            head = n;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;
            tail.next = n;
            n.prev = tail;
            n.next = head;
            head.prev = n;
        }

        size++;
        return true;
    }


    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx == -1)
            return false;
        remove(idx);
        return true;
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }


    @Override
    public void clear() {
        head = null;
        size = 0;

    }


    @Override
    public Byte get(int index) {
        checkIndex(index);
        return node(index).value;
    }


    @Override
    public Byte set(int index, Byte element) {
        Node n = node(index);
        byte old = n.value;
        n.value = element;
        return old;
    }


    @Override
    public void add(int index, Byte element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == size) {
            add(element);
            return;
        }

        Node cur = node(index);
        Node n = new Node(element);

        n.prev = cur.prev;
        n.next = cur;

        cur.prev.next = n;
        cur.prev = n;

        if (index == 0) {
            head = n;
        }

        size++;

    }


    @Override
    public Byte remove(int index) {
        checkIndex(index);
        Node n = node(index);
        if (size == 1) {
            head = null;
        } else {
            n.prev.next = n.next;
            n.next.prev = n.prev;
            if (n == head)
                head = n.next;
        }
        size--;
        return n.value;
    }

    private Node node(int index) {
        Node cur = head;
        for (int i = 0; i < index; i++)
            cur = cur.next;
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException();

    }


    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Byte b : this) {
            if (b.equals(o))
                return i;
            i++;
        }
        return -1;
    }


    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o);
    }


    @Override
    public ListIterator<Byte> listIterator() {
        throw new UnsupportedOperationException();
    }


    @Override
    public ListIterator<Byte> listIterator(int index) {
        throw new UnsupportedOperationException();
    }


    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }


    @Override
    public boolean swap(int index1, int index2) {
        return false;
    }


    @Override
    public void sortAscending() {
    }


    @Override
    public void sortDescending() {
    }


    @Override
    public void shiftLeft() {
    }


    @Override
    public void shiftRight() {
    }
}
