# Running a Job Shop/Flow Shop Experiment #

The `DynamicShopExperiment` class can be used to design and run a simulation experiment of a standard job shop or flow shop model. In these models, each job consists of a number of operations that have to be processed in a predefined order. Each operation requires a specific machine for its processing for a certain amount of time. Further assumptions are that a job can only be processed by one machine at a time and that a machine can only process one operation at a time. In the flow shop model, all jobs visit the machines for their operations in the same order, whereas the processing order can differ from job to job in the more general job shop model.

## Experimental Design ##

The `DynamicShopExperiment` class contains a number of numerical, categorical and object parameters (variables) to set up one of these models. Numerical parameters can be set by simply clicking on the field next to the parameter and changing its value as desired. In this case, we set the following model parameters:

  * the number of machines (`numMachines`) to 10,
  * the minimum number of operations per job (`numOpsMin`) to 2,
  * the maximum number of operations per job (`numOpsMax`) to 10,
  * the minimum processing time of an operation (`opProcTimeMin`) to 1,
  * the maximum processing time of an operation (`opProcTimeMax`) to 99,
  * the utilisation of the machines, i.e. the relative proportion of time that the machines are busy, determined by the job arrival rate (`utilLevel`) to 0.8.

We also configure the simulation to stop after all of the first 10000 arriving jobs (with indices 0 to 9999) have been completed by setting the `stopArrivalsAfterNumJobs` parameter to the corresponding value. Alternatively, you can set the `simulationLength` parameter to stop the simulation after a certain simulated time or the `stopAfterNumJobs` to stop the simulation after a certain number of jobs (not necessarily the first arriving jobs) have been completed.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment1.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment1.png)

Note that parameters requiring integer values can also be increased and decreased by clicking on the upward and downward arrows located right of the respective field.

The second type of parameters, categorical parameters, can be set to one of a number of predefined values by clicking on the field next to the parameter, which will open a drop-down menu listing the available options. In this case, we want to build a job shop model and thus select the JOB\_SHOP option in the drop-down menu of the categorical `scenario` parameter.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment2.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment2.png)

The third type of parameters require a reference to a Java object of a certain class. We illustrate the procedure of setting these object parameters by the example of the `sequencingRule` parameter, which refers to the dispatching rule that is used to schedule the machines. The first step is to click on the “new” button located right of the field.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment3.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment3.png)

This will open a dialog listing all types that are compatible with this parameter, i.e. dispatching rules in the case. Here, we choose the `SPT` rule, which prioritises jobs in non-decreasing order of the processing time of their imminent operation, and click on “OK”.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment4.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment4.png)

We can further set a tie-breaking rule that is applied if two jobs have the same priority, i.e. the same operation processing time in this case. This can be done by clicking on `sequencingRule`, which will open a submenu, and then on the “New” button of the parameter `tieBreaker`, which will open the above dialog again.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment5.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment5.png)

In this example, we choose `TieBreakerFASFS` as the tie-breaking rule, which selects jobs in non-decreasing order of their job number (assigned to the job at their point of arrival). You can remove any assigned object again by clicking on the “Delete” button.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment6.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment6.png)

We have now defined all the compulsory parameters of the `DynamicShopExperiment` class, and are almost ready to run the experiment.

## Run Configuration ##

Before running the experiment, we need to decide on the measures and statistics that we would like jasima® to return and also the output format of these results. Currently, jasima® supports the XML and Excel file formats. To define the output format right-click on “TenMachinesJobShopExperiment.jasima” and select “Run As > Jasima Simulation”.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment7.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment7.png)

The run configuration window will open, in which you can set different options for the output of jasima® in the “Results” section. In this example, we want the results to be printed to the console as well as written to an Excel (XLS) file, so we select both options by clicking on the respective tickboxes. You can also provide a name for the Excel file by clicking on the adjacent text field and typing in the desired name. For illustrative purposes, let us name this file “BasicResults”. Once you are done, click on “Run” to run the simulation experiment.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment8.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment8.png)

jasima® will start and (depending on the chosen log level) print out a number of statements to the console that describe the progress of the simulation experiment. Once the experiment is complete, the results are printed to the console and the file “BasicResults.xls” (containing the same information) is created, which you should find in the project tree in the Eclipse Package Explorer.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment9.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment9.png)

