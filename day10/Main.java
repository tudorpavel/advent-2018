import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.io.*;

public class Main {
    private static final int MAGIC_DIVISOR = 4;
    private static final int MAGIC_STEP_COUNT = 5;

    private static class Points {
        static int steps = 0;

        ArrayList<Point> points = new ArrayList<>();
        int minX;
        int minY;
        int maxX;
        int maxY;

        public void add(Point p) {
            points.add(p);
        }

        public void recomputeBoundaries() {
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;

            for (Point p: points) {
                if (p.x < minX) { minX = p.x; }
                if (p.y < minY) { minY = p.y; }
                if (p.x > maxX) { maxX = p.x; }
                if (p.y > maxY) { maxY = p.y; }
            }

            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public void moveStep() {
            steps++;

            for (Point p: points) {
                p.moveStep();
            }

            recomputeBoundaries();
        }

        public String toString() {
            return points.stream()
                .map(Point::toString)
                .collect(Collectors.joining("\n"));
        }
    }

    private static class Point {
        int x;
        int y;
        int velocityX;
        int velocityY;

        public Point(int x, int y, int velocityX, int velocityY) {
            this.x = x;
            this.y = y;
            this.velocityX = velocityX;
            this.velocityY = velocityY;
        }

        public void moveStep() {
            x += velocityX;
            y += velocityY;
        }

        public String toString() {
            return String.format("position=<%d, %d> velocity=<%d, %d>", x, y, velocityX, velocityY);
        }
    }

    static public void main(String args[]) throws Exception {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        Pattern pattern = Pattern.compile("position=< *(-?\\d+), *(-?\\d+)> velocity=< *(-?\\d+), *(-?\\d+)>");
        var points = new Points();

        while (in.hasNext()) {
            Matcher matcher = pattern.matcher(in.nextLine());
            while (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int vX = Integer.parseInt(matcher.group(3));
                int vY = Integer.parseInt(matcher.group(4));
                points.add(new Point(x, y, vX, vY));
            }
        }

        points.recomputeBoundaries();

        if (points.maxX - points.minX > points.points.size()) {
            while (points.maxX - points.minX > points.points.size() / MAGIC_DIVISOR
                   || points.maxY - points.minY > points.points.size() / MAGIC_DIVISOR) {
                points.moveStep();
            }
        }

        System.out.println("Initial steps: " + points.steps);
        System.out.println("--- Add result file index to steps for Part 2 ---");

        for (int i = 0; i < MAGIC_STEP_COUNT; i++) {
            String fileName = "out/" + i + ".txt";
            new File(fileName);
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (int y = points.minY; y <= points.maxY; y++) {
                for (int x = points.minX; x <= points.maxX; x++) {
                    var found = false;

                    for (Point p: points.points) {
                        if (p.x == x && p.y == y) {
                            printWriter.print("X");
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        printWriter.print(" ");
                    }
                }
                printWriter.print("\n");
            }

            points.moveStep();

            printWriter.close();
        }

        System.out.println("Files written in ./out folder.");
    }
}
