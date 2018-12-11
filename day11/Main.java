import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.io.*;

public class Main {
    private static final int GRID_SIZE = 300;

    static private int powerFor(int x, int y, int serialNumber) {
        int rackId = x + 10;
        int powerLevel = rackId * y;
        powerLevel += serialNumber;
        powerLevel *= rackId;
        powerLevel = (powerLevel % 1000) / 100;

        return powerLevel - 5;
    }
    static public void main(String args[]) throws Exception {
        Scanner in = new Scanner(System.in);
        int serialNumber = in.nextInt();
        int[][] grid = new int[GRID_SIZE][GRID_SIZE];

        for (int y = 0; y < GRID_SIZE; y++) {
            for (int x = 0; x < GRID_SIZE; x++) {
                grid[y][x] = powerFor(x+1, y+1, serialNumber);
            }
        }

        int maxPower = 0;
        int maxX = 0;
        int maxY = 0;
        int maxSquareSize = 0;

        // For Part1 use `squareSize <= 3`
        for (int squareSize = 1; squareSize <= GRID_SIZE; squareSize++) {
            for (int y = 0; y <= GRID_SIZE - squareSize; y++) {
                for (int x = 0; x <= GRID_SIZE - squareSize; x++) {
                    int sum = 0;

                    for (int yy = y; yy < y + squareSize; yy++) {
                        for (int xx = x; xx < x + squareSize; xx++) {
                            sum += grid[yy][xx];
                        }
                    }

                    if (sum > maxPower) {
                        maxPower = sum;
                        maxX = x;
                        maxY = y;
                        maxSquareSize = squareSize;
                    }
                }
            }
        }

        System.out.println(String.format("Part 1: %d,%d", maxX+1, maxY+1));
        System.out.println(String.format("Part 2: %d,%d,%d", maxX+1, maxY+1, maxSquareSize));
    }
}
