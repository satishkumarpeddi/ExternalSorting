package ExternalSorting;

import java.util.*;

public class PolyPhaseMergeSortModel1 {

    // Each tape holds a list of runs (each run is a sorted ArrayList<Integer>)
    public static void distributeRuns(List<List<ArrayList<Integer>>> tapes, ArrayList<Integer> dataSet, int runSize) {
        int[] runsCount = {3, 2, 1}; // Example distribution (must sum to total runs)
        int idx = 0;
        for (int tape = 0; tape < tapes.size(); tape++) {
            for (int r = 0; r < runsCount[tape]; r++) {
                int start = idx * runSize;
                int end = Math.min(start + runSize, dataSet.size());
                if (start >= dataSet.size()) return;
                ArrayList<Integer> run = new ArrayList<>(dataSet.subList(start, end));
                run.sort(Integer::compareTo);
                tapes.get(tape).add(run);
                idx++;
                if (end == dataSet.size()) return;
            }
        }
    }

    // Merge runs from two input tapes into output tape
    public static void mergePhase(List<List<ArrayList<Integer>>> tapes, int outTape) {
        int inTape1 = (outTape + 1) % tapes.size();
        int inTape2 = (outTape + 2) % tapes.size();

        List<ArrayList<Integer>> input1 = tapes.get(inTape1);
        List<ArrayList<Integer>> input2 = tapes.get(inTape2);
        List<ArrayList<Integer>> output = new ArrayList<>();

        int i = 0, j = 0;

        // Merge run-by-run from input tapes
        while (i < input1.size() && j < input2.size()) {
            output.add(mergeRuns(input1.get(i), input2.get(j)));
            i++;
            j++;
        }

        // If any remaining runs (unpaired), add them directly
        while (i < input1.size()) {
            output.add(input1.get(i++));
        }
        while (j < input2.size()) {
            output.add(input2.get(j++));
        }

        // Replace output tape runs with merged runs
        tapes.get(outTape).clear();
        tapes.get(outTape).addAll(output);

        // Clear merged input tapes
        input1.clear();
        input2.clear();

        // Debug print tape states
        System.out.println("After mergePhase output tape " + outTape + ":");
        for (int t = 0; t < tapes.size(); t++) {
            System.out.print("Tape " + t + " runs: ");
            for (ArrayList<Integer> run : tapes.get(t)) {
                System.out.print(run + " ");
            }
            System.out.println();
        }
    }

    // Helper to merge two sorted runs into one sorted run
    public static ArrayList<Integer> mergeRuns(ArrayList<Integer> run1, ArrayList<Integer> run2) {
        ArrayList<Integer> merged = new ArrayList<>();
        int i = 0, j = 0;
        while (i < run1.size() && j < run2.size()) {
            if (run1.get(i) <= run2.get(j)) {
                merged.add(run1.get(i++));
            } else {
                merged.add(run2.get(j++));
            }
        }
        while (i < run1.size()) merged.add(run1.get(i++));
        while (j < run2.size()) merged.add(run2.get(j++));
        return merged;
    }

    public static ArrayList<Integer> polyPhaseSort(ArrayList<Integer> dataSet) {
        // Initialize tapes as lists of runs
        List<List<ArrayList<Integer>>> tapes = Arrays.asList(
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int runSize = 2;

        distributeRuns(tapes, dataSet, runSize);

        System.out.println("After distributeRuns:");
        for (int t = 0; t < tapes.size(); t++) {
            System.out.print("Tape " + t + " runs: ");
            for (ArrayList<Integer> run : tapes.get(t)) {
                System.out.print(run + " ");
            }
            System.out.println();
        }

        int outTape = 0;
        int mergeCount = 0;

        // Merge until only one tape remains with runs or max merges reached
        while (tapes.stream().filter(t -> !t.isEmpty()).count() > 1 && mergeCount < 10) {
            mergePhase(tapes, outTape);
            outTape = (outTape + 1) % tapes.size();
            mergeCount++;
        }

        // Flatten final runs into single sorted list
        for (List<ArrayList<Integer>> tape : tapes) {
            if (!tape.isEmpty()) {
                ArrayList<Integer> sorted = new ArrayList<>();
                for (ArrayList<Integer> run : tape) {
                    sorted.addAll(run);
                }
                return sorted;
            }
        }
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        ArrayList<Integer> dataSet = new ArrayList<>(List.of(7, 3, 5, 2, 1, 4, 5));
        ArrayList<Integer> result = polyPhaseSort(dataSet);
        System.out.println("Final sorted output: " + result);
    }
}
