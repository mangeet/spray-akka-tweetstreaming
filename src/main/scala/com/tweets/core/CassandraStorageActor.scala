package com.tweets.core

import akka.actor._
import akka.actor.Actor
import spray.json.JsObject
import spray.json.JsString
import spray.json.JsNull
import spray.httpx.unmarshalling.MalformedContent
import com.datastax.driver.core.Cluster

object CassandraStorageActor {
  
  // connecting with local cassandra one node instance with keyspace name 'play'
  val cluster = Cluster.builder().addContactPoint("127.0.0.1").build()
  val session = cluster.connect("play")
}

// inserting each tweet as arrive from TweetStreamingActor
class CassandraStorageActor extends Actor {

  def receive: Receive = {
    case Tweet(track, tweet) => 
      CassandraStorageActor.session.execute(s"INSERT INTO tweets (track, tweet_date, tweet) VALUES ('$track', now(), textAsBlob('$tweet'))")

    case JsNull           => Right(None)
    
    case _                => println("Error while saving record in Cassandra.")
  }
}