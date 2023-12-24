package tictactoe;

import java.util.*;
class Board {
    private static String[] boardLayout;
    public Board(){
        boardLayout = new String[]{"_", "_", "_", "_", "_", "_", "_", "_", "_"};
    }

    public static String[] getBoardLayout() {
        return boardLayout;
    }

    public void displayBoard(){
        System.out.println("---------");
        for (int i = 0; i < 9; i += 3) {
            if(boardLayout != null) {
                System.out.printf("| %s %s %s |%n", getSymbol(boardLayout[i]), getSymbol(boardLayout[i + 1]), getSymbol(boardLayout[i + 2]));
            }
        }
        System.out.println("---------");
    }

    private String getSymbol(String input) {
        return input.equals("_") ? " " : input;
    }

    public void makeMove(int coord1, int coord2, String player){
        int index = (coord1 - 1) * 3 + (coord2 - 1);

        if(isValid(coord1, coord2) && boardLayout[index].equals("_")) {
            boardLayout[index] = player;
        }

    }

    public boolean isValid(int coord1, int coord2) {
        if(coord1 < 1 || coord1 > 3 || coord2 < 1 || coord2 > 3) {
            System.out.println("Coordinates should be from 1 to 3!");
            return false;
        } else {
            int index = (coord1 - 1) * 3 + (coord2 - 1);
            if(boardLayout[index].equals("X") || boardLayout[index].equals("O")) {
                System.out.println("This cell is occupied! Choose another one!");
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        int[][] lines = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };
        for (int[] currentLine : lines) {
            int a = currentLine[0];
            int b = currentLine[1];
            int c = currentLine[2];
            if(boardLayout[a] != null && boardLayout[a].equals("X") || boardLayout[a].equals("O")) {
                if (boardLayout[a] != null && boardLayout[a].equals(boardLayout[b]) && boardLayout[b].equals(boardLayout[c])) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isRandomValid(int coord1, int coord2) {
        int index = (coord1 - 1) * 3 + (coord2 - 1);
        if(boardLayout[index].equals("X") || boardLayout[index].equals("O")) {
            return false;
        }
        return true;
    }

    public boolean isFull() {
        boolean isFull = true;
        for (String s : boardLayout) {
            if (s.equals("_")) {
                isFull = false;
                break;
            }
        }
        return isFull;
    }

}

abstract class Player {
    protected final String symbol;

    public Player(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() { return symbol; }
}

class User extends Player{
    public User(String symbol) {
        super(symbol);
    }
}

class EasyComputer extends Player {
    public EasyComputer(String symbol) { super(symbol); }
}

class MediumComputer extends Player {
    public MediumComputer(String symbol) { super(symbol); }
}

class HardComputer extends Player {
    public HardComputer(String symbol) { super(symbol); }
}

class TicTacToeGame {
    private final Board board;
    private final Player playerX;
    private final Player playerO;
    private Player currentPlayer;

    public TicTacToeGame(String player1, String player2) {
        this.board = new Board();

        switch (player1) {
            case "user" -> playerX = new User("X");
            case "easy" -> playerX = new EasyComputer("X");
            case "medium" -> playerX = new MediumComputer("X");
            case "hard" -> playerX = new HardComputer("X");
            default -> playerX = null;
        }

        switch (player2) {
            case "user" -> playerO = new User("O");
            case "easy" -> playerO = new EasyComputer("O");
            case "medium" -> playerO = new MediumComputer("O");
            case "hard" -> playerO = new HardComputer("O");
            default -> playerO = null;
        }

        currentPlayer = playerX;
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        boolean gameOver = false;

        while (!gameOver) {
            board.displayBoard();

            if (currentPlayer instanceof User) {
                handleUserMove(scanner);
            } else if(currentPlayer instanceof EasyComputer){
                handleEasyComputerMove();
            } else if(currentPlayer instanceof MediumComputer){
                if(!handleMediumComputerMove()) {
                    System.out.println("Error!");
                }
            } else if(currentPlayer instanceof HardComputer) {
                handleHardComputerMove();
            }

            if (board.isGameOver()) {
                board.displayBoard();
                gameState(currentPlayer.getSymbol());
                gameOver = true;
            } else if (board.isFull()) {
                board.displayBoard();
                System.out.println("Draw");
                gameOver = true;
            } else {
                currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
            }
        }
    }

    private void handleUserMove(Scanner scanner) {
        int coord1, coord2;

        while (true) {
            System.out.print("Enter the coordinates: > ");
            if (scanner.hasNextInt()) {
                coord1 = scanner.nextInt();

                if (scanner.hasNextInt()) {
                    coord2 = scanner.nextInt();

                    if (board.isValid(coord1, coord2)) {
                        break;
                    }
                } else {
                    System.out.println("You should enter numbers!");
                    scanner.next();
                }
            } else {
                System.out.println("You should enter numbers!");
                scanner.next();
            }

            scanner.nextLine();
        }

        board.makeMove(coord1, coord2, currentPlayer.getSymbol());
    }

    private void handleEasyComputerMove() {
        int coord1, coord2;

        System.out.println("Making move level \"easy\"");
        do {
            coord1 = randCord();
            coord2 = randCord();
        } while (!board.isRandomValid(coord1, coord2));

        board.makeMove(coord1, coord2, currentPlayer.getSymbol());
    }
    public boolean handleMediumComputerMove() {
        String[] boardLayout = Board.getBoardLayout();
        String player = currentPlayer.getSymbol();
        System.out.println("Making move level \"medium\"");
        if(checkRight(boardLayout, player)) {
            return true;
        } else if(checkLeft(boardLayout, player)) {
            return true;
        } else if(checkTop(boardLayout, player)) {
            return true;
        } else if(checkBottom(boardLayout, player)) {
            return true;
        } else if(checkNegDiagonal(boardLayout, player)) {
            return true;
        } else if(checkPosDiagonal(boardLayout, player)) {
            return true;
        }else if(checkVerticalInBetween(boardLayout, player)) {
            return true;
        } else if(checkHorizontalInBetween(boardLayout, player)) {
            return true;
        } else if(checkNegDiagonalInBetween(boardLayout, player)) {
            return true;
        } else if(checkPosDiagonalInBetween(boardLayout, player)) {
            return true;
        } else {
            int coord1, coord2;

            do {
                coord1 = randCord();
                coord2 = randCord();
            } while (!board.isRandomValid(coord1, coord2));

            board.makeMove(coord1, coord2, player);
            return true;
        }
    }

    public boolean checkRight(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[1].equals("_") && boardLayout[0].equals(boardLayout[1]) && boardLayout[2].equals("_")) {
            board.makeMove(1, 3, player);
            return true;
        } else if (!boardLayout[3].equals("_") && !boardLayout[4].equals("_") && boardLayout[3].equals(boardLayout[4]) && boardLayout[5].equals("_")) {
            board.makeMove(2, 3, player);
            return true;
        } else if(!boardLayout[6].equals("_") && !boardLayout[7].equals("_") && boardLayout[6].equals(boardLayout[7]) && boardLayout[8].equals("_")) {
            board.makeMove(3, 3, player);
            return true;
        }
        return false;
    }

    public boolean checkLeft(String[] boardLayout, String player) {
        if(!boardLayout[1].equals("_") && !boardLayout[2].equals("_") && boardLayout[1].equals(boardLayout[2]) && boardLayout[0].equals("_")) {
            board.makeMove(1, 1, player);
            return true;
        } else if (!boardLayout[4].equals("_") && !boardLayout[5].equals("_") && boardLayout[4].equals(boardLayout[5]) && boardLayout[3].equals("_")) {
            board.makeMove(2, 1, player);
            return true;
        } else if(!boardLayout[7].equals("_") && !boardLayout[8].equals("_") && boardLayout[7].equals(boardLayout[8]) && boardLayout[6].equals("_")) {
            board.makeMove(3, 1, player);
            return true;
        }
        return false;
    }

    public boolean checkTop(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[3].equals("_") && boardLayout[0].equals(boardLayout[3]) && boardLayout[6].equals("_")) {
            board.makeMove(3, 1, player);
            return true;
        } else if (!boardLayout[1].equals("_") && !boardLayout[4].equals("_") && boardLayout[1].equals(boardLayout[4]) && boardLayout[7].equals("_")) {
            board.makeMove(3, 2, player);
            return true;
        } else if(!boardLayout[2].equals("_") && !boardLayout[5].equals("_") && boardLayout[2].equals(boardLayout[5]) && boardLayout[8].equals("_")) {
            board.makeMove(3, 3, player);
            return true;
        }
        return false;
    }

    public boolean checkBottom(String[] boardLayout, String player) {
        if(!boardLayout[3].equals("_") && !boardLayout[6].equals("_") && boardLayout[3].equals(boardLayout[6]) && boardLayout[0].equals("_")) {
            board.makeMove(1, 1, player);
            return true;
        } else if (!boardLayout[4].equals("_") && !boardLayout[7].equals("_") && boardLayout[4].equals(boardLayout[7]) && boardLayout[1].equals("_")) {
            board.makeMove(1, 2, player);
            return true;
        } else if(!boardLayout[5].equals("_") && !boardLayout[8].equals("_") && boardLayout[5].equals(boardLayout[8]) && boardLayout[2].equals("_")) {
            board.makeMove(1, 3, player);
            return true;
        }
        return false;
    }

    public boolean checkNegDiagonal(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[4].equals("_") && boardLayout[0].equals(boardLayout[4]) && boardLayout[8].equals("_")) {
            board.makeMove(3, 3, player);
            return true;
        } else if (!boardLayout[4].equals("_") && !boardLayout[8].equals("_") && boardLayout[4].equals(boardLayout[8]) && boardLayout[0].equals("_")) {
            board.makeMove(1, 1, player);
            return true;
        }
        return false;
    }

    public boolean checkPosDiagonal(String[] boardLayout, String player) {
        if(!boardLayout[2].equals("_") && !boardLayout[4].equals("_") && boardLayout[2].equals(boardLayout[4]) && boardLayout[6].equals("_")) {
            board.makeMove(3, 1, player);
            return true;
        } else if (!boardLayout[4].equals("_") && !boardLayout[6].equals("_") && boardLayout[4].equals(boardLayout[6]) && boardLayout[2].equals("_")) {
            board.makeMove(1, 3, player);
            return true;
        }
        return false;
    }

    public boolean checkVerticalInBetween(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[6].equals("_") && boardLayout[0].equals(boardLayout[6]) && boardLayout[3].equals("_")) {
            board.makeMove(2, 1, player);
            return true;
        } else if (!boardLayout[1].equals("_") && !boardLayout[7].equals("_") && boardLayout[1].equals(boardLayout[7]) && boardLayout[4].equals("_")) {
            board.makeMove(2, 2, player);
            return true;
        } else if(!boardLayout[2].equals("_") && !boardLayout[8].equals("_") && boardLayout[2].equals(boardLayout[8]) && boardLayout[5].equals("_")) {
            board.makeMove(2, 3, player);
            return true;
        }
        return false;
    }

    public boolean checkHorizontalInBetween(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[2].equals("_") && boardLayout[0].equals(boardLayout[2]) && boardLayout[1].equals("_")) {
            board.makeMove(1, 2, player);
            return true;
        } else if (!boardLayout[3].equals("_") && !boardLayout[5].equals("_") && boardLayout[3].equals(boardLayout[5]) && boardLayout[4].equals("_")) {
            board.makeMove(2, 2, player);
            return true;
        } else if(!boardLayout[6].equals("_") && !boardLayout[8].equals("_") && boardLayout[6].equals(boardLayout[8]) && boardLayout[7].equals("_")) {
            board.makeMove(3, 2, player);
            return true;
        }
        return false;
    }

