package ru.nsu.berdyugin;


import java.io.IOException;
import java.io.StringReader;
import java.util.List;


/**
 * Запуск поиска подстроки.
 */

public class Main {


    /**
     * Без аргументов показывает пример из задания, с двумя аргументами
     * ищет подстроку в файле.
     *
     * @param args имя файла и искомая строка.
     * @throws IOException если файл не удалось прочитать.
     */

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            // в "абракадабра" ищем "бра"
            List<Long> positions = SubstringSearch.find(new StringReader("абракадабра"), "бра");
            System.out.println(positions); // [1, 8]
        } else if (args.length == 2) {
            List<Long> positions = SubstringSearch.find(args[0], args[1]);
            System.out.println(positions);
        } else {
            System.out.println("Использование: <имя файла> <искомая строка>");
        }
    }
}