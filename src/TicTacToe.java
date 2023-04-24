import java.util.Scanner;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;
    private static final char HUMAN_PLAYER = 'X';
    private static final char BOT_PLAYER = 'O';
    private static final char EMPTY_CELL = '-';

    public static void main(String[] args) {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard(board);
        Scanner scanner = new Scanner(System.in);
        boolean humanTurn = true;
        while (!isGameOver(board)) {
            printBoard(board);
            if (humanTurn) {
                System.out.print("Your turn. Enter row (1-3): ");
                int row = scanner.nextInt() - 1;
                System.out.print("Your turn." + " Enter column (1-3): ");
                int col = scanner.nextInt() - 1;
                if (isValidMove(board, row, col)) {
                    board[row][col] = HUMAN_PLAYER;
                    humanTurn = false;
                } else {
                    System.out.println("Invalid move. Try again.");
                }
            } else {
                System.out.println("Bot's turn.");
                int[] botMove = getNextBotMove(board);
                board[botMove[0]][botMove[1]] = BOT_PLAYER;
                humanTurn = true;
            }
        }
        printBoard(board);
        if (hasHumanWon(board)) {
            System.out.println("Congratulations! You have won.");
        } else if (hasBotWon(board)) {
            System.out.println("Sorry, you have lost. Better luck next time!");
        } else {
            System.out.println("Game over. It's a draw!");
        }
    }

    private static void initializeBoard(char[][] board) {
        for (int i = 0;
             i < BOARD_SIZE;
             i++) {
            for (int j = 0;
                 j < BOARD_SIZE;
                 j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    private static void printBoard(char[][] board) {
        final String BLUE = "\u001B[34m";
        final String RED = "\u001B[31m";
        final String RESET = "\u001B[0m";
        System.out.println("    1   2   3");
        System.out.println("  ┌───┬───┬───┐");
        for (int i = 0;
             i < BOARD_SIZE;
             i++) {
            System.out.print(i + 1 + " ");
            for (int j = 0;
                 j < BOARD_SIZE;
                 j++) {
                if (board[i][j] == 'O') {
                    System.out.print("│ " + BLUE + board[i][j] + RESET + " ");
                } else if (board[i][j] == 'X') {
                    System.out.print("│ " + RED + board[i][j] + RESET + " ");
                } else {
                    System.out.print("│ " + board[i][j] + " ");
                }
            }
            System.out.println("│");
            if (i < BOARD_SIZE - 1) {
                System.out.println("  ├───┼───┼───┤");
            }
        }
        System.out.println("  └───┴───┴───┘");
    }

    private static boolean isValidMove(char[][] board, int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE && board[row][col] == EMPTY_CELL;
    }

    private static int[] getNextBotMove(char[][] board) {
        int[] move = new int[2];
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0;
             i < BOARD_SIZE;
             i++) {
            for (int j = 0;
                 j < BOARD_SIZE;
                 j++) {
                if (board[i][j] == EMPTY_CELL) {
                    board[i][j] = BOT_PLAYER;
                    int score = minimax(board, false, 0);
                    board[i][j] = EMPTY_CELL;
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private static int minimax(char[][] board, boolean isMaximizing, int depth) {
        if (hasHumanWon(board)) {
            return -10 + depth;
        }
        if (hasBotWon(board)) {
            return 10 - depth;
        }
        if (isGameOver(board)) {
            return 0;
        }
        int bestScore;
        if (isMaximizing) {
            bestScore = Integer.MIN_VALUE;
            for (int i = 0;
                 i < BOARD_SIZE;
                 i++) {
                for (int j = 0;
                     j < BOARD_SIZE;
                     j++) {
                    if (board[i][j] == EMPTY_CELL) {
                        board[i][j] = BOT_PLAYER;
                        int score = minimax(board, false, depth + 1);
                        board[i][j] = EMPTY_CELL;
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
        } else {
            bestScore = Integer.MAX_VALUE;
            for (int i = 0;
                 i < BOARD_SIZE;
                 i++) {
                for (int j = 0;
                     j < BOARD_SIZE;
                     j++) {
                    if (board[i][j] == EMPTY_CELL) {
                        board[i][j] = HUMAN_PLAYER;
                        int score = minimax(board, true, depth + 1);
                        board[i][j] = EMPTY_CELL;
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
        }
        return bestScore;
    }

    private static boolean hasHumanWon(char[][] board) {
        return hasPlayerWon(board, HUMAN_PLAYER);
    }

    private static boolean hasBotWon(char[][] board) {
        return hasPlayerWon(board, BOT_PLAYER);
    }

    private static boolean hasPlayerWon(char[][] board, char player) {
        for (int i = 0;
             i < BOARD_SIZE;
             i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
        }
        for (int j = 0;
             j < BOARD_SIZE;
             j++) {
            if (board[0][j] == player && board[1][j] == player && board[2][j] == player) {
                return true;
            }
        }
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    private static boolean isGameOver(char[][] board) {
        if (hasHumanWon(board) || hasBotWon(board)) {
            return true;
        }
        for (int i = 0;
             i < BOARD_SIZE;
             i++) {
            for (int j = 0;
                 j < BOARD_SIZE;
                 j++) {
                if (board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }
}
