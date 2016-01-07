# Getting Started #

This tutorial will guide you through the initial steps of installing and using jasima® to create and run simulation experiments of an existing experiment class, and covers

  1. [Installation](Installation.md)
  1. [Creating a New Simulation Experiment](CreatingExperiments.md)
  1. [Running a Simulation Experiment using jasima® GUI](RunningExperiments.md)

All screenshots have been taken on a system running Windows 8, Eclipse Luna (4.4) and Excel 2013, and may differ slightly from what you see on your screen. The best way to use this guide is to work through the sections step-by-step without skipping any section.

Before proceeding with the [installation](Installation.md) of jasima®, please make sure that Java SE Runtime Environment and Eclipse are installed on your system. You can find the latest versions at

http://www.oracle.com/technetwork/java/javase/downloads/index.html

and

https://www.eclipse.org/downloads/

# Manually building and installing Jasima (outdated?) #

jasima® can either be downloaded as a source archive or checked out directly from the SVN server.

After obtaining the source code, jasima® can be installed using `mvn install` or an IDE.
This will run many tests cases, which will take roughly ten minutes and can be skipped using the parameter `-Dmaven.test.skip=true`.
Likewise, adding `-Dmaven.javadoc.skip=true` will skip Javadoc generation.
The following files will be created:
  * `target/apidoc/*` - API documentation (can be skipped)
  * `target/jasima-main-VERSION-javadic.jar` - compressed API documentation (can be skipped)
  * `target/classes/*` - compiled jasima® classes
  * `target/jasima-main-VERSION.jar` - compressed jasima® classes
  * `target/jasima-main-VERSION-sources.jar` - compressed source coode
  * `target/lib/*` - external dependencies of jasima®
The `install` target will also copy the generated JAR files to your local repository.
If that's not intended, use `mvn package` instead, which accepts the same parameters and will generate the same set of files.

Although the build in the local repository will be preferred over any remote repository, it is a good idea to edit the `pom.xml` and add a version postfix when making code changes.