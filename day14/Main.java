import java.util.*;

public class Main {
    private static class Node {
        static int nodeCount = 0;
        static Node firstNode;
        static Node lastNode;
        int score;
        Node next;

        public Node(int score) {
            this.score = score;
            nodeCount++;
        }

        public static Node makeFirstNode(int score) {
            nodeCount = 0;
            Node n = new Node(score);
            n.next = n;
            firstNode = n;
            lastNode = n;

            return n;
        }

        public static void insertLast(Node newNode) {
            newNode.next = lastNode.next;
            lastNode.next = newNode;
            lastNode = newNode;
        }

        public static void printFirst(int steps) {
            firstNode.printNext(steps);
        }

        public static void printSolutionAfter(int recipeCount) {
            Node n = firstNode.nodeAfter(recipeCount);
            n.printNext(10);
        }

        public void insertAfter(Node newNode) {
            newNode.next = next;
            next = newNode;
        }

        public Node nodeAfter(int steps) {
            Node n = this;
            for (int i = 0; i < steps; i++) {
                n = n.next;
            }

            return n;
        }

        public void printNext(int steps) {
            Node n = this;
            for (int i = 0; i < steps; i++) {
                System.out.print(n.score);
                n = n.next;
            }
        }
    }

    private static class Elf {
        Node currentNode;

        public Elf(Node node) {
            currentNode = node;
        }

        public void move() {
            currentNode = currentNode.nodeAfter(currentNode.score + 1);
        }
    }

    private static void iterate(ArrayList<Elf> elves) {
        int sum = 0;

        for (Elf elf: elves) {
            sum += elf.currentNode.score;
        }

        ArrayList<Integer> digits = new ArrayList();

        if (sum == 0) {
            digits.add(0);
        } else {
            while (sum > 0) {
                digits.add(sum % 10);
                sum /= 10;
            }
        }

        Collections.reverse(digits);

        for (Integer digit: digits) {
            Node.insertLast(new Node(digit));
        }

        for (Elf elf: elves) {
            elf.move();
        }
    }

    private static void iterateUntil(int recipeCount) {
        ArrayList<Elf> elves = new ArrayList();
        Node.makeFirstNode(3);
        Node.insertLast(new Node(7));
        elves.add(new Elf(Node.firstNode));
        elves.add(new Elf(Node.lastNode));

        while (Node.nodeCount < (recipeCount + 10)) {
            iterate(elves);
            // Node.printFirst(Node.nodeCount);
            // System.out.println();
        }
    }

    public static void main(String args[]) {
        final int RECIPE_COUNT = Integer.parseInt(args[0]);
        iterateUntil(RECIPE_COUNT);
        System.out.print("After " + RECIPE_COUNT + ": ");
        Node.printSolutionAfter(RECIPE_COUNT);
        System.out.println();
    }
}
