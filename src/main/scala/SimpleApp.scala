import java.io.File

import com.google.gson.Gson
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{ Seconds, StreamingContext }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.hadoop.fs._

object SimpleApp {
  private var numTweetsCollected = 0L
  private var partNum = 0
  private var gson = new Gson()

  def main(args: Array[String]) {
    // Process program arguments and set properties
    if (args.length < 3) {
      System.err.println("Usage: " + this.getClass.getSimpleName +
        "<outputDirectory> <numTweetsToCollect> <intervalInSeconds> <partitionsEachInterval>")
      System.exit(1)
    }
    val Array(outputDirectory, Utils.IntParam(numTweetsToCollect), Utils.IntParam(intervalSecs), Utils.IntParam(partitionsEachInterval)) =
      Utils.parseCommandLineWithTwitterCredentials(args)

    //  val outputDir = new File(outputDirectory.toString)

    //if (!outputDir.exists()) {
    //  outputDir.mkdirs()
    //  
    //} else{
    //  System.err.println("ERROR - %s already exists: delete or specify another directory".format(
    //    outputDirectory))
    //System.exit(1)
    //}

    println("Initializing Streaming Spark Context...")
    val conf = new SparkConf().setMaster("local[2]").setAppName(this.getClass.getSimpleName)
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(intervalSecs))

    val fs = FileSystem.get(sc.hadoopConfiguration)
    if (fs.exists(new Path(outputDirectory.toString))) {
      println("Target Dir already exists. Now deleting.")
      fs.delete(new Path(outputDirectory.toString), true)
    } else {
      println("now Creating Target Directory.")
      fs.mkdirs(new Path(outputDirectory.toString))
    }

    val tweetStream = TwitterUtils.createStream(ssc, Utils.getAuth)
      .map(gson.toJson(_))
    
      println("Initializing TweetStream...")
        
    tweetStream.foreachRDD((rdd, time) => {
      val count = rdd.count()
      if (count > 0) {
        val outputRDD = rdd.repartition(partitionsEachInterval)
        println("Initializing Streaming Spark Context...")
        //fs.mkdirs(new Path(outputDirectory.toString + "/tweets_" + time.milliseconds.toString))
        outputRDD.saveAsTextFile(outputDirectory.toString + "/tweets_" + time.milliseconds.toString)
        outputRDD.saveAsTextFile(outputDirectory.toString + "/tweets_" + time.milliseconds.toString)
        //rdd.saveAsTextFile(outputDirectory.toString)
        
        //rdd.take(100).foreach(println)
        
        numTweetsCollected += count
        if (numTweetsCollected > numTweetsToCollect) {
          println("Tweets Collection Is Complete")
          System.exit(0)
        }
      }
    })
    ssc.start()
    ssc.awaitTermination()
  }
}