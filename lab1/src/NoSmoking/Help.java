package NoSmoking;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class Help{
    private static double calculate(long w, double x)
    {
      switch((int) w)
      {
          case 7: 
              return Math.cbrt(Math.atan(Math.pow( (x - 5.5)/17.0 ,2)));

          case 5,6,8,9,12,16,17,18: 
              return Math.cbrt(Math.pow( (1.0/3.0)/(2.0-Math.pow(2.0*x,3)) ,3));
      };
          return Math.pow(2.0 * Math.cbrt(Math.exp(Math.log(Math.abs(x)))) ,3);
    }
    
    private static void display(double[][] w)
    {
	      System.out.print('[');
	      for(int i = 0; i < 17; i++)
	      {
	          System.out.print('[');
	          for(int j = 0; j < 16; j++)
	          {
		            if (j != 15) System.out.printf("%.5f; ", w[i][j]);
		            else System.out.printf("%.5f", w[i][j]);
	          }
	          if (i != 16) System.out.println("], \n");
	          else System.out.print("]");
	      }

	      System.out.println(']');
    }

    public static void main(String[] args)
    {
        long[] w = new long[17];
	      for (int i = 4; i <= 20; i++)
	          w[i-4] = i;

      	double[] x = new double[16];
	      for(int i = 0; i < 16; i++)
	          x[i] = Math.random()*17-14;

	      double[][] w1 = new double[17][16];
	      for (int i = 0; i < 17; i++)
	      {
	          for(int j = 0; j < 16; j++)
		            w1[i][j] = calculate(w[i], x[j]);
	      }
	      display(w1);
    }
}
