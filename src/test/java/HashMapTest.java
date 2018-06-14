package test.java;

import main.java.HashMap;
import main.java.Map;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class HashMapTest {
    private Map map;
    private int key;
    private long value;
    private static Random generator = new Random();
    private static final int LOAD_NUMBER = 10_000;

    @Before
    public void setUp() {
        map = new HashMap();
        key = generator.nextInt(Integer.MAX_VALUE);
        value = generator.nextLong();
    }

    @Test
    public void testBasicConstructor() {
        map = new HashMap();

        assertEquals(0, map.size());
    }

    @Test
    public void testConstructorWithPositiveInitialCapacity() {
        map = new HashMap(100);

        assertEquals(0, map.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConsructorWithNegativeInitialCapacity() {
        map = new HashMap(-100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroInitialCapacity() {
        map = new HashMap(0);
    }

    @Test
    public void testConstructorWithPositiveLoadFactor() {
        map = new HashMap(100, 0.6f);
        assertEquals(0, map.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeLoadFactor() {
        map = new HashMap(100, -0.6f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroLoadFactor() {
        map = new HashMap(100, 0.0f);
    }

    @Test(expected = NoSuchElementException.class)
    public void testForNotPresentKe() {
        map.put(key, value);
        map.get(key + 10);
    }

    @Test
    public void testPutAndGet() {
        map.put(key, value);
        long obtainedValue = map.get(key);

        assertEquals(value, obtainedValue);
        assertEquals(1, map.size());
    }

    @Test
    public void testForDuplicates() {
        long secondValue = generator.nextLong();

        map.put(key, value);
        map.put(key, secondValue);
        assertEquals(1, map.size());

        long obtainedValue = map.get(key);
        assertEquals(secondValue, obtainedValue);
    }

    @Test
    public void testForNegativeKeyValues() {
        int negativeKey = -key;

        map.put(negativeKey, -value);
        map.put(key, value);

        assertEquals(2, map.size());
        assertEquals(-value, map.get(negativeKey));
        assertEquals(value, map.get(key));
    }

    @Test
    public void testForOverflow() {
        int keyOverFlow = (int) Long.MAX_VALUE;
        map.put(keyOverFlow, value);
        long obtainedValue = map.get(keyOverFlow);

        assertEquals(value, obtainedValue);
    }

    @Test
    public void testSizeMethod() {
        for (int i = 0; i < LOAD_NUMBER; i++) {
            value = generator.nextLong();
            map.put(i, value);
        }

        assertEquals(LOAD_NUMBER, map.size());
    }

    @Test
    public void testEquals() {
        Map map2 = new HashMap();
        Map map3 = new HashMap(100);

        map.put(key, value);
        map2.put(key,value);
        map3.put(key,value);

        assertEquals(map, map2);
        assertNotEquals(map, map3);
    }

    @Test
    public void testHashCode() {
        map.put(key, value);

        Map map2 = new HashMap();
        map2.put(key, value);

        assertEquals(map.hashCode(), map2.hashCode());
    }

    @Test
    public void testToString() {
        map.put(88743, 687239L);
        map.put(231212, 80124124123L);
        map.put(-1412323, 23123412L);

        assertEquals("{88743=687239, 231212=80124124123, -1412323=23123412}", map.toString());
    }
}