package me.play.streaming.tweet

import akka.actor._

/**
 * @author mangeeteden
 */
object App {

  def main(args: Array[String]): Unit = {
    
    if(args == null || args.length == 0) {
      throw new RuntimeException("Please input search(term) query to start Streaming Tweets.")
    }
    
    // search query to fetch tweets
    val query = args(0)  // Happy Halloween

    val system = ActorSystem()
    val storage = system.actorOf(Props[CassandraStorageActor], "cassandra-storage")
    val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, storage) with OAuthTwitterAuthorization), "twitter-feeds")

    // sending query to TweetStreamerActor
    stream ! query
  }

}