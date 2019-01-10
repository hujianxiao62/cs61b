package hw3.hash;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Write a utility function that returns true if the given oomages
 * have hashCodes that would distribute them fairly evenly across
 * M buckets. To do this, convert each oomage's hashcode in the
 * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
 * and ensure that no bucket has fewer than N / 50
 * Oomages and no bucket has more than N / 2.5 Oomages.
 */

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        Iterator E = oomages.iterator();
        int[] count = new int[M];
        for(int i=0; i<M; i++){
            count[i]=0;
        }

        for(int  i=0; i<oomages.size(); i++){
            int bucketNum = (oomages.get(i).hashCode() & 0x7FFFFFFF) % M;
            count[bucketNum]++;
        }

        for(int i=0; i<M; i++){
            if(count[i] < oomages.size()/50 || count[i] > oomages.size()/2.5){
                return false;
            }
        }
        return true;
    }
}