    public boolean checkNegDiagonalInBetween(String[] boardLayout, String player) {
        if(!boardLayout[0].equals("_") && !boardLayout[8].equals("_") && boardLayout[0].equals(boardLayout[8]) && boardLayout[4].equals("_")) {
            board.makeMove(2, 2, player);
            return true;
        }
        return false;
    }

    public boolean checkPosDiagonalInBetween(String[] boardLayout, String player) {
        if(!boardLayout[2].equals("_") && !boardLayout[6].equals("_") && boardLayout[2].equals(boardLayout[6]) && boardLayout[4].equals("_")) {
            board.makeMove(2, 2, player);
            return true;
        }
        return false;
    }

    public void handleHardComputerMove() {
        int bestVal = -1000;
        int bestMove = -1;

        String[] boardLayout = Board.getBoardLayout();

        for(int i = 0; i < boardLayout.length; i++) {
            if(boardLayout[i].equals("_")) {
                boardLayout[i] = currentPlayer.getSymbol();
                int moveVal = minimax(boardLayout, 0, false);
                boardLayout[i] = "_";

                if(moveVal > bestVal) {
                    bestMove = i;
                    bestVal = moveVal;
                }
            }
        }

        int coord1 = bestMove / 3 + 1;
        int coord2 = bestMove % 3 + 1;

        System.out.println("Making move level \"hard\"");
        board.makeMove(coord1, coord2, currentPlayer.getSymbol());
    }

