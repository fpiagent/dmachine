#!/bin/bash

echo "== Data Collection and Parsing Begin =="

if [ "$#" -le 0 ]
then
	echo "Invalid Input, Please Add the name of one or more log files"
	exit 1;
fi

strategy=""
actions=0
success=0
total_milisecs=0
C=0
R=0
U=0
D=0

echo "=> Processing Log Files"
for log_name in "$@"
do
    echo "Retrieving $log_name";
    #TODO: Make hostname and path a parameter
    scp ${hostname}:${log_path}"$log_name" .;
    strategy=$(grep "=> STRATEGY:" $log_name | /usr/local/bin/sed -nr 's/.*STRATEGY: (.+).*/\1/p');
    total_milisecs=$((total_milisecs + $(grep "TOTAL Milisecs Taken:" $log_name | /usr/local/bin/sed -nr 's/.*TOTAL Milisecs Taken:(.+).*/\1/p')));
   	C=$((C + $(grep "Create RUN" $log_name | /usr/local/bin/sed -nr 's/.*Requested:(.+)\/.*/\1/p')));
	R=$((R + $(grep "Read RUN" $log_name | /usr/local/bin/sed -nr 's/.*Requested:(.+)\/.*/\1/p')));
	U=$((U + $(grep "Update RUN" $log_name | /usr/local/bin/sed -nr 's/.*Requested:(.+)\/.*/\1/p')));
	D=$((D + $(grep "Delete RUN" $log_name | /usr/local/bin/sed -nr 's/.*Requested:(.+)\/.*/\1/p')));
done

echo "=> Removing Log Files"
for log_name in "$@"
do
    rm -rf "$log_name"
done

success=$((success/3))
total_milisecs=$((total_milisecs/3))

echo "== SUMMARY OF STATS =="
echo "======================"
echo "Number of clients parsed: 3"
echo "Strategy Used: ${strategy}"
echo "AVG Total Milisecs taken: ${total_milisecs}"
echo "Total Create Actions Run: ${C}"
echo "Total Read Actions Run: ${R}"
echo "Total Update Actions Run: ${U}"
echo "Total Delete Actions Run: ${D}"


echo "== Data Collection and Parsing Complete =="
