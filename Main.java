import java.util.Scanner;

class PlayingField {
    public static final int playingFieldSize = 3;
    public static char[] playingField = new char[playingFieldSize * playingFieldSize];
    // при создании объекта класса заполнить массив (игровое поле) числами
    public PlayingField() {
        for (int i = 0; i < playingFieldSize * playingFieldSize; i++) {
            playingField[i] = Character.forDigit(i + 1, 10);
        }
    }
    // вывод игрового поля
    public static void OutPlayingField(char[] playingField) {
        System.out.println("    -=The-Game=-   ");
        System.out.println("-------------------");
        for (int i = 0; i < playingFieldSize * playingFieldSize; i += playingFieldSize) {
            System.out.println(String.format("|  %1$s  |  %2$s  |  %3$s  |",
                    playingField[i], playingField[i + 1], playingField[i + 2]));
            System.out.println("-------------------");
        }
    }
    // проверка на свободную ячейку
    public boolean CheckFreeCell(int usersMove) {
        return Character.isDigit(playingField[usersMove - 1])? true: false;
    }

    // перерисовка карты
    public static void MapRedrawing(int usersMove, char c) {
        for (int i = 0; i < playingFieldSize * playingFieldSize; i++) {
            if ((i + 1) == usersMove) {
                playingField[i] = c;
                break;
            }
        }
    }
    // случайный генератор чисел с диапазоном
    private static int RandomNumberInRange(int min, int max) {
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    private static int GetIndexOfFreeCells(int[] indexOfFreeCells) {
        int count = 0;
        for (int i = 0; i < indexOfFreeCells.length; i++) {
            if (Character.isDigit(playingField[i])) {
                indexOfFreeCells[count++] = i;
            }
        }
        return count;
    }
    // ход бота
    public static void BotMove(char c) {
        int[] indexOfFreeCells = new int[playingFieldSize * playingFieldSize];
        for (int i = 0; i < playingFieldSize; i++) {
            indexOfFreeCells[i] = -1;
        }
        int countOfFreeCells = GetIndexOfFreeCells(indexOfFreeCells);
        MapRedrawing(indexOfFreeCells[RandomNumberInRange(1, countOfFreeCells) - 1] + 1, c);
    }
    // проверка на выиграшь
    public static boolean WhoWin(char c) {
        if ((playingField[0] == c && playingField[1] == c && playingField[2] == c) ||
                (playingField[3] == c && playingField[4] == c && playingField[5] == c) ||
                (playingField[6] == c && playingField[7] == c && playingField[8] == c)) {
            return true;
        }
        else if ((playingField[0] == c && playingField[4] == c && playingField[8] == c) ||
                (playingField[2] == c && playingField[4] == c && playingField[6] == c)) {
            return true;
        }
        else if ((playingField[0] == c && playingField[3] == c && playingField[6] == c) ||
                (playingField[1] == c && playingField[4] == c && playingField[7] == c) ||
                (playingField[2] == c && playingField[5] == c && playingField[8] == c)) {
            return true;
        }
        return false;
    }
    // проверка кол-ва свободных ячеек
    public static boolean NoFreeCells() {
        int count = 0;
        for (int i = 0; i < playingFieldSize * playingFieldSize; i++) {
            if (Character.isAlphabetic(playingField[i])) {
                count++;
            }
        }
        return count == playingFieldSize * playingFieldSize? true: false;
    }
}

public class Main {

    private static PlayingField playingField;

    private static boolean CheckOnWin(boolean play) {
        if (playingField.WhoWin('X')) { // если кто-то выиграл
            playingField.OutPlayingField(playingField.playingField); // вывести поле
            System.out.println("    -=You-Win=-    "); // поздравление
            play = false;
        } else if (playingField.WhoWin('O')) {
            playingField.OutPlayingField(playingField.playingField);
            System.out.println("    -=You-Lose=-   ");
            play = false;
        }
        return play;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int usersMove; // ход игрока
        boolean play = true;
        playingField = new PlayingField(); // построить игровое поле
        while (play) {
            playingField.OutPlayingField(playingField.playingField);
            System.out.print("Your turn: ");
            usersMove = scanner.nextInt();
            if (usersMove < 10 && usersMove > 0) { // если введенное число в пределах диапазона 1-9
                if (playingField.CheckFreeCell(usersMove)) { // если ячейка в поле свободна
                    playingField.MapRedrawing(usersMove, 'X'); // нарисовать на поле "Х"
                    if (!playingField.NoFreeCells()) { // если есть свободные ячейки
                        playingField.BotMove('O'); // ход бота
                        if (!playingField.NoFreeCells()) { // если есть свободные ячейки
                            play = CheckOnWin(play);
                        }
                    } else { // если нет свободных ячеек
                        playingField.OutPlayingField(playingField.playingField); // вывод поля
                        play = CheckOnWin(play);
                        if (play) {
                            System.out.println("      -=Draw=-     "); // ничья
                            play = false; // закончить игру
                        }
                    }
                } else {
                    System.out.println("Enter free cell");
                }
            } else {
                System.out.println("Invalid input. Enter numbers 1 - 9");
            }
            if (!play) {
                System.out.print("Try again? (yes = 1 or no = 0): ");
                int userChoice = scanner.nextInt();
                if (userChoice == 1) {
                    playingField = new PlayingField();
                    play = true;
                }
            }
        }
    }
}

