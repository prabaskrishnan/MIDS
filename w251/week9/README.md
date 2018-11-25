Steps to Run this Program.

1. SSH into Spark Cluster
2. cd to the folder where yo copy the scala and build file
3. Run sbt pacakage command
4. With the twitter keys run the following

$SPARK_HOME/bin/spark-submit \
--packages org.apache.spark:spark-streaming_2.11:2.1.0 --packages org.apache.bahir:spark-
streaming-twitter_2.11:2.1.0 \
--master spark://spark1:7077 $(find target -iname "*.jar") \
