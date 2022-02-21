import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class primality_tests
{
   int a;int n;
   //Implementation of higher power in polylogn time as b has atmost possible logn values and a searches aprropriate value 
   //based on binary search of a^b==n where search key is n it also takes logn time hence polylogn time
   public int higher_power(int a,int b)
   {
     int i=1;int y=1;
     int base=a;
     if(b==1)
       return base;
     while(i<b)
     {
        i=2*i;
        if(i>b)
         {
           i=i/2;
           for(;i<b;++i)
           {
              y=base*a; 
           }
        }
        else
        {
          base=base*base;
          y=base;
        } 
     }  
     return y;
   }
  //Modular higher power is also calculated in poly log n using binary representation of b it take log(b) bits in representation
  //which is used for exponentitation modulo operator is used as it is
   public int modular_higher_power(int a,int b,int n)
   {
       int c=0;
       int d=1;
       String b_bitString=Integer.toBinaryString(b);
       int k=b_bitString.length()-1;
       for(int i=0;i<=k;i++)
       {
          c=2*c;
          d=(d*d)%n;
          if(b_bitString.charAt(i)=='1')
          {
               c=c+1;
               d=(d*a)%n;
          }
       }
       return d;
   }
   //Factorization is done by repeatative division by 2 is number is odd power of 2 is return as 0 otherwise each iteration the 
   //input reduces by half takes logn time.
   public int two_factorize(int x)
   {
       int y=0;
       if(x%2!=0)
         return 0;
       else
       {
         while(x%2!=1)
         {
            x=x/2;
            ++y;
         }
       }  
       return y;
   }
  //Miller_Rabin_Step 0 Test
   public boolean CheckForStep0(int n)
   {
     int a=2;int y=0;this.n=n;
     int b_upper_bound=(int)(Math.ceil(Math.log(n)/Math.log(2)));
     for(int b=2;b<=b_upper_bound;++b) 
     {
         int a_lowerbound=(int)(Math.sqrt(n)/2);
         if(a_lowerbound>a)
            a=a_lowerbound;
         if(BinarySearch(a,b,2,(int)Math.sqrt(this.n)+1))
            return false;
     }
     return true;
   }
   //Binary search implementation of used in higher power function where a is restricted by lower or upper bound that varies
   //depending n cases
   public boolean BinarySearch(int a,int b,int l,int u)
   {
      if(a>=u-1||a<l)
        return false;
      
      int y=higher_power(a, b);
      if(y==this.n)
        return true;
      else if(y<this.n)
      {
         l=a;
         int x=(int)(Math.ceil(l+(u-l)/2));
         BinarySearch(x, b,l,u) ;
      }
      else
      {
         u=a;
         BinarySearch((int)Math.floor(l+(u-l)/2), b, l, u);
      }
     return false;
   }
   //a is chosen randomly from a set of values i.e {1,2,...n-1} and a^(n-1) mod n is tested if it is not equal to 1 the number definitely 
   //is not prime else additional check is done in step 2 using roots of unity
   public boolean CheckForStep1(int n)
   {
      Random random=new Random();
      a=random.nextInt(n-2)+1;
      if (modular_higher_power(a, n-1, n)!=1)
          return false;
      else
         return true;    

   }
   //Implementation of step 2 of miller_rabin_test
   public boolean CheckForStep2(int n)
   {
       int k=two_factorize(n);
       boolean isPrime=true;
       int t=(n/higher_power(2, k));
       ArrayList<Integer>xi=new ArrayList<Integer>();
       int x_base=modular_higher_power(a, t, n) ;
       xi.add(x_base);
       for(int i=1;i<k;++i)
       {
          xi.add(modular_higher_power(xi.get(i-1), xi.get(i-1), n));
          if(xi.get(i)==1)
          {
             if(xi.get(i-1)==-1)
               isPrime= true;
             else
               isPrime=false;  
          }
       }
      return isPrime;
   }
}
public class main_prog
{
   //Main method takes r and n applies varies tests and returns prime or composite
   public static void main(String args[]) 
    {
      System.out.print("n=");
      Scanner in =new Scanner(System.in);
      int n =in.nextInt();
      System.out.print("r=");
      int r =in.nextInt();
      if(Miller_Rabin_Test(n,r))
        System.out.println(n + " is a prime number");
      else
        System.out.println(n + " is a composite number");  
      in.close();
    } 
    //Primality tests are done for n iterations and if even single instance of number being composite is present  
    //amongst r instances then number is composite is returned else prime is returned as prime is detected with 100% probability
    public static boolean Miller_Rabin_Test(int n,int r)
    {
       primality_tests test_obj=new primality_tests();
       ArrayList<Boolean> results=new ArrayList<Boolean>();
       for(int i=0;i<r;++i)
       {
         if(!test_obj.CheckForStep0(n))
           results.add(false);
         else if(!test_obj.CheckForStep1(n))
           results.add(false);
         else
         {
            results.add(test_obj.CheckForStep2(n));
         }   
       }
       return !results.contains(false);   
    }
}
