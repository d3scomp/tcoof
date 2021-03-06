# tcoof
Trait-based Coalition Formation Framework

master: [![Build Status](https://travis-ci.org/d3scomp/tcof.svg?branch=master)](https://travis-ci.org/d3scomp/tcoof)
develop: [![Build Status](https://travis-ci.org/d3scomp/tcof.svg?branch=develop)](https://travis-ci.org/d3scomp/tcoof/branches)

## Documentation
See [companion page](https://github.com/d3scomp/tcof/blob/master/TECHNICAL.md).

## Installation + run

1. Install Scala (at least 2.12.1), see http://www.scala-lang.org/download/
2. Install Sbt (http://www.scala-sbt.org/download.html) (Note: Sbt doesn't currently work with JDK9, works with JDK8)
3. Run `sbt compile` from tcof project root
4. Start simulator (see https://github.com/d3scomp/tcoof-roborescue-simulator/blob/master/README.md)
5. Start agents by running `sbt -J-Xms2G -J-Xmx5G 'run-main rcrs.Main'`. First execution may take longer to start (about 5 minutes) because framework needs to precompute map data. Execution needs approximately 5GB of free RAM.
