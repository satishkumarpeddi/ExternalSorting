package ExternalSorting;
import java.util.*;
public class PolyphaseMergeSort {
    public static List<Integer> polyphaseMergeSort(List<Integer> data){
        List<List<Integer>> runs = new ArrayList<>();
        for(int i=0;i<data.size();i++){
            List<Integer> run = new ArrayList<>(data.subList(i+1, Math.min(i+2,data.size())));
            Collections.sort(run);
            runs.add(run);
        }
        List<List<Integer>> tapes = Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        int[] distribution = {runs.size()-1,1,0};
        int idx=0,assigned=0;
        for(int t=0;t<tapes.size();t++){
            for(int r=0;r<distribution[t]&&assigned<runs.size();r++,assigned++){
                tapes.get(t).addAll(runs.get(idx++));
            }
        }
        while(tapes.stream().filter(t->!t.isEmpty()).count()>1){
            int outIdx = tapes.indexOf(tapes.stream().min(Comparator.comparingInt(List::size)).get());
            List<Integer> in1 = new ArrayList<>(tapes.get((outIdx+1)%3));
            List<Integer> in2 = new ArrayList<>(tapes.get((outIdx+2)%3));
            tapes.set(outIdx,merge(in1,in2))

        }   
    }
    public static void main(String[] args){
        List<Integer> data = Arrays.asList(5,2,4,6,1,3,8,7,9);
        System.out.println(polyphaseMergeSort(data));
    }
}
