
class Move {
    int firstPos, pos, lastMove;// relative position
    int score;
    int[] changes;
    boolean isPlayerTurn, nextTurn, pickUpPlayerSide;

    public Move(int pos, boolean isPlayerTurn, int length) {
        this.firstPos = this.pos = this.lastMove = pos;
        this.isPlayerTurn = this.pickUpPlayerSide = isPlayerTurn;
        this.changes = new int[length];
    }

    public Move(boolean isPlayerTurn) {
        this.pos = this.lastMove = -1;
        this.isPlayerTurn = this.pickUpPlayerSide = isPlayerTurn;
        this.changes = null;

        score = isPlayerTurn ? Integer.MIN_VALUE / 2 : Integer.MAX_VALUE / 2;
    }

    public void delta(int absPos, int count) {
        changes[absPos] += count;
        lastMove = absPos;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNextTurn(boolean isAtGoal) {
        this.nextTurn = (isPlayerTurn == isAtGoal);
    }

}