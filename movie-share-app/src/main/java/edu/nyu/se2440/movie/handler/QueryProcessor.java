package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.PeerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryProcessor extends Thread {
    private MovieShareNode peer;
    private String sender;
    private int queryId;
    private String ret_pid;
    private String key;
    private int queryHops;

    private static final Logger logger = LoggerFactory.getLogger(QueryProcessor.class);

    public QueryProcessor(MovieShareNode peer, String sender, int queryId, String ret_pid,
                          String key, int queryHops) {
        this.peer = peer;
        this.sender = sender;
        this.queryId = queryId;
        this.ret_pid = ret_pid;
        this.key = key;
        this.queryHops = queryHops;
    }

    public void run() {

        if (peer.getDbAccess().isReadAccess()) {
            String result = null;
            try {
                result = handleMovieQuery(key);
                result = peer.getId() + "|" + result;
            }
            catch (Exception ex) {
                logger.error("Error querying database");
            }

            String[] data = sender.split(":");
            String host = data[0];
            int port = Integer.parseInt(data[1]);

            logger.info("Sending query result: " + result);

            result = result.replaceAll(" ", "%20");

            peer.connectAndSend(new PeerInfo(sender, host, port),
                    Constants.QRESPONSE, result, true);

            return;
        }

        if (queryHops > 1) {
            String msgdata = String.format("%s %d %s %s %d", sender, queryId, peer.getId(), key, queryHops - 1);
            for (String nextpid : peer.getPeerKeys())
                peer.sendToPeer(nextpid, Constants.QUERY, msgdata, true);
        }
    }

    private String handleMovieQuery(String key) throws SQLException {

        key = key.replaceAll("%20", " ");

        logger.info("Querying movie: " + key);

        String query = "SELECT id, title, director, year, rating FROM movies where title like CONCAT('%', ?, '%');";
        PreparedStatement ps = peer.getDbAccess().getConnection().prepareStatement(query);
        ps.setString(1, key);
        logger.info("Query: " + ps.toString());

        ResultSet rs = ps.executeQuery();

        int i=1;

        String result = "";
        while (rs.next()) {
            String line = "  Movie " + i;
            line += "\n      id: " + rs.getString(1);
            line += "\n      title: " + rs.getString(2);
            line += "\n      director: " + rs.getString(3);
            line += "\n      year: " + rs.getInt(4);
            line += "\n      rating: " + rs.getFloat(5);

            if (result.length() > 0) result += "\n";
            result += line;
            i++;
        }

        if (result.length() == 0)
        {
            result = "RES: No result found";
        }

        return result;
    }
}
