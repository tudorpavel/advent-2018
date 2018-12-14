import java.util.*;
import java.io.*;

public class Main {
    private static class Cart implements Comparable<Cart> {
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

        public int compareTo(Cart c) {
            int yCompare = new Integer(y).compareTo(new Integer(c.y));
            return (yCompare != 0 ? yCompare : new Integer(x).compareTo(new Integer(c.x)));
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
        for (int i = 0; i < carts.size() - 1; i++) {
            for (int j = i + 1; j < carts.size(); j++) {
                Cart c1 = carts.get(i);
                Cart c2 = carts.get(j);

                if (c1.x == c2.x && c1.y == c2.y) {
                    return c1;
                }
            }
        }

        return carts.get(0);
    }

    private static ArrayList<Cart> removeImminentCollisions(char[][] grid, ArrayList<Cart> carts) {
        ArrayList<Cart> newCarts = new ArrayList();
        ArrayList<Integer> collisionIndexes = new ArrayList();

        Collections.sort(carts);

        for (int i = 0; i < carts.size() - 1; i++) {
            for (int j = i + 1; j < carts.size(); j++) {
                if (collisionIndexes.contains(i) ||
                    collisionIndexes.contains(j)) {
                    continue;
                }

                Cart c1 = carts.get(i);
                Cart c2 = carts.get(j);
                Cart c1Next = carts.get(i).nextGen(grid); // look ahead
                Cart c2Next = carts.get(j).nextGen(grid); // look ahead

                if ((c1.x == c2Next.x && c1.y == c2Next.y)
                    || (c1Next.x == c2.x && c1Next.y == c2.y)
                    || (c1Next.x == c2Next.x && c1Next.y == c2Next.y)) {
                    System.out.println("To remove (" + i + "): " + c1);
                    System.out.println("To remove (" + j + "): " + c2);
                    collisionIndexes.add(i);
                    collisionIndexes.add(j);
                    System.out.println(collisionIndexes);
                }
            }
        }

        for (int i = 0; i < carts.size(); i++) {
            if (!collisionIndexes.contains(i)) {
                newCarts.add(carts.get(i));
            }
        }

        return newCarts;
    }

    private static ArrayList<Cart> simulateUntilCrash(char[][] grid, ArrayList<Cart> carts) {
        // System.out.println(carts);

        while (noCollisions(carts)) {
            carts = nextGen(grid, carts);
            // System.out.println(carts);
        }

        return carts;
    }

    private static ArrayList<Cart> simulateWithRemoval(char[][] grid, ArrayList<Cart> carts) {
        // System.out.println(carts);

        while (carts.size() > 1) {
            carts = removeImminentCollisions(grid, carts);
            carts = nextGen(grid, carts);
            // System.out.println(carts);
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

        System.out.println("Carts count: " + carts.size());

        ArrayList<Cart> cartsPart1 = simulateUntilCrash(grid, (ArrayList)carts.clone());
        Cart firstCrash = firstCrash(cartsPart1);
        System.out.println("Part 1: " + firstCrash.x + "," + firstCrash.y);

        ArrayList<Cart> cartsPart2 = simulateWithRemoval(grid, (ArrayList)carts.clone());
        System.out.println("Part 2: " + cartsPart2.get(0).x + "," + cartsPart2.get(0).y);
    }
}
