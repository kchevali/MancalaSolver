
class Move {
    int firstPos, pos, lastMove;// relative position
    int score;
    int[] changes;
    boolean isAlphaTurn, nextTurn, pickUpAlphaSide;

    public Move(int pos, boolean isAlphaTurn, int length) {
        this.firstPos = this.pos = this.lastMove = pos;
        this.isAlphaTurn = this.pickUpAlphaSide = isAlphaTurn;
        this.changes = new int[length];
    }

    public Move(boolean isAlphaTurn, int score) {
        this.pos = this.lastMove = -1;
        this.isAlphaTurn = this.pickUpAlphaSide = isAlphaTurn;
        this.changes = null;

        this.score = score;
    }

    public void delta(int absPos, int count) {
        changes[absPos] += count;
        lastMove = absPos;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNextTurn(boolean isAtGoal) {
        this.nextTurn = (isAlphaTurn == isAtGoal);
    }

}