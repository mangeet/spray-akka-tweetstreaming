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

/**
 * @author mangeeteden
 */

trait TwitterAuthorization {
  def authorize: HttpRequest => HttpRequest
}

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

class TweetStreamerActor(uri: Uri, track: String, storage: ActorRef) extends Actor {
  this: TwitterAuthorization =>
  val io = IO(Http)(context.system)

  def receive: Receive = {
    case query: String =>
      val body = HttpEntity(ContentType(MediaTypes.`application/x-www-form-urlencoded`), s"track=$query")
      val rq = HttpRequest(HttpMethods.POST, uri = uri, entity = body) ~> authorize
      sendTo(io).withResponsesReceivedBy(self)(rq)
    case ChunkedResponseStart(_) =>
    case MessageChunk(tweet, _)  => storage ! Tweet(track, tweet.asString)
    case _ =>
      storage ! Tweet(track, "Oops! ERROR while connecting with Twitter.")
  }
}