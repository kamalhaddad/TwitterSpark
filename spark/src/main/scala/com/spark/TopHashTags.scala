package main.scala.com.spark

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.twitter._

object TopHashTags {

  val cores = 4;
  val master = "local[" + cores + "]" //Local master with 4 cores
  val appName = "TwitterSpark" //

  /* Setting configuration for Spark */
  val config = new SparkConf().setMaster(master).setAppName(appName)

  /* Main entrypoint to spark cluster */
  val sparkContext = new SparkContext(config)

  sparkContext.setLogLevel("WARN")

  def main(args: Array[String]) {
    val twitterConfigFile = "spark/src/main/resources/twitter.conf"

    val twitterParams = scala.io.Source.fromFile(twitterConfigFile).getLines().toArray

    val consumerKey = twitterParams(0)
    val consumerSecretKey = twitterParams(1)
    val accessToken = twitterParams(2)
    val accessTokenSecret = twitterParams(3)
    System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    System.setProperty("twitter4j.oauth.consumerSecret", consumerSecretKey)
    System.setProperty("twitter4j.oauth.accessToken", accessToken)
    System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)

    //        print(consumerKey + " " + consumerSecretKey + " " + accessToken + " " + accessTokenSecret)
    val updateTime = 5
    val streamingContext = new StreamingContext(sparkContext, Seconds(updateTime))

    val filters = args.take(args.length)
    val twitterStream = TwitterUtils.createStream(streamingContext, None, filters)

    val hashTags = twitterStream.filter(status => "en".equalsIgnoreCase(status.getLang()))
      .flatMap(tweet => tweet.getText.split(" ")
        .filter(_.startsWith("#")))

    val tweets = twitterStream.filter(status => "en".equalsIgnoreCase(status.getLang()))
      .flatMap(tweet => tweet.getText.split("\n"))

    //    twitterStream.flatMap(tweet => println(tweet.getText))
    val topCounts10 = hashTags.map((_, 1))
      .reduceByKeyAndWindow(_ + _, Seconds(10))
      .map { case (topic, count) => (count, topic) }
      .transform(_.sortByKey(false))

    tweets.foreachRDD(rdd => {
      val top = rdd.take(2)
      top.foreach(x => println(x))
    })

    //    topCounts10.foreachRDD(rdd => {
    //      val topList = rdd.take(60)
    //      println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
    //      topList.foreach { case (count, tag) => println("%s (%s tweets)".format(tag, count)) }
    //    })

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}
