You must have the following in the same directory/folder:
    plots (folder)
    primes.txt
    retail.txt
    run.py

To run, open run.py in an IDE and enter 'py run.py' in the console.

Once running, the program will run all 4 algorithms (A-Priori, PCY, Multistage PCY, Multihash PCY)
    for all dataset sizes, for all support thresholds.
The console will show which support threshold is currently used for mining.
When complete, the graph for the support threshold is saved in the 'plots' folder and the next 
    support threshold is used, until all 3 graphs are saved.

primes.txt is simply a text file of all prime numbers until 19997. The source is included as a 
    comment under the 'get_next_prime()' function.

retail.txt is the entire dataset provided for the project. Each line is a basket, and there are
    a total of 88,162 baskets. Each basket has a varying number of items. All items are integers.