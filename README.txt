To run my code simply use the command prompts

javac primesSH.java
java primesSH

First thing the program does is set up a boolean array that is 10^8 large. Then sets them all to true. 
Using BlockingQueue in java I implemented 8 threads. I learned what prime sieves are in order to optimize finding the primes. 
Upon finding a prime, I would task one of the threads with eliminating all the multiples of that prime from the array, setting them to false.
Since all you do is find multiples of one number, it actually means that the lower numbers should take more time to eliminate their multiples.
2 having to go through the program 5*10^7 times. But the difference between threads is almost negligable because it gets smaller each time,
and since it alternates which thread it gives the prime to with each prime it comes accross, all the threads work on primes numbers 
all over the array instead of just one range; leading to a much fairer distribution of workload.

With the threads the recorded time is about twice as fast, averaging 434631400 nanocseconds vs 837504500 nanoseconds on my computer (with and without threads).
My program also goes back through the entire array to find the primes after the siev, which doesnt implement the threads in either of this, 
and that also went towards this number. 
So the actual process of only prime sieving with threads vs without is probably even more dramatic.

Upon checking this fact, and moving where I end the threads to before I go back through the array and look for the primes(which doesnt involve the threads)
it came out to 379627600 vs 735661900 nanoseconds. So slightly better results.

