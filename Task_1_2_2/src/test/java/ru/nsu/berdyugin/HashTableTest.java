package ru.nsu.berdyugin;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;


class HashTableTest {

    private static class CollidingKey {
        private final String name;

        CollidingKey(String name) {
            this.name = name;
        }

        @Override
        public int hashCode() {
            return 42;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof CollidingKey)) {
                return false;
            }
            return name.equals(((CollidingKey) other).name);
        }
    }


    // Создание пустой таблицы

    @Test
    void newTableIsEmpty() {
        HashTable<String, Integer> table = new HashTable<>();
        assertEquals(0, table.size());
        assertTrue(table.isEmpty());
    }

    @Test
    void getFromEmptyTableReturnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.get("a"));
    }


    // Добавление и поиск

    @Test
    void putThenGet() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertEquals(1, table.get("a"));
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
    }

    @Test
    void getMissingKeyReturnsNull() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertNull(table.get("b"));
    }

    @Test
    void putSameKeyOverwritesValue() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("a", 2);
        assertEquals(2, table.get("a"));
        assertEquals(1, table.size()); // Пара одна, а не две
    }

    @Test
    void severalKeysAreStoredSeparately() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);
        assertEquals(1, table.get("a"));
        assertEquals(2, table.get("b"));
        assertEquals(3, table.get("c"));
        assertEquals(3, table.size());
    }

    @Test
    void nullValueIsAllowed() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", null);
        assertTrue(table.containsKey("a"));
        assertNull(table.get("a"));
    }

    @Test
    void nullKeyIsRejected() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(IllegalArgumentException.class, () -> table.put(null, 1));
        assertThrows(IllegalArgumentException.class, () -> table.get(null));
        assertThrows(IllegalArgumentException.class, () -> table.containsKey(null));
        assertThrows(IllegalArgumentException.class, () -> table.remove(null));
        assertThrows(IllegalArgumentException.class, () -> table.update(null, 1));
    }


    // Проверка наличия ключа

    @Test
    void containsKeyFindsExistingKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertTrue(table.containsKey("a"));
    }

    @Test
    void containsKeyIsFalseForMissingKey() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertFalse(table.containsKey("b"));
    }


    // Обновление

    @Test
    void updateChangesValue() {
        HashTable<String, Number> table = new HashTable<>();
        table.put("one", 1);
        table.update("one", 1.0);
        assertEquals(Double.valueOf(1.0), table.get("one"));
        assertEquals(1, table.size());
    }

    @Test
    void updateMissingKeyThrows() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NoSuchElementException.class, () -> table.update("a", 1));
    }


    // Удаление

    @Test
    void removeDeletesPair() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertTrue(table.remove("a"));
        assertFalse(table.containsKey("a"));
        assertNull(table.get("a"));
        assertEquals(0, table.size());
    }

    @Test
    void removeMissingKeyReturnsFalse() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertFalse(table.remove("b"));
        assertEquals(1, table.size());
    }

    @Test
    void removeOneKeyKeepsOthers() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.remove("a");
        assertEquals(2, table.get("b"));
        assertEquals(1, table.size());
    }

    @Test
    void keyCanBeAddedAgainAfterRemoval() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.remove("a");
        table.put("a", 5);
        assertEquals(5, table.get("a"));
    }


    // Коллизии

    @Test
    void collidingKeysAreStoredSeparately() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.put(new CollidingKey("z"), 3);

        assertEquals(1, table.get(new CollidingKey("x")));
        assertEquals(2, table.get(new CollidingKey("y")));
        assertEquals(3, table.get(new CollidingKey("z")));
        assertEquals(3, table.size());
    }

    @Test
    void collidingKeyCanBeUpdated() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.update(new CollidingKey("x"), 10);

        assertEquals(10, table.get(new CollidingKey("x")));
        assertEquals(2, table.get(new CollidingKey("y")));
    }

    @Test
    void removingFirstOfCollidingKeysKeepsOthers() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.put(new CollidingKey("z"), 3);

        table.remove(new CollidingKey("z"));

        assertFalse(table.containsKey(new CollidingKey("z")));
        assertEquals(1, table.get(new CollidingKey("x")));
        assertEquals(2, table.get(new CollidingKey("y")));
    }

    @Test
    void removingMiddleOfCollidingKeysKeepsOthers() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.put(new CollidingKey("z"), 3);

        table.remove(new CollidingKey("y"));

        assertFalse(table.containsKey(new CollidingKey("y")));
        assertEquals(1, table.get(new CollidingKey("x")));
        assertEquals(3, table.get(new CollidingKey("z")));
    }

    @Test
    void removingLastOfCollidingKeysKeepsOthers() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.put(new CollidingKey("z"), 3);

        table.remove(new CollidingKey("x"));

        assertFalse(table.containsKey(new CollidingKey("x")));
        assertEquals(2, table.get(new CollidingKey("y")));
        assertEquals(3, table.get(new CollidingKey("z")));
    }


    // Ключи с необычными хеш-кодами

    @Test
    void negativeHashCodesWork() {
        HashTable<Integer, String> table = new HashTable<>();
        table.put(-1, "minus one");
        table.put(-100, "minus hundred");
        table.put(Integer.MIN_VALUE, "min");

        assertEquals("minus one", table.get(-1));
        assertEquals("minus hundred", table.get(-100));
        assertEquals("min", table.get(Integer.MIN_VALUE));
    }


    // Много элементов (массив внутри увеличивается, а потом уменьшается).

    @Test
    void manyElementsAreAllFound() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 1000; i++) {
            table.put(i, i * 2);
        }
        assertEquals(1000, table.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i * 2, table.get(i));
        }
    }

    @Test
    void remainingElementsSurviveManyRemovals() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 1000; i++) {
            table.put(i, i);
        }
        for (int i = 0; i < 1000; i += 2) {
            table.remove(i);
        }

        assertEquals(500, table.size());
        for (int i = 0; i < 1000; i++) {
            assertEquals(i % 2 == 1, table.containsKey(i));
        }
    }

    @Test
    void tableWorksAfterRemovingEverything() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 200; i++) {
            table.put(i, i);
        }
        for (int i = 0; i < 200; i++) {
            table.remove(i);
        }
        assertTrue(table.isEmpty());

        table.put(7, 70);
        assertEquals(70, table.get(7));
    }


    // Объём памяти (массив растёт, когда пар много, и уменьшается, когда мало).

    @Test
    void newTableHasInitialCapacity() {
        assertEquals(16, new HashTable<String, Integer>().capacity());
    }

    @Test
    void capacityGrowsWhenTableFillsUp() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 12; i++) {
            table.put(i, i);
        }
        assertEquals(16, table.capacity());

        table.put(12, 12);
        assertEquals(32, table.capacity());
    }

    @Test
    void capacityShrinksAfterManyRemovals() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 13; i++) {
            table.put(i, i);
        }
        assertEquals(32, table.capacity());

        for (int i = 0; i < 5; i++) {
            table.remove(i);
        }
        assertEquals(32, table.capacity());

        table.remove(5);
        assertEquals(16, table.capacity());
    }

    @Test
    void capacityIsNeverBelowInitial() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 100; i++) {
            table.put(i, i);
        }
        for (int i = 0; i < 100; i++) {
            table.remove(i);
        }
        assertEquals(16, table.capacity());
    }

    @Test
    void capacityStaysCloseToNumberOfPairs() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 1000; i++) {
            table.put(i, i);
        }
        assertTrue(table.capacity() * 0.75 >= table.size());

        for (int i = 0; i < 990; i++) {
            table.remove(i);
        }
        assertEquals(10, table.size());
        assertTrue(table.capacity() <= 4 * table.size());
    }


    // Обход по парам

    @Test
    void iterationOverEmptyTableHasNoPairs() {
        HashTable<String, Integer> table = new HashTable<>();
        assertFalse(table.iterator().hasNext());
    }

    @Test
    void iterationVisitsEveryPairOnce() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 50; i++) {
            table.put(i, i * 10);
        }

        int count = 0;
        int keySum = 0;
        for (HashTable.Entry<Integer, Integer> entry : table) {
            assertEquals(entry.getKey() * 10, entry.getValue());
            keySum += entry.getKey();
            count++;
        }
        assertEquals(50, count);
        assertEquals(1225, keySum); // 0 + 1 + ... + 49
    }

    @Test
    void iterationVisitsCollidingKeys() {
        HashTable<CollidingKey, Integer> table = new HashTable<>();
        table.put(new CollidingKey("x"), 1);
        table.put(new CollidingKey("y"), 2);
        table.put(new CollidingKey("z"), 3);

        int sum = 0;
        for (HashTable.Entry<CollidingKey, Integer> entry : table) {
            sum += entry.getValue();
        }
        assertEquals(6, sum);
    }

    @Test
    void nextAfterLastPairThrows() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        Iterator<HashTable.Entry<String, Integer>> iterator = table.iterator();
        iterator.next();
        assertThrows(NoSuchElementException.class, () -> iterator.next());
    }

    @Test
    void addingPairDuringIterationThrows() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTable.Entry<String, Integer> entry : table) {
                table.put("new", 4);
            }
        });
    }

    @Test
    void removingPairDuringIterationThrows() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        assertThrows(ConcurrentModificationException.class, () -> {
            for (HashTable.Entry<String, Integer> entry : table) {
                table.remove("a");
            }
        });
    }

    @Test
    void updatingValueDuringIterationIsAllowed() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);
        table.put("c", 3);

        for (HashTable.Entry<String, Integer> entry : table) {
            table.update(entry.getKey(), entry.getValue() * 10);
        }

        assertEquals(10, table.get("a"));
        assertEquals(20, table.get("b"));
        assertEquals(30, table.get("c"));
    }


    // Сравнение

    @Test
    void tablesWithSamePairsAreEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        first.put("a", 1);
        first.put("b", 2);

        HashTable<String, Integer> second = new HashTable<>();
        second.put("b", 2);
        second.put("a", 1);

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void emptyTablesAreEqual() {
        assertEquals(new HashTable<String, Integer>(), new HashTable<String, Integer>());
    }

    @Test
    void tableIsEqualToItself() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertEquals(table, table);
    }

    @Test
    void tablesWithDifferentValuesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        first.put("a", 1);
        HashTable<String, Integer> second = new HashTable<>();
        second.put("a", 2);

        assertFalse(first.equals(second));
    }

    @Test
    void tablesWithDifferentKeysAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        first.put("a", 1);
        HashTable<String, Integer> second = new HashTable<>();
        second.put("b", 1);

        assertFalse(first.equals(second));
    }

    @Test
    void tablesWithDifferentSizesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        first.put("a", 1);
        HashTable<String, Integer> second = new HashTable<>();
        second.put("a", 1);
        second.put("b", 2);

        assertFalse(first.equals(second));
        assertFalse(second.equals(first));
    }

    @Test
    void tablesAreEqualRegardlessOfInternalArraySize() {
        HashTable<Integer, Integer> grown = new HashTable<>();
        for (int i = 0; i < 100; i++) {
            grown.put(i, i);
        }
        for (int i = 3; i < 100; i++) {
            grown.remove(i);
        }

        HashTable<Integer, Integer> small = new HashTable<>();
        small.put(0, 0);
        small.put(1, 1);
        small.put(2, 2);

        assertEquals(grown, small);
    }

    @Test
    void tableIsNotEqualToNullOrOtherType() {
        HashTable<String, Integer> table = new HashTable<>();
        assertFalse(table.equals(null));
        assertFalse(table.equals("строка"));
    }


    // Вывод в строку

    @Test
    void emptyTableToString() {
        assertEquals("{}", new HashTable<String, Integer>().toString());
    }

    @Test
    void oneElementToString() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        assertEquals("{a=1}", table.toString());
    }

    @Test
    void severalElementsToString() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("a", 1);
        table.put("b", 2);

        String text = table.toString();
        assertTrue(text.startsWith("{"));
        assertTrue(text.endsWith("}"));
        assertTrue(text.contains("a=1"));
        assertTrue(text.contains("b=2"));
        assertTrue(text.contains(", "));
    }


    // Пример из условия задания

    @Test
    void exampleFromTask() {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        assertEquals(Double.valueOf(1.0), hashTable.get("one"));
    }
}