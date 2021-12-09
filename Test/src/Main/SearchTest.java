package Main;

public class SearchTest {
    public static void main(String [] args) {
        int[] list = new int[1000];

        int startValue = (int)(Math.random() * 4) + 1;

        for(int i = 0; i < list.length; i++) {
            list[i] = startValue;
            startValue += (int)(Math.random() * 4) + 1;
        }

        for(int i = 1; i <= 50000; i++) {
            System.out.println("Elapsed Time: " + findValue(i, list));
        }

        //for(int value : list)
        //    System.out.print(value + ", ");
        //System.out.println("END");
    }

    public static int findValue(int value, int[] array) {
        int count = 0;

        int[] list = array;

        int upperBound = list.length - 1;
        int lowerBound = 0;
        int median = 0;
        int index = -1;

        int searchItem = value;

        median = (lowerBound + upperBound) / 2;

        while(median != lowerBound && index == -1) {
            if(list[median] < searchItem)
                lowerBound = median;
            else if(list[median] > searchItem)
                upperBound = median;
            else
                index = median;
            median = (lowerBound + upperBound) / 2;

            count++;
        }

        if(index == -1 && list[median] == searchItem)
            index = median;
        else if (index == -1 && list[upperBound] == searchItem)
            index = upperBound;

        if(index == -1)
            return count;

        System.out.print(value + ": ");
        System.out.println(index);

        return count;
    }
}