By default, the jasima® output is based on the data of all jobs. However, in simulation studies it is common to discard jobs that are completed within the initial, so called warmup period. Let us assume that we determined the warmup period of our experiment to comprise the completion of 2000 jobs and that we want the output to include only data of the jobs with indices 2000 to 9999 (corresponding to a sample size of 8000 jobs). In jasima® GUI, we can achieve this by clicking on the parameter `shopListener` and the on “Add…” in the submenu.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment10.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment10.png)

This will open a window to inform you that the property is set to null. Click on the “New” button.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment11.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment11.png)

Choose `BatchStatCollector`, and click on “OK”. The next window shows some parameters of this collector. Change the values of the parameters `batchSize` (sample size) and `ignoreFirst` to 8000 and 2000 by clicking on the respective text fields, and click on “OK” to confirm (the `initialPeriod` parameter can be used for warmup periods measured in time units).

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment12.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment12.png)

The `BatchStatCollector` should now appear in the submenu of the `shopListener` parameter. Clicking on the “Remove” button will reset the output to the default again.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment13.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment13.png)

Right-click on “TenMachinesJobShopExperiment.jasima” and select “Run As > Run Configurations…” to enter the “Run Configurations” window again, rename the Excel output file “BatchResults” and rerun jasima® by clicking on “Run”.

Sometimes, we may also be interested in statistics related to the machines rather than jobs. In order to retrieve these, you will need to add a machine listener. To do this, click on the parameter `machineListener` and then on “Create new”.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment14.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment14.png)

Next, add a `MachineStatCollector` by proceeding in the same way as for the `BatchStatCollector`. Enter the “Run Configurations” window for the “TenMachinesJobShopExperiment” once again, rename the Excel output file “MachineResults” and rerun jasima® by clicking on “Run”.

Upon the completion of the simulation run, you should have three different Excel files (“BasicResults.xls”, “BatchResults.xls” and “MachineResults.xls”) in the project tree in the Eclipse Package Explorer. We discuss each file in the following section.

## Interpretation of Experimental Results ##

Open the “BasicResults.xls” file by right-clicking on it and selecting “Open With > System Editor”. This will open the file within Excel (or the corresponding spreadsheet program installed on your system).

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment15.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment15.png)

The spreadsheet consists of two sections, an upper section describing the parameter setting of the simulation experiment and a lower section showing the experimental results. The upper section should look familiar in that the listed parameters and their values correspond one-to-one to what is shown in the GUI. In the lower section, there are two type of measures, single value measures and statistics. An example of the former is `numTardy`, which refers to the number of jobs that have been completed after their due date. In contrast, `flowtime` is a statistic of the flow time of jobs, which is summarised by the mean, the minimum value, the maximum value, the standard deviation of values, the number of counted values (the sample size), and the sum of all values (from left to right). Some of the other listed measures are the makespan (`cMax`), the number of started (`numJobsStarted`) and finished (`numJobsFinished`) jobs, the run time of jasima® in seconds (`runTime`), the simulated time (`simTime`), and the tardiness of jobs (`tardiness`). Note that the number of finished jobs is slightly larger than the value of the `stopArrivalsAfterNumJobs` parameter. This is because the stopping criterion actually stops only the generation of new jobs, while all jobs that are in the system at that point are still finished. The completion of the last job then triggers the end of the simulation run. Thus, if jobs that do not belong to the set of the first 10000 arriving jobs finish earlier than any of those 10000, the number of finished jobs will be larger in the end. Next, open the “BatchResults.xls” file.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment16.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment16.png)

As expected, the upper section for the parameter settings is exactly the same apart from the `shopListener` parameter, which now also includes the `BatchStatCollector`. In contrast, the results section has been amended by a variety of job-related measures and statistics with a “b0” prefix. Note that the `b0.flowtime` and `b0.tardiness` statistics differ from their counterparts without the prefix. This is due to the fact that the former are based solely on jobs with indices 2000 to 9999, whereas the latter are based on all jobs (which is also evident from the respective count values). Next, open the “MachineResults.xls” file.

![https://jasima.googlecode.com/svn/wiki/images/DSExperiment17.png](https://jasima.googlecode.com/svn/wiki/images/DSExperiment17.png)
(Screenshot showing Machine Stats)

In addition to the job-related measures, the results section now shows a variety of machine-related statistics for every machine in the shop. Statistics related to the same are identified by the same prefix, which corresponds to the name of the machine, e.g. “m0” in this case. Measures shown include the queue length (`qLen`) and utilisation (`util`) of machines. Note that the actual utilisation of each machine is very close to 0.8, the value of the (analytically derived) `utilLevel` parameter.