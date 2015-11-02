#bin/.sh

echo "Building project 'spray-akka-tweetstreaming'"
mvn package

echo "Start streaming tweets for query '$1'"
java -jar target/spray-akka-tweetstreaming-0.0.1-SNAPSHOT.jar "$1"