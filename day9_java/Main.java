import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Main {
    private static class Node {
        int marble;
        Node left;
        Node right;

        public Node(int marble) {
            this.marble = marble;
        }

        public Node moveLeft(int index) {
            if (index == 0) return this;

            return left.moveLeft(index - 1);
        }

        public Node moveRight(int index) {
            if (index == 0) return this;

            return right.moveRight(index - 1);
        }

        public Node insert(Node newNode) {
            left.right = newNode;
            newNode.left = left;
            newNode.right = this;
            left = newNode;

            return newNode;
        }

        public Node delete() {
            left.right = right;
            right.left = left;

            return right;
        }
    }

    private static long highScore(int playerCount, int marbleCount) {
        var playerScores = new long[playerCount];
        Node cursor = new Node(0);
        cursor.left = cursor;
        cursor.right = cursor;

        for (int i = 0; i < marbleCount; i++) {
            if (i % 23 == 0) {
                cursor = cursor.moveLeft(7);
                playerScores[i % playerCount] += i + cursor.marble;
                cursor = cursor.delete();
            } else {
                cursor = cursor.moveRight(2);
                cursor = cursor.insert(new Node(i));
            }
        }

        return Arrays.stream(playerScores).max().getAsLong();
    }

    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        Pattern pattern = Pattern.compile("(\\d+) players; last marble is worth (\\d+) points");
        int playerCount = 0;
        int marbleCount = 0;

        while (in.hasNext()) {
            Matcher matcher = pattern.matcher(in.nextLine());
            while (matcher.find()) {
                playerCount = Integer.parseInt(matcher.group(1));
                marbleCount = Integer.parseInt(matcher.group(2));
            }
        }

        System.out.println("Part 1: " + highScore(playerCount, marbleCount));
        System.out.println("Part 2: " + highScore(playerCount, 100 * marbleCount));
    }
}
