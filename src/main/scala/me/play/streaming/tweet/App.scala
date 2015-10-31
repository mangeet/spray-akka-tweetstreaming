package me.play.streaming.tweet

import akka.actor._

/**
 * @author mangeeteden
 */
object App {

  def main(args: Array[String]): Unit = {
    
    val query = "Happy Halloween" // hard-coded but can be extended to get it from command-line args

    val system = ActorSystem()
    val storage = system.actorOf(Props[CassandraStorageActor], "cassandra-storage")
    val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, storage) with OAuthTwitterAuthorization), "twitter-feeds")

    stream ! query
  }

}