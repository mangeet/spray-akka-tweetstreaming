# spray-akka-tweetstreaming

Stream data(Tweets) from Tweeter using Spray Client, Akka core and io and save Tweets in Cassandra DB. 
This utility will be helpful to collect data in Cassandra and do analytics with Spark.

Here are the steps to set-up your environment:

1. Download and Install Apache Cassandra(if not available in your local) and Create a Table in Cassandra. Here is the DDL file available fpr reference under the project scripts/tweet.ddl

2. Create an Tweeter APP account and configure your private consumer and access keys in the file with the name 'tweeter' in your home folder. 
   In the file tweeter(no extension), keep your consumer key, password and Access key, password all one after the other as separate line. TweetStreamerActor reads this configurations from this file and create an Consumer and Token object that are required to connect with Tweeter and start Streaming.

3. There is a script available to start streaming, bin/start-streaming.sh <search-term>
   This script builds the project with maven shade assembly plugin and execute the Main-Class

4. It will start streaming tweets and saving in Cassandra. 

5. Terminate the programme once you feel, there are enough data collected.
