package spark;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

/*
 * 
 * This class is a collection of example designed to demonstrate Spark 2 and Java 8.
 * These examples will use the Java 8 lambda approach
 */


public class hw6 {

	static boolean match = false;

	protected static boolean matched(String line) {
		if ((!match) & line.contains("MOBY DICK; OR THE WHALE"))
			match = true;
		return match;
	}

        protected static String reverse(String line) {
		String reverseline = new StringBuffer(line).reverse().toString();
                return reverseline;
        }




	public static void main(String[] args) {
		//this controls a lot of spark related logging
		//comment or change logging level as needed
		Logger.getLogger("org").setLevel(Level.OFF);
		Logger.getLogger("akka").setLevel(Level.OFF);



		SparkConf sparkConf = new SparkConf().setAppName("HW6");
		if (!sparkConf.contains("spark.master")) {
			//this sets the job to use 2 executors locally
			sparkConf.setMaster("local[2]");
		}
		if(args.length != 2){
			System.err.println("missing arguments: requires inputFile outputDirectory");
			System.exit(-1);
		}
		String inputFile = args[0];
		String outputDirectory = args[1];

		try(
				//these will auto close
				SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();
				JavaSparkContext sc =   new JavaSparkContext(spark.sparkContext()); 
		){



			//Read the file in using the default partitioning
			JavaRDD<String> file = sc.textFile(inputFile,1);
			
			//Q6	
			match = false;
			JavaRDD<String> filterFile = file.filter(line -> matched(line));
			long filterFileCount = filterFile.count();
			System.out.println("\nQ6: Total number of lines after filtering: " + filterFileCount);
			filterFile.saveAsTextFile(outputDirectory);

			//Q7
			//now remove the Non- letter characters 
			JavaRDD<String> words = filterFile.flatMap(line -> Arrays.asList(line.split(" ")).iterator());
                        JavaRDD<String> cleanWords  =  words.map(line -> line.toLowerCase().replaceAll("[^a-zA-Z]", ""));
			System.out.println("\nQ7: Total number of Words after removing non letters: " + cleanWords.count());


			//Q8
                        JavaRDD<String> characters  =  cleanWords.flatMap(word -> Arrays.asList(word.split("(?!^)")).iterator());


			//Map Reduce example: count number of time each character appears
			//this maps each word to a tuple, (word,1) then reduces by key, summing up all occurrences of each word
			JavaPairRDD<String, Integer> counts =  characters.mapToPair(word -> new Tuple2<>(word,1)).reduceByKey((x,y) -> x+y);
			//now sort by the key - this will be expensive with large data sets
			JavaPairRDD<String, Integer> keysorted = counts.sortByKey();
			System.out.println("\nQ8: Characters count: " + keysorted.take(30));


			//Q9

                        JavaPairRDD<Integer, String> valuesorted = keysorted.mapToPair(x -> x.swap()).sortByKey();
                        System.out.println("\nQ9: Characters count(frequency): " + valuesorted.take(30));

			

			//Q10
			JavaRDD<String> reverseFile = filterFile.map(line -> reverse(line));
			System.out.println("\nQ10: First 20 lines of reversed lines :\n " + reverseFile.take(20));				



		}
	}
}
