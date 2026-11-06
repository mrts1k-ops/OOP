package ru.nsu.berdyugin;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;



/**
 * Хеш-таблица: хранит пары (ключ, значение) и находит, добавляет и удаляет
 * их за постоянное время. Коллизии (разные ключи в одной ячейке) решаются
 * цепочками.
 * @param <K> тип ключа.
 * @param <V> тип значения.
 */

public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    private static final int INITIAL_CAPACITY = 16; // Размер массива у пустой таблицы
    private static final double LOAD_FACTOR = 0.75; // Во сколько раз пар может быть меньше ячеек

    private Entry<K, V>[] buckets;
    private int size = 0;
    private int modCount = 0;


    /**
     * Создаёт пустую хеш-таблицу.
     */

    public HashTable() {
        buckets = createBuckets(INITIAL_CAPACITY);
    }


    /**
     * Одна пара в таблице.
     *
     * @param <K> тип ключа.
     * @param <V> тип значения.
     */

    public static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }


        /**
         * Возвращает ключ пары.
         *
         * @return ключ.
         */

        public K getKey() {
            return key;
        }


        /**
         * Возвращает значение пары.
         *
         * @return значение.
         */

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }


    /**
     * Добавляет пару но если такой ключ уже есть,
     * его значение заменяется на новое.
     *
     * @param key ключ.
     * @param value значение.
     */

    public void put(K key, V value) {
        checkKey(key);
        Entry<K, V> existing = findEntry(key);
        if (existing != null) {
            existing.value = value; // Состав пар не изменился, modCount не трогаем
            return;
        }

        if (size + 1 > buckets.length * LOAD_FACTOR) {
            resize(buckets.length * 2);
        }
        int index = indexFor(key, buckets.length);
        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        modCount++;
    }


    /**
     * Ищет значение по ключу.
     *
     * @param key ключ.
     * @return значение или null, если такого ключа нет.
     */

    public V get(K key) {
        checkKey(key);
        Entry<K, V> entry = findEntry(key);
        if (entry == null) {
            return null;
        }
        return entry.value;
    }


    /**
     * Проверяет, есть ли в таблице такой ключ.
     *
     * @param key ключ.
     * @return true, если ключ есть.
     */

    public boolean containsKey(K key) {
        checkKey(key);
        return findEntry(key) != null;
    }


    /**
     * Обновляет значение по ключу (в отличие от put, ключ уже должен быть в таблице).
     *
     * @param key ключ.
     * @param value новое значение.
     * @throws NoSuchElementException если такого ключа нет.
     */

    public void update(K key, V value) {
        checkKey(key);
        Entry<K, V> entry = findEntry(key);
        if (entry == null) {
            throw new NoSuchElementException("Ключа нет в таблице: " + key);
        }
        entry.value = value;
    }


    /**
     * Удаляет пару по ключу.
     *
     * @param key ключ.
     * @return true, если пара была и удалена, false, если такого ключа не было.
     */

    public boolean remove(K key) {
        checkKey(key);
        int index = indexFor(key, buckets.length);
        Entry<K, V> previous = null;
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next; // Удаляем первую пару цепочки
                } else {
                    previous.next = current.next; // Удаляем пару из середины или конца
                }
                size--;
                modCount++;
                shrinkIfNeeded();
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }


    /**
     * Возвращает количество пар в таблице.
     *
     * @return количество пар.
     */

    public int size() {
        return size;
    }


    /**
     * Проверяет, пуста ли таблица.
     *
     * @return true, если пар нет.
     */

    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Возвращает итератор по парам <ключ, значение> (если во время обхода
     * добавить или удалить пару, следующий вызов next бросит
     * ConcurrentModificationException).
     *
     * @return итератор по парам.
     */

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }


    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private final Entry<K, V>[] bucketsAtStart = buckets;
        private final int expectedModCount = modCount;
        private int bucketIndex = 0;
        private Entry<K, V> nextEntry;

        private HashTableIterator() {
            moveToNonEmptyBucket();
        }

        @Override
        public boolean hasNext() {
            return nextEntry != null;
        }

        @Override
        public Entry<K, V> next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException(
                        "Таблицу изменили во время обхода");
            }
            if (nextEntry == null) {
                throw new NoSuchElementException("Пары закончились");
            }

            Entry<K, V> result = nextEntry;
            nextEntry = result.next;
            if (nextEntry == null) {
                moveToNonEmptyBucket();
            }
            return result;
        }

        private void moveToNonEmptyBucket() {
            while (nextEntry == null && bucketIndex < bucketsAtStart.length) {
                nextEntry = bucketsAtStart[bucketIndex];
                bucketIndex++;
            }
        }
    }


    /**
     * Сравнивает таблицу с другим объектом.
     *
     * @param other объект для сравнения.
     * @return true, если таблицы равны.
     */

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof HashTable)) {
            return false;
        }
        HashTable<?, ?> otherTable = (HashTable<?, ?>) other;
        if (size != otherTable.size) {
            return false;
        }

        for (Entry<K, V> entry : this) {
            Entry<?, ?> found = otherTable.findEntry(entry.key);
            if (found == null || !Objects.equals(found.value, entry.value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Entry<K, V> entry : this) {
            hash += entry.key.hashCode() ^ Objects.hashCode(entry.value);
        }
        return hash;
    }


    /**
     * Печатает таблицу в виде "ключ = значение, ключ = значение".
     *
     * @return строка с парами.
     */

    @Override
    public String toString() {
        String result = "{";
        boolean first = true;
        for (Entry<K, V> entry : this) {
            if (!first) {
                result += ", ";
            }
            result += entry;
            first = false;
        }
        return result + "}";
    }


    // Сколько ячеек сейчас в массиве.
    int capacity() {
        return buckets.length;
    }

    // Создаёт пустой массив ячеек.
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] createBuckets(int capacity) {
        return (Entry<K, V>[]) new Entry<?, ?>[capacity];
    }

    // Проверяет, что ключ не null.
    private void checkKey(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("Ключ не может быть null");
        }
    }

    // Находит номер ячейки для ключа.
    private int indexFor(Object key, int capacity) {
        return Math.floorMod(key.hashCode(), capacity);
    }

    // Ищет пару с таким ключом в её цепочке (null если нет)
    private Entry<K, V> findEntry(Object key) {
        Entry<K, V> current = buckets[indexFor(key, buckets.length)];
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }


    private void shrinkIfNeeded() {
        if (buckets.length > INITIAL_CAPACITY && size < buckets.length / 4) {
            resize(buckets.length / 2);
        }
    }


    // Переносит все пары в новый массив другого размера
    private void resize(int newCapacity) {
        Entry<K, V>[] newBuckets = createBuckets(newCapacity);
        for (Entry<K, V> head : buckets) {
            Entry<K, V> current = head;
            while (current != null) {
                Entry<K, V> next = current.next;
                int index = indexFor(current.key, newCapacity);
                current.next = newBuckets[index];
                newBuckets[index] = current;
                current = next;
            }
        }
        buckets = newBuckets;
    }
}