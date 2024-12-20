# Getting Started

### To run all application services:

* on the AWS side DynamoDB IAM user should be created
  with correct permissions
* update properties file with correct permissions
* do 'gradle bootWar' for each service
* copy all war files to one folder
* make sure Java 21 is installed in classpath
* run command in the console:

java -jar -Dspring.profiles.active=dev view-0.0.1-SNAPSHOT.war 
& java -jar -Dspring.profiles.active=dev store-0.0.1-SNAPSHOT.war 
& java -jar -Dspring.profiles.active=dev battle-0.0.1-SNAPSHOT.war

table in DynamoDB will be created and filled with correct data while 
first store app start.

app can be tested using url 'http://3.66.225.3:8080/'

### Application itself is the online multi-player game
It gives the ability to fight Bots, fight 1 on 1 between two players 
online. Also you can create the alliance or enter into present one and 
take a part in multiplayer battles. Multiplayer battles also able 
without taking a part in any alliances.

Alliances can control districts and protect from other alliances. 
District control affects all alliance members, it gives additional 
damage, armor, health and money.

Player needs money to buy stuff in the shop.

In regular battles player will have health depends on his level and 
points. Money, level and points will increase after each fight win 
and stay alive.

Every battle with Bot will give 1 money for the win. 1 on 1 battles
will give more then 1 depends on many factors.