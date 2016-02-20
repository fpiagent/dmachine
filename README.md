# dmachine
DMachine is a standalone component to test several distributed caching or no sql technologies.

##Overview
This is POC to test in an easy and configurable way several Distributed Caching tools.

##Functionality

###Actors
One Director machine
One or More Client machines
One or More Server machines

###Prerequisites
- Basic knowledge and installation of each server node with the wanted technology
- Java 7+ installed in all Client machines
- Remote ssh access to all Client boxes (unix kind of server expected)

###Steps

1. Director machine distributes the files to be run to each client
2. Director machine configures the test
3. The run is triggered
4. Logs are both dropped at each Client box and collected by the Director machine
5. See results

##How to run
The folder runningFiles is meant to have all the required files for running this tests.
- Read runningFiles/README.txt for details on each file
- Configure your test with runningFiles/crud.properties
	- Deterministic or Non-Deterministic?
	- Random seeds (To handle several different deterministic tests)
	- Id generation: How many different objects to be distributed?
	- CRUD distribution: How many Create, Read, Update or Delete per run?
	- Time test: Want to run a stress test? A quality test? Or just a performance test?
	- Serialization: some are not implemented but the tools are theref

##Technologies used/considered

Development
- Gradle
- Java 8

Caching/NoSQL
- Aerospike
- Cassandra
- Coherence
- Couchbase
- Hazelcast
- MemcacheD
- Redis

Serialization
- Avro
- Boon
- Gson (json)
- Java
- Kryo
- Protobuf
- Thrift
- Xstream (xml)

DISCLAIMER: This is a POC and not all technologies have the corresponding implementation, but the concept is applicable. Analyze each distribution technology to know about compatibilities, not all allow all payloads and configure for the desired transport.

