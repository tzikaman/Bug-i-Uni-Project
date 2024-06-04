//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

public class State {
    public static int rows;
    public static int cols;
    private char[][] layout;

    int[] getGoal() {
        int[] res = new int[2];

        for(int x = 0; x < rows; ++x) {
            for(int y = 0; y < cols; ++y) {
                if (this.layout[x][y] == 'g') {
                    res[0] = x;
                    res[1] = y;
                    return res;
                }
            }
        }

        return res;
    }

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        State.rows = rows;
    }

    public static int getCols() {
        return cols;
    }

    public static void setCols(int cols) {
        State.cols = cols;
    }

    public void set(int x, int y, char k) {
        this.layout[x][y] = k;
    }

    char get(int x, int y) {
        return this.layout[x][y];
    }

    boolean isEmpty(int x, int y) {
        return this.layout[x][y] == 'x';
    }

    public int getRobotX() {
        for(int x = 0; x < rows; ++x) {
            for(int y = 0; y < cols; ++y) {
                if (this.layout[x][y] == 'r') {
                    return x;
                }
            }
        }

        return 0;
    }

    public int getRobotY() {
        for(int x = 0; x < rows; ++x) {
            for(int y = 0; y < cols; ++y) {
                if (this.layout[x][y] == 'r') {
                    return y;
                }
            }
        }

        return 0;
    }

    State(int rows, int cols) {
        this.layout = new char[rows][cols];
        State.rows = rows;
        State.cols = cols;
    }
}
