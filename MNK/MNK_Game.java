package MNK;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class MNK_Game implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    public Cell turn;
    private static int m;
    private static int n;
    private static int k;
    private static boolean read_again = true;

    public void MNK_Input() {
        Scanner scanner = new Scanner(System.in);
        if (read_again) {
            try {
                do {
                    System.out.println("Enter size of board (m,n) and length of victory's row (k)");
                    m = scanner.nextInt();
                    n = scanner.nextInt();
                    k = scanner.nextInt();
                } while (IncorrectInput(m, n, k));
            } catch (InputMismatchException inpEx) {
                System.out.println("Input is incorrect. Try again");
                MNK_Input();
            }
        }
        read_again = false;
    }

    public boolean IncorrectInput(int wight, int height, int victory) {
        if (victory > Math.max(wight, height)) {
            System.out.println("Victory's row can't be bigger than the biggest side of game board");
            return true;
        }
        if (victory < 1 || height < 1 || wight < 1) {
            System.out.println("Can't work with negative numbers and 0");
            return true;
        } else {
            return false;
        }
    }

    public static int getM() {
        return m;
    }

    public static int getN() {
        return n;
    }

    public MNK_Game() {
        if (read_again) {
            MNK_Input();
        }
        this.cells = new Cell[n][m];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }


    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();

        // Проверка на выигрыш по лучам от выбранной клетки

        int empty = 0;
        int pcol = move.getColumn();
        int prow = move.getRow();
        int[] mark = new int[8];
        for (int i = 0; i < mark.length; i++) {
            if (i != 4 && i != 5) {
                mark[i] = prow;
            } else {
                mark[i] = pcol;
            }
        }
        //проверка столбца
        while (prow - mark[0] < k && mark[1] - prow < k) {
            if (mark[0] > 0 && cells[mark[0] - 1][pcol] == turn) {
                mark[0]--;
            }
            if (mark[1] < n - 1 && cells[mark[1] + 1][pcol] == turn) {
                mark[1]++;
            }
            if ((mark[0] == 0 || cells[mark[0] - 1][pcol] != turn) && (mark[1] == n - 1 || cells[mark[1] + 1][pcol] != turn)) {
                break;
            }
        }
        //проверка гл.диаг
        while (prow - mark[2] < k && mark[3] - prow < k) {
            int dist1 = prow - mark[2];
            int dist2 = mark[3] - prow;
            if (prow > dist1 && pcol > dist1 && cells[prow - 1 - dist1][pcol - 1 - dist1] == turn) {
                mark[2]--;
            }
            if (prow + dist2 < n - 1 && pcol + dist2 < m - 1 && cells[prow + 1 + dist2][pcol + 1 + dist2] == turn) {
                mark[3]++;
            }
            if ((prow <= dist1 || pcol <= dist1 || cells[prow - 1 - dist1][pcol - 1 - dist1] != turn) && (prow + dist2 >= n - 1 || pcol + dist2 >= m - 1 || cells[prow + 1 + dist2][pcol + 1 + dist2] != turn)) {
                break;
            }
        }
        //проверка строки
        while (pcol - mark[4] < k && mark[5] - pcol < k) {
            if (mark[4] > 0 && cells[prow][mark[4] - 1] == turn) {
                mark[4]--;
            }
            if (mark[5] < m - 1 && cells[prow][mark[5] + 1] == turn) {
                mark[5]++;
            }
            if ((mark[4] == 0 || cells[prow][mark[4] - 1] != turn) && (mark[5] == m - 1 || cells[prow][mark[5] + 1] != turn)) {
                break;
            }
        }
        //проверка поб.диаг
        while (prow - mark[7] < k && mark[6] - prow < k) {
            int dist1 = mark[6] - prow;
            int dist2 = prow - mark[7];
            if (prow + dist1 < n - 1 && pcol > dist1 && cells[prow + 1 + dist1][pcol - 1 - dist1] == turn) {
                mark[6]++;
            }
            if (prow > dist2 && pcol + dist2 < m - 1 && cells[prow - 1 - dist2][pcol + 1 + dist2] == turn) {
                mark[7]--;
            }
            if ((prow + dist1 >= n - 1 || pcol <= dist1 || cells[prow + 1 + dist1][pcol - 1 - dist1] != turn) && (prow <= dist2 || pcol + dist2 >= m - 1 || cells[prow - 1 - dist2][pcol + 1 + dist2] != turn)) {
                break;
            }
        }
        int temp = mark[6];
        mark[6] = mark[7];
        mark[7] = temp;
        for (int j = 0; j < mark.length - 1; j += 2) {
            if (mark[j + 1] - mark[j] + 1 >= k) {
                turn = turn == Cell.X ? Cell.O : Cell.X;
                return Result.WIN;
            }
        }
        for (int u = 0; u < n; u++) {
            for (int v = 0; v < m; v++) {
                if (cells[u][v] == Cell.E) {
                    empty++;
                }
            }
        }
        if (empty == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < n
                && 0 <= move.getColumn() && move.getColumn() < m
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        int i = 0;
        StringBuilder sb = new StringBuilder(" ");
        while (i < m) {
            sb.append(i % 10);
            i++;
        }
        for (int r = 0; r < n; r++) {
            sb.append("\n");
            sb.append(r % 10);
            for (int c = 0; c < m; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
            }
        }
        return sb.toString();
    }
}
