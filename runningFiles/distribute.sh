echo '>> DISTRIBUTING DISTRIBUTION MACHINE NEW VERSION TO CLIENTS'

scp object-distribution-performance-1.0.jar sample.host.com:/Users/user/

mkdir -p props
cat crud.properties | sed 's/ASYNC=true/ASYNC=true/' | sed 's/HOST=hostname/HOST=hostname/' | sed 's/ID_FROM=0/ID_FROM=0/' | sed 's/ID_TO=25/ID_TO=20/' > props/crud1async.properties
cat crud.properties | sed 's/ASYNC=true/ASYNC=false/' | sed 's/HOST=hostname/HOST=hostname/' | sed 's/ID_FROM=0/ID_FROM=0/' | sed 's/ID_TO=25/ID_TO=20/' > props/crud1sync.properties

cat crud.properties | sed 's/ASYNC=true/ASYNC=true/' | sed 's/HOST=hostname/HOST=hostname2/' | sed 's/ID_FROM=0/ID_FROM=21/' | sed 's/ID_TO=25/ID_TO=40/' > props/crud2async.properties
cat crud.properties | sed 's/ASYNC=true/ASYNC=false/' | sed 's/HOST=hostname/HOST=hostname2/' | sed 's/ID_FROM=0/ID_FROM=21/' | sed 's/ID_TO=25/ID_TO=40/' > props/crud2sync.properties

cat crud.properties | sed 's/ASYNC=true/ASYNC=true/' | sed 's/HOST=hostname/HOST=hostname3/' | sed 's/ID_FROM=0/ID_FROM=41/' | sed 's/ID_TO=25/ID_TO=50/' > props/crud3async.properties
cat crud.properties | sed 's/ASYNC=true/ASYNC=false/' | sed 's/HOST=hostname/HOST=hostname3/' | sed 's/ID_FROM=0/ID_FROM=41/' | sed 's/ID_TO=25/ID_TO=50/' > props/crud3sync.properties

scp props/crud1async.properties sample.hostname.com:/Users/home/fpiagent/ws/oc
scp props/crud1sync.properties sample.hostname.com:/Users/home/fpiagent/ws/oc
scp props/crud2async.properties sample.hostname.com:/Users/home/fpiagent/ws/oc
scp props/crud2sync.properties sample.hostname.com:/Users/home/fpiagent/ws/oc
scp props/crud3async.properties sample.hostname.com:/Users/home/fpiagent/ws/oc
scp props/crud3sync.properties sample.hostname.com:/Users/home/fpiagent/ws/oc

echo '>> DISTRIBUTION COMPLETE'
