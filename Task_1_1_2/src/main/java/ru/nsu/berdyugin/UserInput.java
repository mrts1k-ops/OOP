package ru.nsu.berdyugin;

import java.util.Scanner;


/**
 * Чтение ответов пользователя на консоли.
 */

public class UserInput {
    private final Scanner scanner; //Читает текст из консоли.

    /**
     * Создаёт объект для чтения ввода.
     *
     * @param scanner сканер, из которого читаем.
     */

    public UserInput(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Задаёт вопрос и ждёт ответ "1" (да) или "0" (нет).
     *
     * @param question текст вопроса.
     * @return true, если пользователь ввёл 1, и false, если 0.
     */

    public boolean askYesNo(String question) {
        while (true) {
            System.out.println(question);
            String line = scanner.nextLine().trim();

            if (line.equals("1")) {
                return true;
            }
            if (line.equals("0")) {
                return false;
            }
            System.out.println("Нужно ввести 1 или 0."); //Неверный ввод, спрашиваем снова
        }
    }
}
