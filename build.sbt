name := "scala-spark-twitter"
version := "1.0"
scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "1.6.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"
//libraryDependencies += "org.apache.bahir" %% "spark-streaming-twitter" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.5.2"
libraryDependencies += "com.google.code.gson" % "gson" % "2.3.1"
