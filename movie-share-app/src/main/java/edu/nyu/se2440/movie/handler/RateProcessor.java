package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.PeerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RateProcessor extends Thread {
    private MovieShareNode peer;
    private String sender;
    private int rateId;
    private String ret_pid;
    private String key;
    private float rate;
    private int queryHops;

    private static final Logger logger = LoggerFactory.getLogger(RateProcessor.class);

    public RateProcessor(MovieShareNode peer, String sender, int rateId, String ret_pid,
                         String key, float rate, int queryHops) {
        this.peer = peer;
        this.sender = sender;
        this.rateId = rateId;
        this.ret_pid = ret_pid;
        this.key = key;
        this.rate = rate;
        this.queryHops = queryHops;
    }

    public void run() {

        if (peer.getDbAccess().isReadAccess() && peer.getDbAccess().isUpdateAccess()) {
            String result = null;
            try {
                float newRate = handleMovieRate(key, rate);
                result = peer.getId() + "|" + "   New rating is " + newRate;
            }
            catch (Exception ex) {
                logger.error("Error querying database");
            }

            String[] data = sender.split(":");
            String host = data[0];
            int port = Integer.parseInt(data[1]);

            result = result.replaceAll(" ", "%20");

            logger.info("Sending rate result: " + result);
            logger.info("host: " + host + ", port: " + port);

            peer.connectAndSend(new PeerInfo(sender, host, port),
                    Constants.RRESPONSE, result, true);

            return;
        }

        if (queryHops > 1) {
            String msgdata = String.format("%s %d %s %s %f %d",
                    sender, rateId, ret_pid, key, rate, queryHops - 1);
            for (String nextpid : peer.getPeerKeys()) {
                peer.sendToPeer(nextpid, Constants.RATE, msgdata, true);
            }
        }
    }

    private float handleMovieRate(String key, float rate) throws SQLException {
        logger.info("Querying movie: " + key);

        String query = "SELECT rating, numVotes FROM movies where id=?";

        PreparedStatement ps = peer.getDbAccess().getConnection().prepareStatement(query);

        ps.setString(1, key);

        logger.info("Query: " + ps.toString());

        ResultSet rs = ps.executeQuery();
        rs.next();

        Float rating = rs.getFloat(1);
        int numVotes = rs.getInt(2);

        logger.info("Original rating: " + rating + ", numVotes: " + numVotes);

        float tmp = numVotes * rating;
        numVotes++;

        rating = (tmp + rate) / numVotes;

        logger.info("New rating: " + rating + ", numVotes: " + numVotes);

        String sql = "UPDATE movies SET rating=?, numVotes=? where id=?;";

        ps = peer.getDbAccess().getConnection().prepareStatement(sql);
        ps.setFloat(1, rating);
        ps.setInt(2, numVotes);
        ps.setString(3, key);

        logger.info("Query: " + ps.toString());

        ps.execute();

        return rating;
    }
}
