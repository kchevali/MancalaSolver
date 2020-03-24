class Core {
    Board board;
    int maxDepth;
    int maxMoves;

    public Core(Board board, int maxDepth, int maxMoves) {
        this.board = board;
        this.maxDepth = maxDepth;
        this.maxMoves = maxMoves;
    }

    public Move move(boolean isAlphaTurn, int movesMade) {
        return move(isAlphaTurn, 0, movesMade, movesMade - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public Move move(boolean isAlphaTurn, int depth, int movesMade, int prevMoveCount, int alpha, int beta) {
        Move bestMove = new Move(isAlphaTurn, worstScore(isAlphaTurn));
        for (int i = 0; i < board.rows; i++) {
            Move newMove = board.createMove(i, isAlphaTurn);
            int pos = board.getAbsolutePosition(newMove);
            if (board.spaces[pos] > 0) {
                board.coreMove(newMove);
                board.setNextTurn(newMove);

                int score;
                boolean isGameOver = board.isGameOver();
                int moveCount = prevMoveCount + 1;
                if (moveCount > maxMoves) {
                    score = worstScore(isAlphaTurn);
                } else if (isGameOver || depth >= maxDepth) {
                    score = board.getScore(isGameOver);
                } else {
                    Move nextMove = move(newMove.nextTurn, depth + 1, movesMade,
                            isAlphaTurn == newMove.nextTurn ? moveCount : movesMade - 1, alpha, beta);
                    score = nextMove.score;
                }
                board.invert(newMove);
                newMove.setScore(score);
                bestMove = compare(bestMove, newMove);

                if (isAlphaTurn) {
                    alpha = Math.max(alpha, score);
                } else {
                    beta = Math.min(beta, score);
                }
                if (alpha >= beta)
                    break;

                // System.out.println("Reveral");
                // System.out.println(board);
                // System.out.println(board);
                // System.out.println("Reversal done");
            }
        }
        return bestMove;
    }

    public int worstScore(boolean isAlphaTurn) {
        return isAlphaTurn ? Integer.MIN_VALUE / 4 : Integer.MAX_VALUE / 4;
    }

    public Move compare(Move a, Move b) {
        return a.isAlphaTurn == (a.score > b.score) ? a : b;
    }
}