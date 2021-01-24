import bg.sofia.mentormate.assignment.Brickwork;
import bg.sofia.mentormate.assignment.Cell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BrickworkTest {
    private Brickwork brickwork = new Brickwork();
    int M = 2;
    int N = 2;
    int[][] layer = new int[][]{
            {1, 1},
            {2, 2}
    };

    @Test
    public void testGetCellsFromArray() {
        HashMap<Integer, Cell> cellHashMap = new HashMap<>();

        Cell cell1 = new Cell(0 ,0, 1);
        Cell cell2 = new Cell(0 ,1, 1);
        Cell cell3 = new Cell(1 ,0, 2);
        Cell cell4 = new Cell(1 ,1, 2);

        cellHashMap.put(0, cell1);
        cellHashMap.put(1, cell2);
        cellHashMap.put(2, cell3);
        cellHashMap.put(3, cell4);

        assertEquals(cellHashMap, brickwork.getCellsFromArray(layer, M, N));
    }

    @Test
    void testAreTwoCellsNeighboursHorizontal() {
        Cell one = new Cell(0, 0, 0);
        Cell two = new Cell(0, 1, 0);

        assertTrue(brickwork.areTwoCellsNeighbours(one, two));
    }

    @Test
    void testAreTwoCellsNeighboursVerticalTrue() {
        Cell one = new Cell(0, 0, 0);
        Cell two = new Cell(1, 0, 0);

        assertTrue(brickwork.areTwoCellsNeighbours(one, two));
    }

    @Test
    void testAreTwoCellsNeighboursVerticalFalse() {
        Cell one = new Cell(0, 0, 0);
        Cell two = new Cell(2, 0, 0);

        assertFalse(brickwork.areTwoCellsNeighbours(one, two));
    }

    @Test
    void testCreateAdjacencyMatrix() {
        boolean[][] adjacencyMatrix = new boolean[][] {
                {false, false, true, false},
                {false, false, false, true},
                {true, false, false, false},
                {false, true, false, false}
        };
        int i;
        for (i = 0; i < M; i++) {
            assertArrayEquals(adjacencyMatrix[i], brickwork.createAdjacencyMatrix(layer, M, N)[i]);
        }
    }

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setStreams() {
        System.setOut(new PrintStream(out));
    }

    @AfterEach
    public void restoreInitialStreams() {
        System.setOut(originalOut);
    }

    //Contents have differences only in line separators (Win/Linux) -> Fix
    @Test
    void testBrickworkSmall() {
        brickwork.brickwork(layer, M, N);
        String solution = "Second layer:\n1 2 \n1 2";
        assertEquals(solution, out.toString().trim());
    }

    @Test
    void testBrickworkSmall2() {
        int M = 2;
        int N = 4;
        int[][] layer = new int[][] {
                {1, 1, 2, 2},
                {3, 3, 4, 4}
        };
        brickwork.brickwork(layer, M, N);
        String solution = "Second layer:\n1 2 3 4 \n1 2 3 4";
        assertEquals(solution, out.toString().trim());
    }

    @Test
    void testBrickworkMedium() {
        int M = 2;
        int N = 8;
        int[][] layer = new int[][] {
                {1, 1, 2, 2, 6, 5, 5, 8},
                {3, 3, 4, 4, 6, 7, 7, 8}
        };
        brickwork.brickwork(layer, M, N);
        String solution = "Second layer:\n" +
                            "1 2 3 4 4 5 6 6\n" +
                            "1 2 3 7 7 5 8 8 ";
        assertEquals(solution, out.toString().trim());
    }

    @Test
    void testBrickworkLarge() {
        int M = 4;
        int N = 8;
        int[][] layer = new int[][] {
                {1, 2, 2, 12, 5, 7, 7, 16},
                {1, 10, 10, 12, 5, 15, 15, 16},
                {9, 9, 3, 4, 4, 8, 8, 14},
                {11, 11, 3, 13, 13, 6, 6, 14}
        };
        brickwork.brickwork(layer, M, N);
        String solution = "Second layer:\n" +
                "1 1 2 3 3 7 4 4 \n" +
                "5 5 2 6 6 7 8 9 \n" +
                "10 11 11 12 13 14 8 9 \n" +
                "10 15 15 12 13 14 16 16";
        assertEquals(solution, out.toString().trim());
    }
}