    public int minimax(String[] board, int depth, boolean isMax) {
        int score = evaluate(board);
        if (score == 10)
            return score;
        if (score == -10)
            return score;
        if (isFull(board)) {
            return 0;
        }

        int best;
        if (isMax) {
            best = -1000;

            for (int i = 0; i < board.length; i++) {
                if (board[i].equals("_")) {
                    board[i] = currentPlayer.getSymbol();
                    best = Math.max(best, minimax(board, depth + 1, false));
                    board[i] = "_";
                }
            }

        } else {
            best = 1000;

            for(int i = 0; i < board.length; i++) {
                if(board[i].equals("_")) {
                    board[i] = oppositeSymbol(currentPlayer);
                    best = Math.min(best, minimax(board, depth + 1, true));
                    board[i] = "_";
                }
            }

        }
        return best;

    }

    public static boolean isFull(String[] boardLayout) {
        boolean isFull = true;
        for (String s : boardLayout) {
            if (s.equals("_")) {
                isFull = false;
                break;
            }
        }
        return isFull;
    }

    public int evaluate(String[] boardLayout) {
        int[][] lines = {
                {0, 1, 2},
                {3, 4, 5},
                {6, 7, 8},
                {0, 3, 6},
                {1, 4, 7},
                {2, 5, 8},
                {0, 4, 8},
                {2, 4, 6}
        };
        for (int[] currentLine : lines) {
            int a = currentLine[0];
            int b = currentLine[1];
            int c = currentLine[2];
            if(boardLayout[a] != null && boardLayout[a].equals("X") || boardLayout[a].equals("O")) {
                if (boardLayout[a] != null && boardLayout[a].equals(boardLayout[b]) && boardLayout[b].equals(boardLayout[c])) {
                    if(boardLayout[a].equals(currentPlayer.getSymbol())) {
                        return 10;
                    } else if(boardLayout[a].equals(oppositeSymbol(currentPlayer))) {
                        return -10;
                    }
                }
            }
        }
        return -1;
    }

