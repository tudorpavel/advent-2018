import java.util.*;

public class Main {
    private static class Node {
        int score;
        Node next;

        public Node(int score, Node next) {
            this.score = score;
            this.next = next;
        }

        public void insertAfter(Node newNode) {
            newNode.next = next;
            next = newNode;
        }

        public Node nodeAfter(int steps) {
            if (steps == 0) {
                return this;
            }

            return next.nodeAfter(steps - 1);
        }

        public void printNext(int steps) {
            System.out.println(score);
            if (steps == 0) {
                return;
            }

            next.printNext(steps - 1);
        }
    }

    private static class Elf {
        Node currentNode;

        public Elf(Node node) {
            currentNode = node;
        }
    }

    private static void iterate(int recipeCount) {
        ArrayList<Elf> elves = new ArrayList();
        Node firstNode = new Node(3, null);
        Node secondNode = new Node(7, firstNode);
        firstNode.next = secondNode;
        elves.add(new Elf(firstNode));
        elves.add(new Elf(secondNode));
    }

    public static void main(String args[]) {
        // System.out.println("Part 1: ")
    }
}
