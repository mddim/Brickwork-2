package bg.sofia.mentormate.assignment;

import java.util.HashMap;
import java.util.Scanner;

public class Brickwork {

    public HashMap<Integer, Cell> getCellsFromArray(int[][] firstLayer, int M, int N) {
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

    public boolean areTwoCellsNeighbours(Cell one, Cell two) {
        return one.getRow() == two.getRow() && one.getColumn() == two.getColumn() - 1 ||
                one.getRow() == two.getRow() && one.getColumn() == two.getColumn() + 1 ||
                one.getRow() == two.getRow() - 1 && one.getColumn() == two.getColumn() ||
                one.getRow() == two.getRow() + 1 && one.getColumn() == two.getColumn();
    }

    public boolean[][] createAdjacencyMatrix(int[][] layer, int M, int N) {
        int cellsCount = M*N;
        HashMap<Integer, Cell> cells = getCellsFromArray(layer, M, N);
        boolean[][] matrix = new boolean[cellsCount][cellsCount];
        int i, j;
        for (i = 0; i < cellsCount; i++) {
            for (j = 0; j < cellsCount; j++) {
                Cell firstCell = cells.get(i);
                Cell secondCell = cells.get(j);
                if (firstCell.getValue() != secondCell.getValue() &&
                        areTwoCellsNeighbours(firstCell, secondCell)) {
                    matrix[i][j] = true;
                } else {
                    matrix[i][j] = false;
                }
            }
        }
        return matrix;
    }

    public boolean bipartiteMatching(boolean[][] matrix, int matrixSize, int vertexFromSetOne,
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

    public int[] perfectBipartiteMatching(boolean[][] matrix, int matrixSize) {

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

    public void printSolution(int[] assigned, int M, int N, int[][] firstLayer) {
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
            System.out.print("\n");
        }
    }

    public void brickwork(int[][] firstLayer, int M, int N) {
        boolean[][] matrix = createAdjacencyMatrix(firstLayer, M, N);
        int matrixSize = M * N;
        int[] assigned = perfectBipartiteMatching(matrix, matrixSize);
        if (assigned == null) {
            System.out.println("-1 Solution doesn't exist!");
            return;
        }
        printSolution(assigned, M, N, firstLayer);
    }

    private boolean validNeighbours(int[][] matrix, int x, int y, int M, int N) {
        int differentNeighbours = 0;
        int currentValue = matrix[x][y];
        if (y >= N - 1 || matrix[x][y + 1] != currentValue) {
            differentNeighbours++;
        }
        if (y <= 0 || matrix[x][y - 1] != currentValue) {
            differentNeighbours++;
        }
        if (x <= 0 || matrix[x - 1][y] != currentValue) {
            differentNeighbours++;
        }
        if (x >= M - 1 || matrix[x + 1][y] != currentValue) {
            differentNeighbours++;;
        }
        return differentNeighbours == 3;
    }

    private boolean validateInputLayer(int[][] layer, int M, int N) {
        for (int i = 1; i < M; i++) {
            for (int j = 1; j < N; j++) {
                if (!validNeighbours(layer, i, j, M, N)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main (String[] args) {
        int M, N;
        Scanner in = null;
        try {
            in = new Scanner(System.in);

            System.out.println("Enter number of rows");
            M = in.nextInt();
            while (M % 2 != 0) {
                System.out.println("Number of rows should be an even number\n" +
                        "Enter again");
                M = in.nextInt();
            }
            System.out.println("Enter number of columns");
            N = in.nextInt();
            while (N % 2 != 0) {
                System.out.println("Number of columns should be an even number\n" +
                        "Enter again");
                N = in.nextInt();
            }

            int[][] firstLayer = new int[M][N];

            System.out.println("Enter first layer bricks");
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    firstLayer[i][j] = in.nextInt();
                }
            }

            Brickwork brickwork = new Brickwork();

            while (!brickwork.validateInputLayer(firstLayer, M, N)) {
                System.out.println("Invalid layer\n" +
                        "Enter first layer bricks");
                for (int i = 0; i < M; i++) {
                    for (int j = 0; j < N; j++) {
                        firstLayer[i][j] = in.nextInt();
                    }
                }
            }

            brickwork.brickwork(firstLayer, M, N);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception while reading input");
        }
    }
}