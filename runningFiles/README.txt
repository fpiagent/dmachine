DMachine
--------
Distributed Load Generator

HOW TO:
-------
- Build Project:
	- gradle build
- Distribute jar and properties file to Distributed Machines:
	- cd build/libs
	- ./distribute.sh  
- Execute DMachine:
	- ./run_async.sh couchbase|cassandra|redis|memcached
	- OR ./run_sync.sh couchbase|cassandra|redis|memcached
- Collect Data
	- Log file names will be printed when executing run_sync script, Full logs available in each box
	- ./collect_data.sh brings the files and parses it


