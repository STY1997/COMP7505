//import java.util.Arrays;
class DisplayRandom extends DisplayRandomBase {

    public DisplayRandom(String[] csvLines) {
        super(csvLines);
    }

    @Override
    public Plane[] sort() {
        Plane[] planeLists = this.getData();
        return quickSort(planeLists, 0, planeLists.length - 1);
        //return mergeSort(planeLists, 0, planeLists.length - 1);
    }

    /* Implement all the necessary methods here */

    /**
     * Sort the plane list.
     * @param planeList plane list
     * @param left element index of the left part
     * @param right element index of the right part
     * @return sorted plane list
     */
    private Plane[] quickSort(Plane[] planeList, int left, int right) {
        if (left < right) {
            int index = partition(planeList, left, right);
            quickSort(planeList, left, index - 1);
            quickSort(planeList, index + 1, right);
        }
        return planeList;
    }

    /**
     * Return the new pivot index.
     * @param planeList plane list
     * @param left element index of the left part
     * @param right element index of the right part
     * @return new pivot index
     */
    private int partition(Plane[] planeList, int left, int right) {
        //set the the first item in the partition list as the new pivot
        int pivot = left;
        int index = pivot + 1;
        for (int i = index; i <= right; i++) {
            if (planeList[i].compareTo(planeList[pivot]) < 0) {
                swap(planeList, i, index);
                index++;
            }
        }
        swap(planeList, left, index - 1);
        return index - 1;
    }

    /**
     * Swap the positions of element i and j in the list
     * @param planeList plane list
     * @param i element index in left part
     * @param j element index in right part
     */
    private void swap(Plane[] planeList, int i, int j) {
        Plane temporary = planeList[i];
        planeList[i] = planeList[j];
        planeList[j] = temporary;
    }


}

class DisplayPartiallySorted extends DisplayPartiallySortedBase {

    public DisplayPartiallySorted(String[] scheduleLines, String[] extraLines) {
        super(scheduleLines, extraLines);
    }

    @Override
    Plane[] sort() {
        Plane[] scheduleLines = this.getSchedule();
        Plane[] extraLines = this.getExtraPlanes();
        mergeSort(extraLines, 0, extraLines.length - 1);
        return mergeSortTwoSortedLists(scheduleLines, extraLines);
    }

    /* Implement all the necessary methods here */
    private void mergeSort(Plane[] extraLines, int left, int right){
        if (left == right) {
            return;
        }
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(extraLines, left, mid);
            mergeSort(extraLines, mid + 1, right);
            merge(extraLines, mid, left, right);
        }
    }

    private void merge(Plane[] planeLists, int mid, int left, int right) {
        Plane[] tempPlaneList = new Plane[right - left + 1];
        int index = mid + 1;
        int temp = left;
        int k = 0;
        //put the smaller element to the temporary list
        while (temp <= mid && index <= right) {
            if (planeLists[temp].compareTo(planeLists[index]) <= 0) {
                tempPlaneList[k++] = planeLists[temp++];
            } else {
                tempPlaneList[k++] = planeLists[index++];
            }
        }
        //add the rest element to the list
        while (index <= right) {
            tempPlaneList[k++] = planeLists[index++];
        }
        while (temp <= mid) {
            tempPlaneList[k++] = planeLists[temp++];
        }
        //copy the sorted plane list from temporary list to original list
        for (int readIndex = 0; readIndex < right - left + 1; readIndex++) {
            planeLists[left + readIndex] = tempPlaneList[readIndex];
        }
    }

    private Plane[] mergeSortTwoSortedLists(Plane[] scheduleLines, Plane[] sortedExtraLines) {
        int right = scheduleLines.length + sortedExtraLines.length;
        Plane[] sortedLines = new Plane[right];
        int i = 0, j = 0, k = 0;
        while (i < scheduleLines.length && j < sortedExtraLines.length) {
            if (scheduleLines[i].compareTo(sortedExtraLines[j]) <= 0) {
                sortedLines[k++] = scheduleLines[i++];
            } else {
                sortedLines[k++] = sortedExtraLines[j++];
            }
        }
        while (i < scheduleLines.length) {
            sortedLines[k++] = scheduleLines[i++];
        }
        while (j < sortedExtraLines.length) {
            sortedLines[k++] = sortedExtraLines[j++];
        }
        return sortedLines;
    }

//    public static void main(String[] args) {
//
//        Plane[] schedule = new Plane[4];
//        schedule[0] = new Plane("ENC3453", "8:23");
//        schedule[1] = new Plane("ABC1233", "9:24");
//        schedule[2] = new Plane("ABC1234", "9:24");
//        schedule[3] = new Plane("BAA1113", "9:24");
//
//        Plane[] extra = new Plane[5];
//        extra[0] = new Plane("COP7505", "10:40");
//        extra[1] = new Plane("NYC3361", "13:50");
//        extra[2] = new Plane("COP3506", "12:30");
//        extra[3] = new Plane("BBC2314", "11:05");
//        extra[4] = new Plane("ACT1500", "8:15");
//
//        String[] strings = new String[4];
//        strings[0] = "ABC1233,9:24";
//        strings[1] = "ENC3453,8:23";
//        strings[2] = "BAA1113,9:24";
//        strings[3] = "ABC1234,9:24";
//
//        DisplayPartiallySorted d = new DisplayPartiallySorted(strings, strings);
//        d.setSchedule(schedule);
//        d.setExtraPlanes(extra);
//        //System.out.println(Arrays.toString(d.mergeSortTwoSortedLists(d.getSchedule(), d.getSchedule())));
//
//        d.mergeSort(d.getExtraPlanes(), 0, d.getExtraPlanes().length - 1);
//
////        DisplayRandom d2 = new DisplayRandom(strings);
////        d2.setData(extra);
////        d2.sort();
//        //System.out.println(Arrays.toString(extra));
//
//
//
//
//    }

}


