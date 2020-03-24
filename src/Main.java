import java.util.*;

class Main {

    static Scanner scan = new Scanner(System.in);
    static Board board;
    static Core core;

    static int rows;

    public static void main(String[] args) {
        int pebbleCount = 4;// number of pebbles per space
        int maxDepth = 13; // max search depth
        int maxMoves = Integer.MAX_VALUE;// 4 //handicaps cpu so it doesn't seem like you are cheating when you are
                                         // playing
        // your friends

        board = new CaptureBoard(rows = 6, pebbleCount);// May replace with AvalancheBoard
        board.randomize();

        boolean isEnterBoard = false;
        enterBoard(isEnterBoard);

        core = new Core(board, maxDepth, maxMoves);
        System.out.println(board);

        boolean isPlayerFirst = false;
        boolean alpha = true;

        if (isPlayerFirst) {
            System.out.println("Beta:");
            playerTurn(!alpha);
        }
        while (!board.isGameOver()) {
            System.out.println("\n=========================================");
            System.out.println("Alpha:");
            cpuTurn(alpha);

            if (board.isGameOver())
                break;
            System.out.println("Beta:");
            cpuTurn(!alpha);

        }
        System.out.println(board.isWin() ? "Alpha Wins!!" : "Beta is Victorious!");
        // core.testMove(true);
        board.addUpBoard();
        System.out.println(board);
        scan.close();
    }

    public static void enterBoard(boolean isEnterBoard) {
        if (isEnterBoard) {
            for (int i = 0; i < rows; i++) {
                int across = board.acrossIndex(i);
                System.out.println("Next Row");
                board.spaces[i] = scan.nextInt();
                board.spaces[across] = scan.nextInt();
            }
            System.out.println("Player Score");
            board.spaces[board.alphaGoal] = scan.nextInt();
            System.out.println("Other Score");
            board.spaces[board.betaGoal] = scan.nextInt();
        }
    }

    public static void cpuTurn(boolean isLefty) {
        Move cpuMove;
        int movesMade = 0;
        do {
            cpuMove = core.move(isLefty, movesMade++);
            board.execute(cpuMove);
            System.out.println("CPU Move: " + cpuMove.firstPos);
            // System.out.println(board);
        } while (cpuMove.nextTurn == cpuMove.isAlphaTurn);
        int score = cpuMove.score * (isLefty ? 1 : -1);
        if (score > 9000) {
            System.out.println("Win in sight!");
            score -= 10000;
        }
        if (score < -9000) {
            System.out.println("Nani!! A loss??");
            score += 10000;
        }
        System.out.println("Net Score: " + score + " / " + board.allPebbleCount);
        System.out.println(board);
    }

    public static void playerTurn(boolean isLefty) {
        Move userMove;
        System.out.println("Start Turn: What would you like to move (start at 0)?");
        do {
            int userPos;
            boolean check;
            do {
                userPos = scan.nextInt();
                int absPos = 0;
                if (check = (userPos >= 0 && userPos < rows))
                    absPos = board.getAbsolutePosition(userPos, isLefty);
                if (check = check && board.spaces[absPos] <= 0)
                    System.out.println("Invalid space!");
            } while (check);
            userMove = board.move(userPos, isLefty);
            System.out.println(board);
            if (userMove.nextTurn == userMove.isAlphaTurn) {
                System.out.println("FREE Turn: What would you like to move?");
            }
        } while (userMove.nextTurn == userMove.isAlphaTurn);
    }
}