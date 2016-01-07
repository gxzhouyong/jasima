# Saving results in an Excel file #

A tabular structure lends itself well to the presentation of the parameters and results of an experiment execution for many reasons:
Borwsing a spreadsheet is fast and convenient, calculations can be made on-the-fly and modern spread sheet applications can easily handle huge amounts of data.
The class `ExcelSaver` can be used to generate Microsoft Excel (.xls) files from experiment runs.
It can either be added from code or created and added by an experiment runner class using the `--xlsres` parameter.
It does not make a difference if the experiment itself is created from code, deserialied from an XML file or created from an Excel file.

<a href='Hidden comment: TODO explain sheets and tables'></a>

# Configuring an experiment from an Excel file #

Jasima can also read in experiment conigurations from Excel files.
Currently, only `CompoundConfExperiment`, `FullFactorialExperiment` and their subclasses are supported.
The experiment type can be configured in three different ways:
  1. Programatically:  If an existing experiment is passed to `ExcelExperimentReader.createExperiment`, it is always used.
  1. Explicitly:  Otherwise, if there is a parameters sheet and it specifies an experiment, it is used.
  1. By deduction:  Otherwise, if only one of the sheets 'configurations' and 'parameters' exists, a `CompoundConfExperiment` or a `FullFactorialExperiment` is created.

## Specifying values ##

While Java has literally thousands of different types, Excel naturally can not directly represent any type found in Java.
The primitive types and String can easily be represented by text and numeric cells and will be converted as needed.
If a date cell is encountered, its value is converted to a Unix timestamp.
If any other type is required, a text cell must be used.
It may either contain a class name, in which case its default constructor is used, or a file name, in which case the XML file of that name will be deserialized.

## The 'parameters' sheet ##

This optional sheet is used to configure the `AbstractMultiExperiment`.
Only the first two columns are used, and each row must form a key-value pair.
Each pair will set one property of the outer experiment.
The key 'experiment' is special:
If it appears, it must appear only once and before all other rows.

Example:
| **experiment** | `RandomFractionalExperiment` |
|:---------------|:-----------------------------|
| **baseExperiment** | `mimac03.jasima`             |
| **maxConfigurations** | 20                           |

## The 'configurations' sheet ##

This sheet must be present when a `CompoundConfExperiment` is used.
The first row contains the properties of the base experiment that will be changed, while each following row is one configuration of the base experiment to be run.
The special property 'experiment' will override the outer experiment's `baseExperiment` parameter.
Empty cells are allowed, and will lead to the property not being changed in the configuration in which they appear.
Except for empty cells, the layout must be rectangular (in particular, each row will have the same length).

Example (will create 4 experiments):
| **experiment** | **utilLevel** | **dueDateFactor** |
|:---------------|:--------------|:------------------|
|                | 0.5           | 0.5               |
|                | 0.5           | 0.8               |
|                | 0.9           | 0.8               |
| `holthaus_big.jasima` | 0.99          | 0.9               |

## The 'factors' sheet ##

This sheet must be present when a `FullFactorialExperiment` is used.
Similarly to the 'configurations' sheet, the first row specifies the properties that will be changed.
Unlike the 'configurations' sheet, each column have a different name and each value can be combined with any other value, as long as exactly one is used from each row.
`FullFactorialExperiment` will create an experiment for each possibly combination, while subclasses like `RandomFractionalExperiment` may only create and run a subset of those experiments.
The special property 'experiment' is also supported.
The order of the value cells within their column is not significant.

Example (will create 36 experiments):
| **experiment** | **utilLevel** | **dueDateFactor** |
|:---------------|:--------------|:------------------|
| `holthaus_small.jasima` | 0.5           | 0.5               |
| `holthaus_big.jasima` | 0.6           | 0.7               |
|                | 0.7           | 1.0               |
|                | 0.8           |                   |
|                | 0.9           |                   |
|                | 0.95          |                   |