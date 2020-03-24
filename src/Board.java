import java.util.*;

class Board {

    // 0 -> (totalSpaces-1) : board
    // totalSpaces : AlphaScore | totalSpaces + 1 : betaScore
    int[] spaces;
    int rows, totalSpaces, alphaGoal, betaGoal;
    int allPebbleCount;

    // Alpha: top -> bottom
    // beta: bottom -> top

    public Board(int rows, int pebbleCount) {
        this.rows = rows;
        this.totalSpaces = 2 * rows + 2;
        spaces = new int[totalSpaces];// +2 for score

        for (int i = 0; i < totalSpaces; i++) {
            spaces[i] = pebbleCount;
        }

        this.alphaGoal = rows;
        this.betaGoal = totalSpaces - 1;

        this.spaces[alphaGoal] = this.spaces[betaGoal] = 0;// score
        this.allPebbleCount = pebbleCount * this.rows * 2;
    }

    public Move createMove(int pos, boolean isAlphaTurn) {
        return new Move(pos, isAlphaTurn, totalSpaces);
    }

    public void randomize() {
        int length = rows * 2;
        int[] nums = new int[length + 1];
        nums[0] = 0;
        nums[length] = allPebbleCount;
        for (int i = 1; i < length; i++)
            nums[i] = (int) (Math.random() * (allPebbleCount + 1));
        Arrays.sort(nums);
        for (int i = 0, pos = 0; i < length; i++, pos++) {
            if (pos == alphaGoal)
                pos++;
            spaces[pos] = nums[i + 1] - nums[i];
        }
    }

    public int acrossIndex(int absPos) {
        return totalSpaces - absPos - 2;
    }

    public Move move(int pos, boolean isAlphaTurn) {
        Move move = createMove(pos, isAlphaTurn);
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

        absPos = nextPosition(move.isAlphaTurn, absPos);
        for (int i = 0; i < pebbles; i++) {
            step(move, absPos, 1);
            absPos = nextPosition(move.isAlphaTurn, absPos);
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

    public int nextPosition(boolean isAlphaTurn, int pos) {
        pos += (isAlphaTurn && pos == betaGoal - 1) || (!isAlphaTurn && pos == alphaGoal - 1) ? 2 : 1;
        return pos - (pos >= totalSpaces ? totalSpaces : 0);
    }

    public int prevPosition(boolean isAlphaTurn, int pos) {
        pos -= (isAlphaTurn && pos == 0) || (!isAlphaTurn && pos == alphaGoal + 1) ? 2 : 1;
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
        return isSideEmpty(0, alphaGoal) || isSideEmpty(alphaGoal + 1, betaGoal);
    }

    public int getScore(boolean isGameOver) {
        int maxAlphaScore = spaces[alphaGoal] + getPebbles(0, alphaGoal);
        int maxBetaScore = spaces[betaGoal] + getPebbles(alphaGoal + 1, betaGoal);
        int gameOverScore = maxAlphaScore - maxBetaScore + (2 * maxAlphaScore > allPebbleCount ? 10000 : -10000);
        return isGameOver ? gameOverScore : spaces[alphaGoal] - spaces[betaGoal];
    }

    public void addUpBoard() {
        spaces[alphaGoal] += getPebbles(0, alphaGoal);
        spaces[betaGoal] += getPebbles(alphaGoal + 1, betaGoal);
        for (int i = 0; i < totalSpaces; i++) {
            if (i != alphaGoal && i != betaGoal)
                spaces[i] = 0;
        }
    }

    public boolean isWin() {
        int maxAlphaScore = spaces[alphaGoal] + getPebbles(0, alphaGoal);
        return 2 * maxAlphaScore >= allPebbleCount;
    }

    public boolean isAtGoal(int lastMove) {
        return lastMove == alphaGoal || lastMove == betaGoal;
    }

    public int getAbsolutePosition(int pos, boolean pickUpAlphaSide) {
        return pos + (pickUpAlphaSide ? 0 : (rows + 1));
    }

    public int getAbsolutePosition(Move move) {
        return getAbsolutePosition(move.pos, move.pickUpAlphaSide);
    }

    public void setRelativePosition(Move move, int absPos) {
        move.pickUpAlphaSide = absPos < alphaGoal;
        move.pos = absPos - (move.pickUpAlphaSide ? 0 : alphaGoal + 1);
    }

    public String format(int num) {
        return String.format("%2d", num);
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(" -").append(spaces[betaGoal]).append("-\n");
        for (int i = 0; i < rows; i++) {
            b.append(format(spaces[i])).append(" ").append(format(spaces[alphaGoal + rows - i])).append("\n");
        }
        b.append(" -").append(spaces[alphaGoal]).append("-\n");
        return b.toString();
    }
}