class Core {
    Board board;
    int maxDepth;
    int maxMoves;

    public Core(Board board, int maxDepth, int maxMoves) {
        this.board = board;
        this.maxDepth = maxDepth;
        this.maxMoves = maxMoves;
    }

    public Move move(boolean isPlayerTurn, int movesMade) {
        return move(isPlayerTurn, 0, movesMade, movesMade - 1);
    }

    public Move move(boolean isPlayerTurn, int depth, int movesMade, int prevMoveCount) {
        Move bestMove = new Move(isPlayerTurn);
        for (int i = 0; i < board.rows; i++) {
            Move newMove = board.createMove(i, isPlayerTurn);
            int pos = board.getAbsolutePosition(newMove);
            if (board.spaces[pos] > 0) {
                board.coreMove(newMove);
                board.setNextTurn(newMove);

                int score;
                boolean isGameOver = board.isGameOver();
                int moveCount = prevMoveCount + 1;
                if (moveCount > maxMoves) {
                    score = punishment(isPlayerTurn);
                } else if (isGameOver || depth >= maxDepth) {
                    score = board.getScore(isGameOver);
                } else {
                    Move nextMove = move(newMove.nextTurn, depth + 1, movesMade,
                            isPlayerTurn == newMove.nextTurn ? moveCount : movesMade - 1);
                    score = nextMove.score;
                }
                newMove.setScore(score);
                bestMove = compare(bestMove, newMove);
                // System.out.println("Reveral");
                // System.out.println(board);
                board.invert(newMove);
                // System.out.println(board);
                // System.out.println("Reversal done");
            }
        }
        return bestMove;
    }

    public int punishment(boolean isPlayerTurn) {
        return isPlayerTurn ? Integer.MIN_VALUE / 2 : Integer.MAX_VALUE / 2;
    }

    public Move compare(Move a, Move b) {
        return a.isPlayerTurn == (a.score > b.score) ? a : b;
    }
}