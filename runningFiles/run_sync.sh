#!/bin/bash

timestamp=$(date +%s);

if [ "$#" -ne 1 ]
then
	echo "Invalid Input, please use one: couchbase | cassandra | redis | memcached"
	exit 1;
fi

if [[ $1 == "couchbase" ]]
then
	CLASS="com.porter.product.couchbase.CouchbasePerformanceRunner"
elif [[ $1 == "cassandra" ]]
then
	CLASS="com.porter.product.cassandra.CassandraPerformanceRunner"
elif [[ $1 == "redis" ]]
then 
	CLASS="com.porter.product.redis.RedisPerformanceRunner"
elif [[ $1 == "memcached" ]]
then
	CLASS="com.porter.product.memcached.MemcacheDPerformanceRunner"
else
	echo "Invalid Input, please use one: couchbase | cassandra | redis | memcached"
	exit 1;
fi


echo "<<Running SYNC Test for ${1}>>"
echo "Log File Name : ${timestamp}_${1}_sync1.log ${timestamp}_${1}_sync2.log ${timestamp}_${1}_sync3.log"

echo "Triggering CLIENT 1"
ssh sample.hostname.com "cd /Users/home/fpiagent/ws/oc ; /Users/home/fpiagent/ws/jre1.8.0_25/bin/java -cp object-distribution-performance-1.0.jar:lib/* ${CLASS} crud1sync.properties > ${timestamp}_${1}_sync1.log" &> /dev/null &

echo "Triggering CLIENT 2"
ssh sample.hostname.com "cd /Users/home/fpiagent/ws/oc ; /Users/home/fpiagent/ws/jre1.8.0_25/bin/java -cp object-distribution-performance-1.0.jar:lib/* ${CLASS} crud2sync.properties > ${timestamp}_${1}_sync2.log" &> /dev/null &

echo "Triggering CLIENT 3"
ssh sample.hostname.com "cd /Users/home/fpiagent/ws/oc ; /Users/home/fpiagent/ws/jre1.8.0_25/bin/java -cp object-distribution-performance-1.0.jar:lib/* ${CLASS} crud3sync.properties > ${timestamp}_${1}_sync3.log" &> /dev/null &

echo "<<Run Started Successfully>>"
