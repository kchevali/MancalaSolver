class CaptureBoard extends Board {

    public CaptureBoard(int rows, int pebbleCount) {
        super(rows, pebbleCount);
    }

    public void coreMove(Move move) {
        basicMove(move);
        if (isAtGoal(move.lastMove))
            return;

        boolean isHomeSide = move.isAlphaTurn == (move.lastMove < alphaGoal);
        int lastMove = move.lastMove;
        int across = acrossIndex(lastMove);
        if (isHomeSide && spaces[move.lastMove] == 1 && spaces[across] > 0) {
            int winning = spaces[across] + 1;
            step(move, lastMove, -1);
            step(move, across, -spaces[across]);
            step(move, move.isAlphaTurn ? alphaGoal : betaGoal, winning);
            move.lastMove = lastMove;
        }
    }
}