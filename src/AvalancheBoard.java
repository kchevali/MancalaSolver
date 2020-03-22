class AvalancheBoard extends Board {

    public AvalancheBoard(int rows, int pebbleCount) {
        super(rows, pebbleCount);
    }

    public void coreMove(Move move) {
        do {
            basicMove(move);
            setRelativePosition(move, move.lastMove);
        } while (!isAtGoal(move.lastMove) && spaces[move.lastMove] > 1);
    }
}