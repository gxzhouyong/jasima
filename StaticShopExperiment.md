# StaticShopExperiment #

This experiment is easy to understand because all machines, jobs and their routes are known in advance since they are read from a text file.

First, create a new text file called `sse_inst.txt` (or any other name) and insert the following definitions of three machines, two routes and three jobs into it:
```
# number of machines
3

# number of routes
2

# machine orders of each route (row)
1 2 3
2 1 3

# processing time of each machine (column) for each route (row)
100 200 170
200 100 170

jobs

# number of jobs
3

# <route number> <release date> <due date> <weight>
              1              0          0        1
              1            500          0        1
              2            550          0        1
```

Then, create the actual experiment using the wizard, making sure to use `StaticShopExperiment` as the class name.

The property `instFileName` must be set to the text file containing the scheduling instance, which is `sse_inst.txt` in this example.

At this point, running the experiment will produce summarized results.
While that is often exactly what is needed, we want to look at the jobs being processed in greater detail.
To do this, edit the run configuration that was automatically created, add `--debug` as a program argument and re-run the experiment.
While the console output remains the same, the file `log_debug.txt` now contains detailed information about each job entering the system, being processed by each machine and leaving the system.

Feel free to experiment with different schedulers (change the experiment's `sequencingRule` property) and different job configurations.

Continue with AdvancedExperiments.