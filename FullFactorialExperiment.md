# Running Multiple Replications with Varying Parameters #

Often, simulation studies are conducted to compare a number of different settings or designs for a given problem. If the comparisons only concerns a single parameter for which we want to test a limited number of values, this can be realised by manually setting up the experiment with a given candidate value, running the simulation experiment, and repeating these steps until all candidate values have been tested. However, if we want to test a larger number of values or are interested in different combinations of parameter values this quickly becomes a very tedious process. For this reason, the `FullFactorialExperiment` class offers a framework for automatically testing all combinations of a number of provided parameter values. The instructions in this section build on those on how to run [a job shop/flow shop experiment](DynamicShopExperiment.md) and [multiple replications of the same experiment](MultipleReplicationExperiment.md).

## Experimental Design ##

Set processing time distribution (U[1, 49], U[15, 35]), utilisation (0.8, 0.95), scenario (JOB\_SHOP, FLOW\_SHOP), minimum number of operations (2, 10) as in the performance study.

Set seed to 1234567809, note that outer seed overwrites seed of base experiment.

## Run Configurations ##

The experiment can be configured and run in the same way as an experiment of the `DynamicShopExperiment` class. The following section illustrates the basic structure of the resulting Excel output files. Since this is independent of the activated shop and machine listeners we only discuss one Excel file (generated with the default shop listener).

## Interpretation of Experimental Results ##