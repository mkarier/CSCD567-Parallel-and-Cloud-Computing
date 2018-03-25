

public class SerialPrime 
{
	
	public SerialPrime() 
	{
		super();
	}//end of constructor
	
	//checks whether an int n is prime or not.
	public static boolean isPrime(int n) 
	{
	    //check if n is a multiple of 2
	    if (n%2==0) return false;
	    //if not, then just check the odds
	    for(int i=3;i*i<=n;i+=2) 
	    {
	        if(n%i==0)
	            return false;
	    }//end of for loop
	    return true;
	}//end of isPrime
	
	public static int numSerailPrimes(int low, int high) 
	{
		int numPrime = 0;
		//long start = System.currentTimeMillis();
		for( int i = low; i <= high; i ++ ) 
		{
			if (isPrime(i)) { numPrime ++; }
		}//end of for loop
		//long stop = System.currentTimeMillis();
		//System.out.println("Number of primes found is: " + numPrime);
		return numPrime;
	}//end of numSerialPrimes

}//end of SerialPrime
