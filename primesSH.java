import java.util.*;
import java.io.*;
import java.util.concurrent.*;


class primesSH{



  private static int LIMIT = 100000000;
  private static int THREADNUM = 8;
  private static boolean[] l1 = new boolean[LIMIT];
  private static BlockingQueue<Job> joblist = new ArrayBlockingQueue<Job>(THREADNUM);

  private static class Job
  {
      public int start;

      public Job(int start)
      {
        this.start = start;
      }
  }

  private static class ThreadWork implements Runnable
  {
    public void run(){
      try{
          while(true)
          {
            Job job = joblist.take();

            if (job.start == -1) {
                return;
            }
            for (int i = job.start + (job.start + 1); i < LIMIT; i += job.start +1) {
                l1[i] = false;
            }
          }
      }
      catch (InterruptedException e) {
        }
    }
  }


  public static void main(String[] args) {

    long startTime = System.nanoTime();
    for (int i=0; i<LIMIT; i++) {
    l1[i] = true;
    }

    int j = 0;

    int uppercap = (int)Math.sqrt(LIMIT);

    for (int i = 0; i < THREADNUM; i++)
    {
      new Thread(new ThreadWork()).start();
    }

    while(j<uppercap)
    {
      if(j==0)
      {
        l1[j]=false;
      }

      if(!l1[j])
      {
        j++;
        continue;
      }

      Job job = new Job(j);
      try{
      joblist.put(job);
      }
      catch (InterruptedException e){ }

      j++;
    }

    try{
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    joblist.put(new Job(-1));
    }
    catch (InterruptedException e){}

    long endTime   = System.nanoTime();
    long totalTime = endTime - startTime;

    long sum=0;
    int num=0;
    for(int i = 0; i<LIMIT; i++)
    {
      if(l1[i])
      {
        sum += i+1;
        num++;
      }
    }

    int p = 9;
    int[] maxprimes = new int[10];
    int n = LIMIT-1;
    while(p>=0)
    {
      if(l1[n])
      {
        maxprimes[p--] = n + 1;
      }
      n--;
    }



    File file = new File("primes.txt");
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
