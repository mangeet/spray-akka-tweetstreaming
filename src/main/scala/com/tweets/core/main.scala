package com.tweets.core

import akka.actor._

/**
 * @author mangeeteden
 */
object Main extends App {

  val query = "Cassandra Summit" // hard-coded but can be extended to get it from command-line args

  val system = ActorSystem()
  val storage = system.actorOf(Props[CassandraStorageActor])
  val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, storage) with OAuthTwitterAuthorization))

  stream ! query
}