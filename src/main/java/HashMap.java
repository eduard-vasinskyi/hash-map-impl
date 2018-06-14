package main.java;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Test task<p>
 * Purpose: implementation of HashMap with open addressing.<p>
 * Double Hashing probing method is chosen for this implementation as it provides slightly higher efficiency.<p>
 * Duplicate keys are allowed but values are overwritten.<p>
 * Load factor is chosen to be in range of 0.3 to 0.5 as to prevent efficiency/space degradation.
 *
 * @author Eduard Vasinskyi
 * @version 1.0
 */

public class HashMap implements Map {
    private static final int DEFAULT_CAPACITY = 17;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final float MINIMUM_LOAD_FACTOR = 0.3f;
    private static final float MAXIMUM_LOAD_FACTOR = 0.7f;
    private static final float EXPANSION_RATE = 2f;

    private int capacity;
    private HashEntry[] table;
    private float loadFactor;
    private int size;

    /**
     * Basic constructor
     */
    public HashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructor which sets initial capacity
     *
     * @param initialCapacity desired initial capacity of a hash map
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructor which sets initial capacity and load factor
     *
     * @param initialCapacity desired initial capacity of a hash map
     * @param loadFactor      desired load factor of a hash map
     */
    public HashMap(int initialCapacity, float loadFactor) {
        // Initial capacity validation
        if (initialCapacity <= 0)
            throw new IllegalArgumentException("Capacity should be a positive number");
        else if (initialCapacity <= DEFAULT_CAPACITY)
            initialCapacity = DEFAULT_CAPACITY;
        else
            initialCapacity = nextPrime(initialCapacity);

        // Load factor validation
        if (loadFactor <= 0)
            throw new IllegalArgumentException("Load factor should be a positive number");
        else if (loadFactor > MAXIMUM_LOAD_FACTOR)
            loadFactor = MAXIMUM_LOAD_FACTOR;
        else if (loadFactor < MINIMUM_LOAD_FACTOR)
            loadFactor = MINIMUM_LOAD_FACTOR;

        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new HashEntry[capacity];
        this.size = 0;
    }

    /**
     * Inner class that stores key/value pairs
     */
    private static class HashEntry {
        int key;
        long value;

        HashEntry(int key, long value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HashEntry entry = (HashEntry) o;
            return key == entry.key &&
                    value == entry.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }

    /**
     * Puts value into a map using given key
     *
     * @param key   key of an item
     * @param value value of an item
     * @return boolean value that represents succesful operation
     */
    @Override
    public boolean put(int key, long value) {
        if (!ensureCapacity()) reHash(); // if too many elements, then do re-hashing

        HashEntry entry = new HashEntry(key, value);

        int hash = hash(key);   // initial hash of a key
        int step = secondHash(key); // secondary hash of a key
        boolean isDuplicate = false;

        while (table[hash] != null) {

            if (table[hash].key == key) {
                isDuplicate = true;
                break;
            }
            hash += step;
            hash %= capacity;
        }

        if (!isDuplicate) size++;
        table[hash] = entry;

        return true;
    }

    /**
     * Gets a value from a map by a given key
     *
     * @param key key that is used to find value in a map
     * @return value by a given key
     * @throws NoSuchElementException if there is no value associated with a given key
     */
    @Override
    public long get(int key) {
        int hash = hash(key);
        int step = secondHash(key);

        while (table[hash] != null) {
            if (table[hash].key == key)
                return table[hash].value;
            hash += step;
            hash %= capacity;
        }

        throw new NoSuchElementException("No item with key: " + key);
    }

    /**
     * Returns number of elements in a map
     *
     * @return size of a map
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Ensures that capacity is enough based on load factor and current size of a map
     *
     * @return boolean value that shows if capacity is enough
     */
    private boolean ensureCapacity() {
        return (size + 1) < (capacity * loadFactor);
    }

    /**
     * Re-hashes a map by creating a new array of hash entries that is at least twice as big as an old array.
     * Elements from an old array are then
     */
    private void reHash() {
        capacity = nextPrime((int) (EXPANSION_RATE * capacity));
        HashEntry[] oldTable = table;
        table = new HashEntry[capacity];
        size = 0;
        for (HashEntry entry : oldTable) {
            if (entry != null) {
                put(entry.key, entry.value);
            }
        }
    }

    /**
     * Basic hash function for an integer
     *
     * @param key key to be hashed
     * @return hash of a key
     */
    private int hash(int key) {
        int hash = key % capacity;
        return key < 0 ? -hash : hash;
    }

    /**
     * Secondary hash function which is used to calculate step size for probing
     *
     * @param key key to be hashed
     * @return secondary hash of a key
     */
    private int secondHash(int key) {
        return 5 - key % 5;
    }

    /**
     * Basic recursive function that returns next prime number greater than initial number
     *
     * @param number initial number
     * @return next prime number that is greater than initial number
     */
    private int nextPrime(int number) {
        number++;
        boolean isPrime = true;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                isPrime = false;
                break;
            }
        }
        if (isPrime)
            return number;
        else return nextPrime(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashMap hashMap = (HashMap) o;
        return capacity == hashMap.capacity &&
                Float.compare(hashMap.loadFactor, loadFactor) == 0 &&
                size == hashMap.size &&
                Arrays.equals(table, hashMap.table);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(table);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (HashEntry entry : table) {
            if (entry != null) {
                sb.append(entry.key);
                sb.append("=");
                sb.append(entry.value);
                sb.append(", ");
            }
        }
        sb.delete(sb.length()-2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}
