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

        public boolean matchesDigits(ArrayList<Integer> digits) {
            Node n = this;

            for (Integer digit: digits) {
                if (n.score != digit || n == lastNode) {
                    return false;
                }
                n = n.next;
            }

            return true;
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

    private static int nodesUntilSequence(String scoreSequence) {
        ArrayList<Elf> elves = new ArrayList();
        Node.makeFirstNode(3);
        Node.insertLast(new Node(7));
        elves.add(new Elf(Node.firstNode));
        elves.add(new Elf(Node.lastNode));

        ArrayList<Integer> digits = new ArrayList();

        for (int i = 0; i < scoreSequence.length(); i++) {
            digits.add(scoreSequence.charAt(i) - '0');
        }

        Node node = Node.firstNode;
        int index = 0;

        while (!node.matchesDigits(digits)) {
            if (Node.nodeCount - index > digits.size()) {
                node = node.next;
                index++;
            }

            iterate(elves);
            // Node.printFirst(Node.nodeCount);
            // System.out.println();
        }

        return index;
    }

    public static void main(String args[]) {
        final String GIVEN_INPUT = args[0];

        // Part 1
        int inputNumber = Integer.parseInt(GIVEN_INPUT);
        iterateUntil(inputNumber);
        System.out.print("After " + inputNumber + ": ");
        Node.printSolutionAfter(inputNumber);
        System.out.println();

        // Part 2
        System.out.println("Found " + GIVEN_INPUT + " after: " + nodesUntilSequence(GIVEN_INPUT));
    }
}
