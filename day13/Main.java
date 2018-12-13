import java.util.*;
import java.io.*;

public class Main {
    private static class Cart {
        int x;
        int y;
        char direction; // one of ['^','>','v','<']
        int turnCount;

        public Cart(int x, int y, char direction, int turnCount) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.turnCount = turnCount;
        }

        public String toString() {
            return String.format("{ x: %d, y: %d, direction: %s }",
                                 x, y, direction);
        }

        public Cart nextGen(char[][] grid) {
            int nextX = x;
            int nextY = y;
            char nextDirection = direction;
            int nextTurnCount = turnCount;

            switch (grid[y][x]) {
            case '|':
                if (direction == '^') {
                    nextY--;
                } else {
                    nextY++;
                }
                break;
            case '-':
                if (direction == '>') {
                    nextX++;
                } else {
                    nextX--;
                }
                break;
            case '/':
                switch (direction) {
                case '^':
                    nextX++;
                    nextDirection = '>';
                    break;
                case '>':
                    nextY--;
                    nextDirection = '^';
                    break;
                case 'v':
                    nextX--;
                    nextDirection = '<';
                    break;
                case '<':
                    nextY++;
                    nextDirection = 'v';
                    break;
                }
                break;
            case '\\':
                switch (direction) {
                case '^':
                    nextX--;
                    nextDirection = '<';
                    break;
                case '>':
                    nextY++;
                    nextDirection = 'v';
                    break;
                case 'v':
                    nextX++;
                    nextDirection = '>';
                    break;
                case '<':
                    nextY--;
                    nextDirection = '^';
                    break;
                }
                break;
            default: // '+'
                nextTurnCount = turnCount + 1;

                if (turnCount % 3 == 0) {
                    switch (direction) {
                    case '^':
                        nextX--;
                        nextDirection = '<';
                        break;
                    case '>':
                        nextY--;
                        nextDirection = '^';
                        break;
                    case 'v':
                        nextX++;
                        nextDirection = '>';
                        break;
                    case '<':
                        nextY++;
                        nextDirection = 'v';
                        break;
                    }
                } else if (turnCount % 3 == 1) {
                    switch (direction) {
                    case '^':
                        nextY--;
                        break;
                    case '>':
                        nextX++;
                        break;
                    case 'v':
                        nextY++;
                        break;
                    case '<':
                        nextX--;
                        break;
                    }
                } else {
                    switch (direction) {
                    case '^':
                        nextX++;
                        nextDirection = '>';
                        break;
                    case '>':
                        nextY++;
                        nextDirection = 'v';
                        break;
                    case 'v':
                        nextX--;
                        nextDirection = '<';
                        break;
                    case '<':
                        nextY--;
                        nextDirection = '^';
                        break;
                    }
                }
            }

            return new Cart(nextX, nextY, nextDirection, nextTurnCount);
        }
    }

    private static ArrayList<Cart> nextGen(char[][] grid, ArrayList<Cart> carts) {
        ArrayList<Cart> newCarts = new ArrayList();

        for (Cart cart: carts) {
            newCarts.add(cart.nextGen(grid));
        }

        return newCarts;
    }

    private static boolean noCollisions(ArrayList<Cart> carts) {
        for (int i = 0; i < carts.size() - 1; i++) {
            for (int j = i + 1; j < carts.size(); j++) {
                Cart c1 = carts.get(i);
                Cart c2 = carts.get(j);

                if (c1.x == c2.x && c1.y == c2.y) {
                    return false;
                }
            }
        }

        return true;
    }

    private static Cart firstCrash(ArrayList<Cart> carts) {
        int minY = Integer.MAX_VALUE;
        int minX = Integer.MAX_VALUE;
        Cart firstCrash = carts.get(0);

        for (int i = 0; i < carts.size() - 1; i++) {
            for (int j = i + 1; j < carts.size(); j++) {
                Cart c1 = carts.get(i);
                Cart c2 = carts.get(j);

                if (c1.x == c2.x && c1.y == c2.y) {
                    if (c1.y < minY) {
                        minY = c1.y;
                        minX = c1.x;
                        firstCrash = c1;
                    } else if (c1.y == minY && c1.x < minX) {
                        minX = c1.x;
                        firstCrash = c1;
                    }
                }
            }
        }

        return firstCrash;
    }

    private static ArrayList<Cart> simulateUntilCrash(char[][] grid, ArrayList<Cart> carts) {
        System.out.println(carts);

        while (noCollisions(carts)) {
            carts = nextGen(grid, carts);
            System.out.println(carts);
        }

        return carts;
    }

    private static void printGrid(char[][] grid) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        ArrayList<ArrayList<Character>> inputMatrix = new ArrayList<>();
        ArrayList<Cart> carts = new ArrayList<>();

        while (in.hasNext()) {
            String s = in.nextLine();
            ArrayList<Character> row = new ArrayList<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                if (c == '^' || c == '>' || c == 'v' || c == '<') {
                    carts.add(new Cart(i, inputMatrix.size(), c, 0));

                    if (c == '^' || c == 'v') {
                        row.add('|');
                    } else {
                        row.add('-');
                    }
                } else {
                    row.add(s.charAt(i));
                }
            }

            inputMatrix.add(row);
        }

        char[][] grid = new char[inputMatrix.size()][inputMatrix.get(0).size()];

        for (int y = 0; y < inputMatrix.size(); y++) {
            for (int x = 0; x < inputMatrix.get(0).size(); x++) {
                grid[y][x] = inputMatrix.get(y).get(x);
            }
        }

        ArrayList<Cart> cartsPart1 = simulateUntilCrash(grid, (ArrayList)carts.clone());
        Cart firstCrash = firstCrash(cartsPart1);
        System.out.println("Part 1: " + firstCrash.x + "," + firstCrash.y);
    }
}
