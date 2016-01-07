# Introduction #

Although Jasima can be integrated into the Eclipse IDE using a plugin, it does not depend on Eclipse and can be used on its own.
Maven is not strictly required to use Jasima either, but will simplify installation and updates of Jasima and all its dependencies.
There is no documentation for using Jasima without Maven.

# Using Jasima in a Maven project #
When inserted into the `pom.xml`, the following snippet will add Jasima as a dependency:
```
<repositories>
	<repository>
		<id>jasima-rep</id>
		<url>http://www2.ips.biba.uni-bremen.de/~hil/maven2</url>
	</repository>
</repositories>
<dependencies>
	<dependency>
		<groupId>jasima</groupId>
		<artifactId>jasima-main</artifactId>
		<version>0.0.1-SNAPSHOT</version>
	</dependency>
</dependencies>
```

# Running an experiment from Java code #
```
package tutorial;

import jasima.core.experiment.Experiment;
import jasima.shopSim.models.holthausSimple2.HolthausSimple2Experiment;

public class Main {
	public static void main(String[] args) {
		Experiment e = new HolthausSimple2Experiment();
		e.runExperiment();
		e.printResults();
	}
}
```

# Running an experiment in XML format #
```
mvn compile exec:java -Dexec.mainClass=jasima.core.util.XmlExperimentRunner -Dexec.args=<experimentFile>
```