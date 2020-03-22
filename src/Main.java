import java.util.*;

class Main {

    static Scanner scan = new Scanner(System.in);
    static Board board;
    static Core core;

    static int rows;

    public static void main(String[] args) {
        int pebbleCount = 4;
        int maxDepth = 9;
        int maxMoves = 4;

        board = new AvalancheBoard(rows = 6, pebbleCount);

        boolean isEnterBoard = false;
        enterBoard(isEnterBoard);

        core = new Core(board, maxDepth, maxMoves);
        System.out.println(board);

        boolean isPlayerFirst = true;

        if (isPlayerFirst) {
            playerTurn();
        }
        while (!board.isGameOver()) {

            cpuTurn();
            playerTurn();

        }
        System.out.println(board.isWin() ? "Victory!!" : "Shame on my family...");
        // core.testMove(true);
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
            board.spaces[board.playerGoal] = scan.nextInt();
            System.out.println("Other Score");
            board.spaces[board.otherGoal] = scan.nextInt();
        }
    }

    public static void cpuTurn() {
        Move cpuMove;
        int movesMade = 0;
        do {
            cpuMove = core.move(true, movesMade++);
            board.execute(cpuMove);
            System.out.println("CPU Move: " + cpuMove.firstPos);
            // System.out.println(board);
        } while (cpuMove.nextTurn == cpuMove.isPlayerTurn);
        int score = cpuMove.score;
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

    public static void playerTurn() {
        Move userMove;
        System.out.println("Start Turn: What would you like to move (start at 0)?");
        do {
            int userPos = scan.nextInt();
            userMove = board.move(userPos, false);
            System.out.println(board);
            if (userMove.nextTurn == userMove.isPlayerTurn) {
                System.out.println("FREE Turn: What would you like to move?");
            }
        } while (userMove.nextTurn == userMove.isPlayerTurn);
    }
}