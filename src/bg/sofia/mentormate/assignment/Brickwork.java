package bg.sofia.mentormate.assignment;

import java.util.HashMap;
import java.util.Scanner;

public class Brickwork {

    HashMap<Integer, Cell> getCellsFromArray(int[][] firstLayer, int M, int N) {
        HashMap<Integer, Cell> cells = new HashMap<>();
        int i, j;
        int id = 0;
        for (i = 0; i < M; i++) {
            for (j = 0; j < N; j++) {
                Cell cell = new Cell(i, j, firstLayer[i][j]);
                cells.put(id, cell);
                id++;
            }
        }
        return cells;
    }

    boolean areTwoCellsNeighbours(Cell one, Cell two) {
        return one.getRow() == two.getRow() && one.getColumn() == two.getColumn() - 1 ||
                one.getRow() == two.getRow() && one.getColumn() == two.getColumn() + 1 ||
                one.getRow() == two.getRow() - 1 && one.getColumn() == two.getColumn() ||
                one.getRow() == two.getRow() + 1 && one.getColumn() == two.getColumn();
    }

    boolean[][] createAdjacencyMatrix(int[][] layer, int M, int N) {
        int cellsCount = M*N;
        HashMap<Integer, Cell> cells = getCellsFromArray(layer, M, N);
        boolean[][] matrix = new boolean[cellsCount][cellsCount];
        int i, j;
        for (i = 0; i < cellsCount; i++) {
            for (j = 0; j < cellsCount; j++) {
                Cell firstCell = cells.get(i);
                Cell secondCell = cells.get(j);
                if (firstCell.getValue() != secondCell.getValue() && areTwoCellsNeighbours(firstCell, secondCell)) {
                    matrix[i][j] = true;
                } else {
                    matrix[i][j] = false;
                }
            }
        }
        return matrix;
    }

    boolean bipartiteMatching(boolean[][] matrix, int matrixSize, int vertexFromSetOne,
                              boolean[] visited, int[] assigned)
    {
        int vertexFromSetTwo;
        for (vertexFromSetTwo = 0; vertexFromSetTwo < matrixSize; vertexFromSetTwo++)
        {

            if (matrix[vertexFromSetOne][vertexFromSetTwo] && !visited[vertexFromSetTwo])
            {
                visited[vertexFromSetTwo] = true;

                if (assigned[vertexFromSetTwo] < 0 || bipartiteMatching(matrix, matrixSize, assigned[vertexFromSetTwo],
                        visited, assigned))
                {
                    assigned[vertexFromSetTwo] = vertexFromSetOne;
                    return true;
                }
            }
        }
        return false;
    }

    int[] perfectBipartiteMatching(boolean[][] matrix, int matrixSize) {

        int[] assigned = new int[matrixSize];

        int i;
        for (i = 0; i < matrixSize; ++i)
            assigned[i] = -1;

        // Count of matches. If the number of matches is less
        // than the count of vertices then no solution exists.
        int result = 0;
        int vertexFromSetOne;
        for (vertexFromSetOne = 0; vertexFromSetOne < matrixSize; vertexFromSetOne++) {

            boolean[] visited = new boolean[matrixSize];
            for (i = 0; i < matrixSize; ++i)
                visited[i] = false;

            if (bipartiteMatching(matrix, matrixSize, vertexFromSetOne, visited, assigned)) {
                result++;
            }
        }

        if (result < matrixSize) {
            return null;
        }

        return assigned;
    }

    void printSolution(int[] assigned, int M, int N, int[][] firstLayer) {
        int[][] solution = new int[M][N];
        HashMap<Integer, Cell> cells = getCellsFromArray(firstLayer, M, N);
        int value = 1;
        int i;
        for (i = 0; i < (M * N); i++) {
            Cell firstHalfBrick = cells.get(i);
            Cell secondHalfBrick = cells.get(assigned[i]);
            if (solution[firstHalfBrick.getRow()][firstHalfBrick.getColumn()] == 0 &&
                    solution[secondHalfBrick.getRow()][secondHalfBrick.getColumn()] == 0) {
                solution[firstHalfBrick.getRow()][firstHalfBrick.getColumn()] = value;
                solution[secondHalfBrick.getRow()][secondHalfBrick.getColumn()] = value;
                value++;
            }
        }
        System.out.println("Second layer:");
        int j;
        for (i = 0; i < M; i++) {
            for (j = 0; j < N; j++) {
                System.out.print(solution[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    void brickwork(int[][] firstLayer, int M, int N) {
        boolean[][] matrix = createAdjacencyMatrix(firstLayer, M, N);
        int matrixSize = M * N;
        int[] assigned = perfectBipartiteMatching(matrix, matrixSize);
        if (assigned == null) {
            System.out.println("-1 Solution doesn't exist!");
            return;
        }
        printSolution(assigned, M, N, firstLayer);
    }

    public static void main (String[] args) {
        int M, N;
        Scanner in = null;
        try {
            in = new Scanner(System.in);

            System.out.println("Enter number of rows");
            M = in.nextInt();
            System.out.println("Enter number of columns");
            N = in.nextInt();

            int[][] firstLayer = new int[M][N];

            System.out.println("Enter first layer bricks");
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    firstLayer[i][j] = in.nextInt();
                }
            }

            Brickwork brickwork = new Brickwork();
            brickwork.brickwork(firstLayer, M, N);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception while reading input");
        }
    }
}