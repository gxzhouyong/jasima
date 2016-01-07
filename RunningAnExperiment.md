# Introduction #

After setting up a project to use Jasima, source files can be added in the `src` directory and run from Eclipse.
The Jasima plugin automatically installed the Eclipse M2E plugin, so Eclipse will interact with Maven and run the project with the correct classpath, which includes all Maven dependencies.
Howver, to simply run a serialized experiment, no code has to be written.

The easiest way to create and edit experiments is using the Jasima editor included in the Eclipse plugin, which will show all properties of an experiment and enable comfortable editing.
The graphical editor supports any Java bean and can also be extended with custom editors for classes that go beyond a list of bean properties.
Experiments that were created that way are stored as XML files and can be launched from the IDE or deserialized in user code.
Creating and editing those files by hand is not covered in this tutorial.

# Running a simple experiment #

Use File->New->Other and select Jasima Experiment to create an experiment in XML format anywhere in the project tree.
The default experiment type, `HolthausSimple2Experiment`, will work out of the box.
The XML file can be run using Run As->Jasima Simulation.

# Interpreting results #

After the experiment has been run, the results appear on the console formatted as two tables.
Each row of the first table represents a `ValueStat` object (also known as tally), which summarizes a number of data points.
The following statistical values are printed:
  * `Name` - the name of the result
  * `Mean` - the weighted average of all data points
  * `Min`/`Max` - the lowest and highest number encountered during the simulation
  * `StdDev` - the weighted standard deviation (square root of variance)
  * `Count` - the number of data points (regardless of their weight)
  * `Sum` - the weighted sum of all data points
Values will be NaN if there aren't enough data points.

All results that are not convertible to `ValueStat` are printed in a separate table, which has only two columns: `Name` and `Value`.

Continue with StaticShopExperiment.