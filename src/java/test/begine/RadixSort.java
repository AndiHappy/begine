package begine;

/**
 
基数排序的方式可以采用 LSD (Least sgnificant digital) 或 MSD (Most sgnificant digital)，
LSD 的排序方式由键值的最右边开始，而 MSD 则相反，由键值的最左边开始。 以 LSD 为例，假设原来有一串数值如下所示：

36   9   0   25   1   49   64   16   81   4
首先根据个位数的数值，按照个位置等于桶编号的方式，将它们分配至编号0到9的桶子中：

编号	   0	1	2	3	4	   5	  6	  7	8	 9
       0	1			64	25	  36			 9
         81			4		      16			49
然后，将这些数字按照桶以及桶内部的排序连接起来：

0   1   81   64   4   25   36   16   9   49
接着按照十位的数值，分别对号入座：

编号	    0	1	  2    	3  	4  	5	  6  	7	8	9
        0	16	25	  36	49		  64		81	
        1									
        4									
        9									
最后按照次序重现连接，完成排序：

1
0   1   4   9   16   25   36   49   64   81
  
 * */
public class RadixSort {
	public static void sort(int[] number, int d) //d表示最大的数有多少位
	{
		int k = 0;
		int n = 1;
		int m = 1; //控制键值排序依据在哪一位
		
		int[][] temp = new int[10][number.length]; //数组的第一维表示可能的余数0-9
		
		int[] order = new int[10]; //数组orderp[i]用来表示该位是i的数的个数
		
		while (m <= d) {
			for (int i = 0; i < number.length; i++) {
				int lsd = ((number[i] / n) % 10);
				temp[lsd][order[lsd]] = number[i];
				order[lsd]++;
			}
		
			for (int i = 0; i < 10; i++) {
				if (order[i] != 0)
					for (int j = 0; j < order[i]; j++) {
						number[k] = temp[i][j];
						k++;
					}
				order[i] = 0;
			}
			
			n *= 10;
			k = 0;
			m++;
		}
	}

	public static void main(String[] args) {
		int[] data = { 73, 22, 93, 43, 55, 14, 28, 65, 39, 81, 33, 100 };
		RadixSort.sort(data, 3);
		for (int i = 0; i < data.length; i++) {
			System.out.print(data[i] + "");
		}
	}
}