README

a. Application Description

The Movie Share Application is built on the PeerBase P2P framework (http://cs.berry.edu/~nhamid/p2p/framework-java.html). A node may have a movie database read capability or read/write capability. There is a configurable maximum number of peers for each node. 

The application has three main functionalities:

 - Query Movie. A node can query movie details by providing some piece of information of the tile of a movie. The application then sends the query to its peer nodes for the information. When a node receives a movie query message, it will query the movie database if it has the database read capability and then send the response back to the sender. Otherwise, it will send the query to its peer nodes for the information until the configuable maximum hops have been reached.

 - Rate Movie. A node can rate a movie. To rate a movie, a movie ID and a rate must be entered. The application then sends the rating data to its peer nodes for the information. When a node receives a movie rate message, it will update the rating in the movie database if it has the database write capability and then send the new rating back to the sender. Otherwise, it will send the movie rating message to its peer nodes until the configuable maximum hops have been reached. The new rating is based on the ratings from total number of raters.

 - Message. A node can send messages to peer nodes. To send a message, enter message text in the input text box. The application then sends the message to its peer nodes. When a node receives a message, it will display the message on the UI. It then sends the messages to its own peer nodes until the configuable maximum hops have been reached. 

 The Movie Share application has a Java Swing based GUI. It lists peer nodes and two output windows to display incoming data. It also provide text boxes for inputing data.

The application configuration parameters are in appliocation.properties under sr/main/resources. It contains database properties, peer node specific configurations, node host/port information and a neighbor host/port information.


b. Build and Run the application

To build the application, at a command promopt of a terminal, run the following command:

	mvn clean install

To run the application, run the following command:

	mvn exec:java -Dexec.mainClass=edu.nyu.se2440.movie.MovieShareApplication

A GUI window will pop up. The application will automatically detect and build its peer nodes. Its peer nodes will be displayed under Peer List.  


c. Use the application

- To query a movie, enter a movie title information in the Movie text box. Click Query button to initiate the query. The result will be displayed in the Movie Output window.

- To rate a movie, enter the movie title in the Movie text box and its rating in the Rate text box. Click the Rate button to initiate the rating. The new rating will be displayed in the Movie Output window.

- To send a message to peers, enter a text message in the Message text box. Click Send button to initiate the send. The message will be displayed in the Message Output windows of peers.