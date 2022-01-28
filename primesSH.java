import java.util.*;
import java.io.*;
import java.util.concurrent.*;


class primesSH{



  private static int LIMIT = 100000000; // set 10^8 as upper limit
  private static int THREADNUM = 8; // set number of threads
  private static boolean[] primelist = new boolean[LIMIT];
  private static BlockingQueue<Job> joblist = new ArrayBlockingQueue<Job>(THREADNUM);

  private static class Job  // class for making a job for a thread to do
  {
      public int start;

      public Job(int start)
      {
        this.start = start;
      }
  }

  private static class ThreadWork implements Runnable //class for the threads themselves
  {
    public void run(){
      try{
          while(true)
          {
            Job job = joblist.take();

            if (job.start == -1) { //that is the signal for the thread to stop
                return;
            }
            for (int i = job.start + (job.start + 1); i < LIMIT; i += job.start +1) {
                primelist[i] = false; // change all multiples of the prime to false
            }
          }
      }
      catch (InterruptedException e) {
        }
    }
  }


  public static void main(String[] args) {

    long startTime = System.nanoTime(); // start clock

    for (int i=0; i<LIMIT; i++) { // set all numbers to true
    primelist[i] = true;
    }

    int j = 0;

    int uppercap = (int)Math.sqrt(LIMIT); // only go up to sqrt of 10^8

    for (int i = 0; i < THREADNUM; i++)
    {
      new Thread(new ThreadWork()).start(); // create the threads
    }

    while(j<uppercap)
    {
      if(j==0)
      {
        primelist[j]=false;
      }

      if(!primelist[j])
      {
        j++;
        continue;
      }

      Job job = new Job(j); // prime is found make a job for the threads
      try{
      joblist.put(job);
      }
      catch (InterruptedException e){ }

      j++;
    }

    try{          // stop all the threads
      for(int i = 0; i< THREADNUM; i++)
          joblist.put(new Job(-1));
    }
    catch (InterruptedException e){}


    long sum=0;
    int num=0;
    for(int i = 0; i<LIMIT; i++)  // go back through and count the primes
    {
      if(primelist[i])
      {
        sum += i+1;
        num++;
      }
    }

    int p = 9;
    int[] maxprimes = new int[10]; // find maximum primes
    int n = LIMIT-1;
    while(p>=0)
    {
      if(primelist[n])
      {
        maxprimes[p--] = n + 1;
      }
      n--;
    }

    long endTime   = System.nanoTime(); // stop the clock
    long totalTime = endTime - startTime;

    File file = new File("primes.txt"); // output to text file
    try{
        file.createNewFile();
    }
    catch (IOException e){
    }

    try{
    FileWriter writer = new FileWriter("primes.txt");
      writer.write("Nanoseconds: "+totalTime+" Number of Primes: "+num+" Sum: "+sum);
      writer.write("\n");
      for(int i =0; i<10;i++)
        writer.write(""+maxprimes[i]+" ");
      writer.close();
    }
      catch (IOException e){}



  }

}