    public int randCord() {return (int) (Math.random() * 3) + 1;}

    public void gameState(String state) {
        System.out.println(state + " wins!");
    }

    public String oppositeSymbol(Player player) {
        return player.getSymbol().equals("X") ? "O" : "X";
    }

}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String errorMessage = "Bad parameters!";
        String playerX;
        String playerO;

        label1:
        do {
            System.out.print("Input command: > ");
            String[] input = scanner.nextLine().split(" ");

            if(input.length == 1 && input[0].equals("exit")) {
                break;
            }

            while(!isValid(input, errorMessage)) {
                System.out.print("Input command: > ");
                input = scanner.nextLine().split(" ");
                if(input.length == 1 && input[0].equals("exit")) {
                    break label1;
                }
            }

            playerX = input[1];
            playerO = input[2];

            TicTacToeGame game = new TicTacToeGame(playerX, playerO);
            game.startGame();

        } while(true);

    }

    public static boolean isValid(String[] input, String errorMessage){
        if(input[0].equals("exit")) {
            return true;
        }
        if(!input[0].equals("start")) {
            System.out.println(errorMessage);
            return false;
        }
        if(input.length != 3) {
            System.out.println(errorMessage);
            return false;
        }
        for (String s : input) {
            if (!s.equals("start") && !s.equals("easy") && !s.equals("user") && !s.equals("medium") && !s.equals("hard")) {
                System.out.println(errorMessage);
                return false;
            }
        }
        return true;
    }
}