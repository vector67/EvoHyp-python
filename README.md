# EvoHyp (python)
### Overview
This is the python port of the
java EvoHyp library which can be found [here](https://sites.google.com/view/evohyp).
Documentation for the java version can be found [here](https://drive.google.com/open?id=1hSNeH3Cky4jYQvRk6Sq5M-2Jp2PQDbSf),
that documentation almost exactly applies to this port, but with exceptions in the naming conventions.
Where possible naming has been expanded and pythonized in order to facilitate reading.

This is a library designed to help in the development of
hyper heuristic algorithms. There are no external dependencies, so no need to run
pip install on anything.

### Structure
This library is split into two halves, GeneticAlgorithm and GeneticProgram.
Each of those has a normal single threaded version, and then a distributed version.
The examples folders show how to use each one and include example problem and solutions.
As this library grows, more examples will be added for more complex domains.