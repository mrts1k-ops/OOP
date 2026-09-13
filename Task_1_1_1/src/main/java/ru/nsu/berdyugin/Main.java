package ru.nsu.berdyugin;

import java.util.Arrays;

/**
 * Обьявляем начало функции.
 */

public class Main {

    /**
     * Восстанавливаем свойства кучи.
     *
     * @param a массив, представляющий кучу.
     * @param n текущий размер кучи (количество элементов, участвующих в сортировке).
     * @param i индекс корня поддерева, для которого восстанавливается свойство кучи.
     */

    public static void heapify(int[] a, int n, int i) { // Восстанавливаем свойства кучи
        int largest = i;
        int l = 2 * i + 1; // Находим индекс левого и правого потомка
        int r = 2 * i + 2;

        if (l < n && a[l] > a[largest]) { // Левый потомок
            largest = l;
        }

        if (r < n && a[r] > a[largest]) { // Правый потомок
            largest = r;
        }

        // Если нашли больший элемент - меняем элементы местами
        if (largest != i) {
            int temp = a[i];
            a[i] = a[largest];
            a[largest] = temp;

            heapify(a, n, largest); // Повторяем провеку
        }
    }

    /**
     * Сортирует массив целых чисел по возрастанию пирамидальной
     * сортировки.
     *
     * @param a массив, который необходимо отсортировать; изменяется на месте.
     */

    public static void heapsort(int[] a) { // Сортирует массив пирамидальной сортировки
        int n = a.length;

        // Строим максимальную кучу
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(a, n, i);
        }

        // Переносим максимальные элементы в конец массива
        for (int i = n - 1; i > 0; i--) {
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            //Восстанавливаем кучу
            heapify(a, i, 0);
        }
    }

    /**
     * Запускаем программу.
     *
     * @param args аргументы командной строки (не используются).
     */

    public static void main(String[] args) {
        int[] array = {5, 8, 2, 1, 9}; // Создаём исходный массив
        heapsort(array); // Сортируем массив

        System.out.println(Arrays.toString(array));
    }
}