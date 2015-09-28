package com.tweets.core

import scala.io.Source
import akka.actor.Actor
import akka.actor.ActorRef
import akka.io.IO
import spray.can._
import spray.client.pipelining._
import spray.http._
import spray.http.Uri
import spray.json.JsObject
import spray.json.JsonParser
import spray.json.JsString
import scala.util.Try

/**
 * @author mangeeteden
 */

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}

/* file with name 'twitter' kept at user-home to read, Consumer and Access Token keys. 
 * This keys are required for OAuth authentication to connect to Twitter account. 
 */
trait OAuthTwitterAuthorization extends TwitterAuthorization {

  import OAuth._

  val home = System.getProperty("user.home")
  val lines = Source.fromFile(s"$home/twitter").getLines().toList

  val consumer = Consumer(lines(0), lines(1))
  val token = Token(lines(2), lines(3))

  val authorize: (HttpRequest) => HttpRequest = oAuthAuthorizer(consumer, token)
}

object TweetStreamerActor {
  val twitterUri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
}

case class Tweet(track: String, tweet: String)

/*
 * Actor to prepare HttpRequest, connects with Twitter and get the stream of tweets, 
 * each tweet then will be process by CassandraStorageActor
 */
class TweetStreamerActor(uri: Uri, storage: ActorRef) extends Actor {
  this: TwitterAuthorization =>
  val io = IO(Http)(context.system)

  var track = "";
  def receive: Receive = {
    case query: String =>
      track = query;
      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val rq = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize
      sendTo(io).withResponsesReceivedBy(self)(rq)

    case ChunkedResponseStart(_) => println("Chunked Response started.")

    case MessageChunk(entity, _) =>
      println("Got Chunked Response." + entity.asString)
      self ! Tweet(track, entity.asString)

    case Tweet(track, entity) =>
      Try {
        JsonParser(entity).asJsObject
        storage ! Tweet(track, entity.trim())
      }

    case ChunkedMessageEnd(_, _) => println("Chunked Message Ended")

    case Http.Closed             => println("HTTP closed")

    case _ =>
      storage ! Tweet("TRACK", "Oops! ERROR while connecting with Twitter.")
  }
}

