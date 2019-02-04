/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int l=0;
        String[] sortS = new String[asciis.length];
        for(int i=0; i<asciis.length; i++){
            //get longest length of string
            l = l > asciis[i].length()? l:asciis[i].length();
            sortS[i]=asciis[i];
        }

        //apply radix sort for each digit, from right to left
        for(int i =l; i>=1; i--){
            sortS = sortHelperLSD(sortS,i);
        }
        return sortS;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        //count char at each digit(index)
        // if string length is short than index, then it is a placeholder, count it at the end of count[]
        int[] count = new int[257];
        for (String i : asciis) {
            if(i.length()<index){
                count[256]++;
            }else {
                count[(int) i.charAt(index-1)]++;
            }
        }

        //calculate the starts[], starts[256](placeholder) starts at 0
        int[] starts = new int[257];
        starts[256] = 0;
        int pos = count[256];
        for (int i = 0; i < starts.length-1; i += 1) {
            starts[i] = pos;
            pos += count[i];
        }

        //sort using starts[]
        String[] sort = new String[asciis.length];
        for (int i = 0; i < asciis.length; i += 1) {
            if(asciis[i].length()<index){
                sort[starts[256]] = asciis[i];
                starts[256]++;
            }else {
                int position = starts[(int) asciis[i].charAt(index-1)];
                sort[position] = asciis[i];
                starts[(int) asciis[i].charAt(index-1)]++;
            }
        }
        return sort;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
