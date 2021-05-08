package edu.nyu.se2440.movie.core;

import edu.nyu.se2440.movie.gui.GUIDisplay;
import edu.nyu.se2440.movie.listener.MessageButtonListener;
import edu.nyu.se2440.movie.listener.MovieQueryButtonListener;
import edu.nyu.se2440.movie.listener.RateMovieButtonListener;
import edu.nyu.se2440.peerbase.LoggerUtil;
import edu.nyu.se2440.peerbase.PeerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;

public class MovieShareService {
    MovieShareNode peer;
    Properties props;

    private static final Logger logger = LoggerFactory.getLogger(MovieShareService.class);

    public MovieShareService(Properties props) {
        this.props = props;
        initialize();
    }

    private void initialize() {
        LoggerUtil.setHandlersLevel(Level.FINE);

        String myHost = props.getProperty("my.peer.host");
        int myPort = Integer.parseInt(props.getProperty("my.peer.port"));

        String neighborHost = props.getProperty("neighbor.peer.host");
        int neighborPort = Integer.parseInt(props.getProperty("neighbor.peer.port"));

        PeerInfo myPeer = new PeerInfo(myHost, myPort);
        PeerInfo neighborPeer = new PeerInfo(neighborHost, neighborPort);

        int maxPeers = Integer.parseInt(props.getProperty("max.peers"));
        int buildHops = Integer.parseInt(props.getProperty("build.peer.hops"));

        int queryHops = Integer.parseInt(props.getProperty("query.peer.hops"));
        int msgHops = Integer.parseInt(props.getProperty("msg.peer.hops"));

        GUIDisplay display = new GUIDisplay(myPeer.getId(), myPeer);

        boolean dbQueryEnabled = Boolean.parseBoolean(props.getProperty("peer.db.query.enabled"));
        boolean dbUpdateEnabled = Boolean.parseBoolean(props.getProperty("peer.db.update.enabled"));

        Connection connection = null;
        if (dbQueryEnabled || dbUpdateEnabled) {
            try {
                connection = connectToDatabase(props);
            } catch (Exception e) {
                e.printStackTrace();
                dbQueryEnabled = false;
                dbUpdateEnabled = false;
            }
        }

        NodeDBAccess dbAccess = new NodeDBAccess(dbQueryEnabled, dbUpdateEnabled, connection);
        peer = new MovieShareNode(maxPeers, myPeer, neighborPeer, buildHops, queryHops, msgHops, display, dbAccess);

        display.getMovieQueryButton().addActionListener(new MovieQueryButtonListener(display.getMovieInput(), peer));
        display.getRateButton().addActionListener(new RateMovieButtonListener(display.getMovieInput(), display.getRateInput(), peer));
        display.getMsgSendButton().addActionListener(new MessageButtonListener(display.getMessageInput(), peer));

        peer.buildPeers(neighborHost, neighborPort, buildHops);

        (new Thread() { public void run() { peer.mainLoop(); }}).start();

        peer.startStabilizer(new MovieShareStabilizer(peer), 30000);
    }


    private static Connection connectToDatabase(Properties props) throws ClassNotFoundException, SQLException {

        Class.forName(props.getProperty("db.driver"));

        String server = props.getProperty("db.server");
        String database = props.getProperty("db.database");
        String connectionStr = "jdbc:mysql://" + server + "/" + database + "?serverTimezone=UTC";

        Connection connection = DriverManager.getConnection(connectionStr,
                props.getProperty("db.username"), props.getProperty("db.password"));

        return connection;
    }



}
