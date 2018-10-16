#!/bin/bash
for i in 1 2 3 4 5
do
  START=$(date +%s)
  echo "Starting file transfer Loop :  $i ......"
  curl -o /root/data/largefile1 -H 'X-Auth-Token: AUTH_tk9dd294e90b614e00985e983ea8471c3b' https://dal05.objectstorage.softlayer.net/v1/AUTH_15bec70f-34cd-41f2-b32a-b3a6520d6113/container1/largefile1
  END=$(date +%s)
  DIFF=$(( $END - $START ))
  S= echo  "scale=2; 1024/$DIFF " | bc -l 
  echo "It took $DIFF seconds to download  1GB file"
done


