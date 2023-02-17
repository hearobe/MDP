package algorithm;

import java.util.ArrayList;
import java.util.Arrays;

public class Permute {
    int length;
    int[] indexes;
    int[] elements;
    ArrayList<int[]> permutations = new ArrayList<int[]>();

    // length is the number of goal nodes there are (excluding start node)
    public Permute(int length) {
        this.length = length;
        indexes = new int[length];
        elements = new int[length];

        for (int i = 0; i<length; i++) {
            elements[i] = i+1;
            indexes[i] = 0;
        }
    }

    public ArrayList<int[]> getPermutations() {
        int i = 0;
        permutations.add(Arrays.copyOf(elements, length));
        while (i < length) {
            if (indexes[i] < i) {
                swap(elements, i % 2 == 0 ?  0: indexes[i], i);
                permutations.add(Arrays.copyOf(elements, length));
                indexes[i]++;
                i = 0;
            }
            else {
                indexes[i] = 0;
                i++;
            }
        }

        return permutations;
    }

    private void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
