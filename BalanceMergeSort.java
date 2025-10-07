package ExternalSorting;
import java.util.*;
public class BalanceMergeSort {
    public static List<Integer> merge(List<Integer> tape1,List<Integer> tape2){
        List<Integer> outputTape=  new ArrayList<>();
        int i=0,j=0;
        while(i<tape1.size()&&j<tape2.size()){
            if(tape1.get(i)<=tape2.get(j)){
                outputTape.add(tape1.get(i++));
            }else{
                outputTape.add(tape2.get(j++));
            }
        }
        while(i<tape1.size()) outputTape.add(tape1.get(i++));
        while(j<tape2.size()) outputTape.add(tape2.get(j++));
        return outputTape;
    }
    public static List<Integer> balancedMergeSort(List<Integer> data,int numTapes){
        List<List<Integer>> tapes = new ArrayList<>();
        for(int i=0;i<numTapes;i++)  tapes.add(new ArrayList<>());
        for(int i=0;i<data.size();i++){
            tapes.get(i%numTapes).add(data.get(i));
        }
        for(List<Integer> tape:tapes){
            Collections.sort(tape);
        }
        while(tapes.size()>1){
            List<List<Integer>> newTapes = new ArrayList<>();
            for(int i=0;i<tapes.size();i+=2){
                List<Integer> tape1= tapes.get(i);
                List<Integer> tape2= (i+1<tapes.size())?tapes.get(i+1):new ArrayList<>();
                newTapes.add(merge(tape1,tape2));
            }

            tapes= newTapes;
        }
        return tapes.get(0);
    }
    public static void main(String[] args){
        List<Integer> data = Arrays.asList(5, 2, 4, 6, 1, 3, 8, 7, 10, 9);
        int numTapes = 3;
        List<Integer> sorted = balancedMergeSort(data,numTapes);
        System.out.println("Sorted: "+sorted);
    }
}
