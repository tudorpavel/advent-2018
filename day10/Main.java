import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.io.*;

import javax.imageio.ImageIO;

public class Main {
    private static final int MAGIC_STEP_COUNT = 10;

    private static class Points {
        private static final int MAGIC_NORMALIZE_PADDING = 10;

        ArrayList<Point> points = new ArrayList<>();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        public void add(Point p) {
            points.add(p);

            if (p.x < minX) { minX = p.x; }
            if (p.y < minY) { minY = p.y; }
            if (p.x > maxX) { maxX = p.x; }
            if (p.y > maxY) { maxY = p.y; }
        }

        public void normalizeCoordinates() {
            int xIncrement = 0;
            int yIncrement = 0;
            int inc = 0;

            if (minX < 0) { xIncrement = (- minX) + MAGIC_NORMALIZE_PADDING; }
            if (minY < 0) { yIncrement = (- minY) + MAGIC_NORMALIZE_PADDING; }
            if (xIncrement > yIncrement) {
                inc = xIncrement;
            } else {
                inc = yIncrement;
            }

            minX += inc;
            maxX += inc;
            minY += inc;
            maxY += inc;

            for (Point p: points) {
                p.x += inc;
                p.y += inc;
            }
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

        points.normalizeCoordinates();

        // System.out.println(points.minX);
        // System.out.println(points.maxX);
        // System.out.println(points.minY);
        // System.out.println(points.maxY);
        // System.out.println(points);

        try {
            int width = points.maxX, height = points.maxY;

            for (int i = 0; i < MAGIC_STEP_COUNT; i++) {
                // TYPE_INT_ARGB specifies the image format: 8-bit RGBA packed
                // into integer pixels
                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                Graphics2D ig2 = bi.createGraphics();

                ig2.setPaint(Color.red);

                for (Point p: points.points) {
                    ig2.drawLine(p.x, p.y, p.x, p.y);
                    p.moveStep();
                }

                ImageIO.write(bi, "PNG", new File("out/" + i + ".png"));
            }

            System.out.println("Images written in ./out folder.");
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
