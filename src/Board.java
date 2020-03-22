class Board {

    // 0 -> (totalSpaces-1) : board
    // totalSpaces : playerScore | totalSpaces + 1 : otherScore
    int[] spaces;
    int rows, totalSpaces, playerGoal, otherGoal;
    int allPebbleCount;

    // player: top -> bottom
    // other: bottom -> top

    public Board(int rows, int pebbleCount) {
        this.rows = rows;
        this.totalSpaces = 2 * rows + 2;
        spaces = new int[totalSpaces];// +2 for score

        for (int i = 0; i < totalSpaces; i++) {
            spaces[i] = pebbleCount;
        }

        this.playerGoal = rows;
        this.otherGoal = totalSpaces - 1;

        this.spaces[playerGoal] = this.spaces[otherGoal] = 0;// score
        this.allPebbleCount = pebbleCount * this.rows * 2;
    }

    public Move createMove(int pos, boolean isPlayerTurn) {
        return new Move(pos, isPlayerTurn, totalSpaces);
    }

    public int acrossIndex(int absPos) {
        return totalSpaces - absPos - 2;
    }

    public Move move(int pos, boolean isPlayerTurn) {
        Move move = createMove(pos, isPlayerTurn);
        coreMove(move);
        setNextTurn(move);
        return move;
    }

    public void step(Move move, int absPos, int count) {
        move.delta(absPos, count);
        spaces[absPos] += count;
    }

    public void basicMove(Move move) {
        int absPos = getAbsolutePosition(move);
        int pebbles = spaces[absPos];
        step(move, absPos, -pebbles);

        absPos = nextPosition(move.isPlayerTurn, absPos);
        for (int i = 0; i < pebbles; i++) {
            step(move, absPos, 1);
            absPos = nextPosition(move.isPlayerTurn, absPos);
        }
    }

    public void coreMove(Move move) {
        basicMove(move);
    }

    public void setNextTurn(Move move) {
        move.setNextTurn(!isGameOver() && isAtGoal(move.lastMove));
    }

    public void execute(Move move) {
        for (int i = 0; i < totalSpaces; i++) {
            spaces[i] += move.changes[i];
        }
        setNextTurn(move);
    }

    public void invert(Move move) {
        for (int i = 0; i < totalSpaces; i++) {
            spaces[i] -= move.changes[i];
        }
    }

    public int nextPosition(boolean isPlayerTurn, int pos) {
        pos += (isPlayerTurn && pos == otherGoal - 1) || (!isPlayerTurn && pos == playerGoal - 1) ? 2 : 1;
        return pos - (pos >= totalSpaces ? totalSpaces : 0);
    }

    public int prevPosition(boolean isPlayerTurn, int pos) {
        pos -= (isPlayerTurn && pos == 0) || (!isPlayerTurn && pos == playerGoal + 1) ? 2 : 1;
        return pos + (pos < 0 ? totalSpaces : 0);

    }

    public boolean isSideEmpty(int start, int goal) {
        for (int i = start; i < goal; i++) {
            if (spaces[i] > 0)
                return false;
        }
        return true;
    }

    public int getPebbles(int start, int goal) {
        int total = 0;
        for (int i = start; i < goal; i++) {
            total += spaces[i];
        }
        return total;
    }

    public boolean isGameOver() {
        return isSideEmpty(0, playerGoal) || isSideEmpty(playerGoal + 1, otherGoal);
    }

    public int getScore(boolean isGameOver) {
        int maxPlayerScore = spaces[playerGoal] + getPebbles(0, playerGoal);
        int maxOtherScore = spaces[otherGoal] + getPebbles(playerGoal + 1, otherGoal);
        int gameOverScore = maxPlayerScore - maxOtherScore + (2 * maxPlayerScore > allPebbleCount ? 10000 : -10000);
        return isGameOver ? gameOverScore : spaces[playerGoal] - spaces[otherGoal];
    }

    public boolean isWin() {
        int maxPlayerScore = spaces[playerGoal] + getPebbles(0, playerGoal);
        return 2 * maxPlayerScore >= allPebbleCount;
    }

    public boolean isAtGoal(int lastMove) {
        return lastMove == playerGoal || lastMove == otherGoal;
    }

    public int getAbsolutePosition(Move move) {
        return move.pos + (move.pickUpPlayerSide ? 0 : (rows + 1));
    }

    public void setRelativePosition(Move move, int absPos) {
        move.pickUpPlayerSide = absPos < playerGoal;
        move.pos = absPos - (move.pickUpPlayerSide ? 0 : playerGoal + 1);
    }

    public String format(int num) {
        return String.format("%2d", num);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(" -").append(spaces[otherGoal]).append("-\n");
        for (int i = 0; i < rows; i++) {
            b.append(format(spaces[i])).append(" ").append(format(spaces[playerGoal + rows - i])).append("\n");
        }
        b.append(" -").append(spaces[playerGoal]).append("-\n");
        return b.toString();
    }
}