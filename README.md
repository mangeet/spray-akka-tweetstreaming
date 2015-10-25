# spray-akka-tweetstreaming

Stream data(Tweets) from Tweeter using Spray Client, Akka core and io and save Tweets in Cassandra DB. 
This utility will be helpful to collect data in Cassandra and do analytics with Spark.

Here are the steps to set-up your environment:

1. Install Apache Cassandra(if not availabke in local) and Create a Table in Cassandra. Here is the DDL placed under project scripts/tweet.ddl
2. Create an Tweeter APP account and configure your private consumer and access keys in the file tweeter in your home folder.
