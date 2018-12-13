import java.util.*;
import java.util.regex.*;
import java.util.stream.*;
import java.io.*;

public class Main {
    private static ArrayList<String> notes = new ArrayList<>();
    private static int minIndex;
    private static int maxIndex;

    private static LinkedHashMap<Integer, Character> nextGen(LinkedHashMap<Integer, Character> pots) {
        LinkedHashMap<Integer, Character> next = new LinkedHashMap<>();
        int newMin = 0;
        int newMax = 0;
        boolean minSet = false;

        for (int i = minIndex - 2; i <= maxIndex + 2; i++) {
            String s = "";
            s += pots.get(i - 2) == null ? "." : "#";
            s += pots.get(i - 1) == null ? "." : "#";
            s += pots.get(i) == null ? "." : "#";
            s += pots.get(i + 1) == null ? "." : "#";
            s += pots.get(i + 2) == null ? "." : "#";

            for (String note: notes) {
                String substr = note.substring(0, 5);

                if (s.equals(substr)) {
                    if (!minSet) {
                        newMin = i;
                        minSet = true;
                    }
                    next.put(i, '#');
                    newMax = i;
                }
            }
        }

        minIndex = newMin;
        maxIndex = newMax;

        return next;
    }

    public static void main(String args[]) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        Pattern pattern = Pattern.compile("initial state: ([#\\.]+)");

        LinkedHashMap<Integer, Character> pots = new LinkedHashMap<>();

        if (in.hasNext()) {
            Matcher matcher = pattern.matcher(in.nextLine());
            while (matcher.find()) {
                String s = matcher.group(1);

                boolean minSet = false;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == '#') {
                        if (!minSet) {
                            minIndex = i;
                            minSet = true;
                        }
                        pots.put(i, s.charAt(i));
                        maxIndex = i;
                    }
                }
            }
        }


        if (in.hasNext()) {
            in.nextLine();
        }

        while (in.hasNext()) {
            String note = in.nextLine();

            if (note.charAt(9) == '#') {
                notes.add(note);
            }
        }

        // System.out.println(pots);
        for (int i = 0; i < 20; i++) {
            pots = nextGen(pots);
            // System.out.println(pots);
        }

        int sum = 0;
        for (int n: pots.keySet()) {
            sum += n;
        }
        System.out.println("Part 1: " + sum);

        // Okay so Part 2 result for 5000 generations is 405195
        // Okay so Part 2 result for 50000 generations is 4050195
        // Okay so Part 2 result for 500000 generations is 40500195
        // Okay so Part 2 result for 5000000 generations is 405000195
        // ...
        // So maybe Part 2 result for 50000000000 generations is 4050000000195?
        final int GENERATION_COUNT = 5000000;
        for (int i = 0; i < GENERATION_COUNT; i++) {
            pots = nextGen(pots);
        }

        sum = 0;
        for (int n: pots.keySet()) {
            sum += n;
        }
        System.out.println("Generation count: " + GENERATION_COUNT);
        System.out.println("Part 2: " + sum);
    }
}
