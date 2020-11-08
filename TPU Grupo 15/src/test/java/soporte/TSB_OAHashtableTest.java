package test.java.soporte;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import soporte.TSB_OAHashtable;

import static org.junit.jupiter.api.Assertions.*;

class TSB_OAHashtableTest {
    TSB_OAHashtable<Integer, String> ht1 = new TSB_OAHashtable<>(3, 0.2f);

    @BeforeEach
    public void setUp() {

        ht1.put(1, "Argentina");
        ht1.put(2, "Brasil");
        ht1.put(3, "Chile");
        ht1.put(4, "Mexico");
        ht1.put(5, "Uruguay");
        ht1.put(6, "Perú");
        ht1.put(7, "Colombia");
        ht1.put(8, "Ecuador");
        ht1.put(9, "Paraguay");
        ht1.put(10, "Bolivia");
        ht1.put(11, "Venezuela");
        ht1.put(12, "Estados Unidos");
        ht1.toString();
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void size() {
    assertEquals(12,ht1.size());
    }

    @org.junit.jupiter.api.Test
    void isEmpty() {
    assertFalse(ht1.isEmpty());

    }

    @org.junit.jupiter.api.Test
    void isEmpty2() {
        ht1.clear();
        assertTrue(ht1.isEmpty());

    }

    @org.junit.jupiter.api.Test
    void containsKey() {
        assertTrue(ht1.containsKey(1));
    }
    @org.junit.jupiter.api.Test
    void containsKey2() {
        assertFalse(ht1.containsKey(20));
    }

    @org.junit.jupiter.api.Test
    void containsValue() {
        assertTrue(ht1.containsValue("Argentina"));
    }
    @org.junit.jupiter.api.Test
    void containsValue2() {
        assertFalse(ht1.containsValue("SALAME"));
    }
    @org.junit.jupiter.api.Test
    void get() {
        assertSame((String)"Argentina", ht1.get(1));
    }
    @org.junit.jupiter.api.Test
    void get2() {
        assertNull(ht1.get(0));
    }

    @org.junit.jupiter.api.Test
    void put() {
    }

    @org.junit.jupiter.api.Test
    void remove() {
        assertEquals(12,ht1.size());
        String value = ht1.remove(2);
        assertEquals(11, ht1.size());
        assertSame("Brasil",value);
    }

    @org.junit.jupiter.api.Test
    void clear() {
    }

    @org.junit.jupiter.api.Test
    void values() {
    }

    @org.junit.jupiter.api.Test
    void testClone() {
    }

    @org.junit.jupiter.api.Test
    void testEquals() {
    }

    @org.junit.jupiter.api.Test
    void testHashCode() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
        ht1.toString();
    }

    @org.junit.jupiter.api.Test
    void contains() {
    }

    @org.junit.jupiter.api.Test
    void rehash() {
    }
}