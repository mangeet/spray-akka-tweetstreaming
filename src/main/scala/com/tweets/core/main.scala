package com.tweets.core

import akka.actor._

/**
 * @author mangeeteden
 */
object Main extends App {

  val system = ActorSystem() 
  val storage = system.actorOf(Props[CassandraStorageActor])
  val stream = system.actorOf(Props(new TweetStreamerActor(TweetStreamerActor.twitterUri, storage) with OAuthTwitterAuthorization))

  stream ! "Cassandra Summit"
}