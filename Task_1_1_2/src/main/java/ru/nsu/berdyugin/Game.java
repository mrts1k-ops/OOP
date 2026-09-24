package ru.nsu.berdyugin;

import java.util.Scanner;


/**
 * Игра, раунды, раздача карт, ходы, определение победителя и счёт.
 */

public class Game {
    private final UserInput input = new UserInput(new Scanner(System.in)); //Вводные данные и консоли.
    private final Deck deck; // Колода
    private final Player player = new Player(input); //Игрок
    private final Dealer dealer = new Dealer(); // Дилер
    private int playerWins = 0; // Количество раундов, что выиграл игрок
    private int dealerWins = 0; // Количество раундов, что выиграл дилер
    private int round = 0; // Номер раунда


    /**
     * Создаёт игру.
     *
     * @param decksCount количество колод по 52 карты.
     */

    public Game(int decksCount) {
        this.deck = new Deck(decksCount);
    }


    /**
     * Запускает игру: раунды идут, пока пользователь не захочет выйти.
     */

    public void start() {
        System.out.println("Доброе пожаловать!");

        boolean playAgain = true;
        while (playAgain) {
            playRound();
            playAgain = input.askYesNo("\nВведите \u201C1\u201D, чтобы сыграть ещё раунд, и \u201C0\u201D, чтобы выйти...");
        }
    }


    /**
     * Проводит один раунд.
     */

    private void playRound() {
        round++;
        System.out.println("\nРаунд " + round);

        dealCards();

        // Если у кого-то блэккджек
        boolean playerBlackJack = player.getHand().isBlackJack();
        boolean dealerBlackJack = dealer.getHand().isBlackJack();
        if (playerBlackJack || dealerBlackJack) {
            openDealerCard();
            if (playerBlackJack && dealerBlackJack) {
                System.out.println("\nУ обоих блэкджек!");
                drawRound();
            } else if (playerBlackJack) {
                System.out.println("\nУ вас блэкджек!");
                playerWinsRound();
            } else {
                System.out.println("\nУ дилера блэкджек!");
                dealerWinsRound();
            }
            return;
        }

        //Ход игрока (если перебор, он сразу проигрывает)
        boolean noBust = playerTurn();
        if (!noBust) {
            dealerWinsRound();
            return;
        }

        //Ход дилера (если перебор, он сразу выигрывает)
        dealerTurn();
        if (dealer.getHand().isBust()) {
            playerWinsRound();
            return;
        }

        // Сравниваем суммы очков.
        int playerScore = player.getHand().getScore();
        int dealerScore = dealer.getHand().getScore();
        if (playerScore > dealerScore) {
            playerWinsRound();
        } else if (playerScore < dealerScore){
            dealerWinsRound();
        } else{
            drawRound();
        }
    }


    /**
     * Раздаёт по две карты игроку и дилеру.
     */

    private void dealCards() {
        player.getHand().clear(); // Убираем карты прошлого раунда.
        dealer.getHand().clear();

        for (int i = 0; i < 2; i++) {
            player.getHand().add(deck.draw());
            dealer.getHand().add(deck.draw());
        }

        System.out.println("Дилер раздал карты");
        printHands(true);
    }


    /**
     * Ход игрока: берёт карты, пока не остановится или не будет перебора.
     *
     * @return false, если у игрока перебор, иначе true.
     */

    private boolean playerTurn() {
        System.out.println("\nВаш ход\n-------");

        while (player.wantsToTake()) {
            Card card = deck.draw();
            player.getHand().add(card);
            System.out.println("Вы открыли карту " + card);
            printHands(true);

            if (player.getHand().isBust()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Ход дилера: открывает закрытую карту и берёт карты, пока у него меньше 17.
     */

    private void dealerTurn() {
        System.out.println("\nХод дилера\n-------");
        openDealerCard();

        while (dealer.wantsToTake()) {
            Card card = deck.draw();
            dealer.getHand().add(card);
            System.out.println("\nДилер открывает карту " + card);
            printHands(false);
        }
    }


    /**
     * Открывает закрытую карту дилера и показает все карты.
     */

    private void openDealerCard() {
        System.out.println("Дилер открывает закрытую карту " + dealer.getHand().getCard(1));
        printHands(false);
    }

    /**
     * Печатает карты игрока и дилера.
     *
     * @param hideDealerCard true, если вторая карта дилера закрыта.
     */

    private void printHands(boolean hideDealerCard) {
        System.out.println("    Ваши карты: " + player.getHand().describe());

        if (hideDealerCard) {
            System.out.println("     Карты дилера: " + dealer.getHand().describeHidden());
        } else {
            System.out.println("     Карты дилера: " + dealer.getHand().describe());
        }
    }


    /**
     * Игрок выиграл раунд.
     */

    private void playerWinsRound() {
        playerWins++;
        System.out.println("\nВы выиграли раунд! " + scoreText());
    }


    /**
     * Дилер выиграл раунд.
     */

    private void dealerWinsRound() {
        dealerWins++;
        System.out.println("\nВы выиграли раунд! " + scoreText());
    }

    /**
     * Ничья.
     */

    private void drawRound() {
        System.out.println("\nНичья! " + scoreText());
    }


    /**
     * Формирует текст со счётом.
     *
     * @return строка вида "Счёт 1:0 в вашу пользу."
     */

    private String scoreText() {
        String text = "Счёт " + playerWins + ":" + dealerWins;
        if (playerWins > dealerWins) {
            text += " в вашу пользу.";
        } else if (dealerWins > playerWins) {
            text += " в пользу дилераю.";
        } else {
            text += ".";
        }
        return text;
    }
}


