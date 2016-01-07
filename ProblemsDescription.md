# Benchmark Problems #


---


## General Definition of the Job Shop/Flow Shop Problem ##
In general, scheduling is concerned with the allocation of limited
resources to tasks over time, with the basic aim to ensure
an effective and efficient use of the available resources. In this case, the resources are machines that have to be allocated to jobs
(the tasks) in the best possible way (minimising the objective function). In flow shop and job shop problems, each job consists of a number of processing steps, or operations, that need to be performed on distinct machines in a prespecified processing order. It is further assumed that

  * a job can be processed by only one machine at a time (no splitting or overlapping),
  * a machine can process only one job at a time (no batch processing),
  * a started operation must be completed before the respective machine can process another job (no preemption),
  * an operation can only be performed on the specified machine (no alternate routings),
  * the processing of jobs is independent of the progress of other jobs (no assembly operations or precedence constraints),
  * processing times are independent of the schedule and cover all activities related to the execution of an operation (no sequence-dependent setup times),
  * machines are the only limiting resource (no lack of operators, tools, or material),
  * there are no space or time constraints on input and output buffers (no blocking, scrap or rework),
  * all machines are continusouly available (no breakdowns or maintenance activities),
  * jobs are only available to be scheduled once they have been released.

If the processing orders of all jobs specify that they visit the machines in the same order, the problem is called a flow shop problem. In the more general job shop problem, each job can have a different visiting order of machines.


---


## Specific Problem Characteristics and Objective Function ##
Following the setting by Rajendran and Holthaus (1999), there are **10 machines** in the shop and **new jobs arrive over time according to a Poisson process**, where their characteristics are completely unknown until the time of their arrival (release). Upon arrival, processing times and machines (which are both drawn from a uniform discrete distribution) are assigned to operations at random, which implies that all 10 machines have roughly the same utilisation, i.e. there is no long-term bottleneck.

The objective function is the minimisation of the **mean flow time** of jobs, where the flow time of a job is defined as the length of time the job spends in the shop, from the time of its arrival to the time of its completion. jasima is configured to simulate the completion of 10000 jobs, where the flow times of the first 2000 Jobs are not included in the objective function as they cover the warmup period. In other words, the performance measure obtained from a simulation run is the mean flow time of **jobs numbered from 2001 to 10000**. To reduce the influence of a certain stream of random numbers, **each simulation run is repeated 30 times** using different random number streams.


---


## Experimental Factors ##
In accordance with Rajendran and Holthaus (1999), we vary four of the problem parameters as follows:

  * **processing order** is set to **job shop** or **flow shop** (operations of each job are ordered according to the machine index),

  * **number of operations per job** is set to **10** or **2−10** (drawn from a discrete uniform Distribution),

  * **processing times** are drawn from the **U[1, 49]** or **U[15, 35]** distribution, thereby varying the variance of operation processing times,

  * **utilisation** is set to **80%** or **95%** by varying the parameter of the interarrival time distribution.

Hence, with four experimental factors and two values for each factor, there are **2<sup>4</sup>=16 test problems** in total.


---
