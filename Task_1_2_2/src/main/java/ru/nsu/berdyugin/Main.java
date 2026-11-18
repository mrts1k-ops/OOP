package ru.nsu.berdyugin;


import java.util.ConcurrentModificationException;


/**
 * Демонстрация работы хеш-таблицы.
 */

public class Main {

    public static void main(String[] args) {
        HashTable<String, Number> hashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.update("one", 1.0);
        System.out.println(hashTable.get("one"));

        hashTable.put("two", 2);
        hashTable.put("three", 3);
        System.out.println(hashTable);
        System.out.println("Есть ключ two: " + hashTable.containsKey("two"));

        // Обход по парам
        for (HashTable.Entry<String, Number> entry : hashTable) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        } try {
            for (HashTable.Entry<String, Number> entry : hashTable) {
                hashTable.put("four", 4);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("Таблицу нельзя менять во время обхода: " + e.getMessage());
        }

        hashTable.remove("two");
        System.out.println("После удаления: " + hashTable);
    }
}