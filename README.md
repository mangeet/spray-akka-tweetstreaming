# spray-akka-tweetstreaming

Stream data(Tweets) from Tweeter using Spray Client, Akka core and io and save Tweets in Cassandra DB. 
This utility will be helpful to collect data in Cassandra and do analytics with Spark.

Here are the steps to set-up your environment:

1. Install Apache Cassandra(if not availabke in local) and Create a Table in Cassandra. Here is the DDL placed under project scripts/tweet.ddl
2. Create an Tweeter APP account and configure your private consumer and access keys in the file tweeter in your home folder. In the file tweeter(no extension), keep your consumer key, password and Access key, password all one after other in different line with any key. TweetStreamerActor reads this configurations from this file and create an Consumer and Token object that are required to connect with Tweeter and start Streaming.
3. There is direct script is aavailable to execute the main class, so goto main <code>class com.tweets.core.Main</code>, change the query(search query) and run the class.
4. It will start streaming tweets and saving in Cassandra. 
5. Terminate the programme once you feel,ther are enough data collected.
