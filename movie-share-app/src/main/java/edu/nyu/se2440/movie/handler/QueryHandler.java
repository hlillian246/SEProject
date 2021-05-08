package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.HandlerInterface;
import edu.nyu.se2440.peerbase.PeerConnection;
import edu.nyu.se2440.peerbase.PeerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class QueryHandler implements HandlerInterface {
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(QueryHandler.class);


    public QueryHandler(MovieShareNode peer) {
        this.peer = peer;
    }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {

        logger.info("Received: " + msg);

        String[] data = msg.getMsgData().split("\\s");

        if (data.length != 5) {
            logger.info("length: " + data.length);

            peerconn.sendData(new PeerMessage(Constants.ERROR, "Query: incorrect arguments"));
            return;
        }

        String sender = data[0].trim();
        int queryId = Integer.parseInt(data[1].trim());

        String ret_pid = data[2].trim();
        String key = data[3].trim();
        int queryHops = Integer.parseInt(data[4].trim());

        logger.info("Received movie query: " + key);

        peerconn.sendData(new PeerMessage(Constants.REPLY, "Query: ACK"));

        if (!peer.getPeerMsgIds().containsKey(sender)) {
            peer.getPeerMsgIds().put(sender, new HashMap<>());
        }

        Map<String, Integer> senderMsgIds = peer.getPeerMsgIds().get(sender);
        if (!senderMsgIds.containsKey(Constants.QUERY)) {
            senderMsgIds.put(Constants.QUERY, 0);
        }

        Integer currentId = senderMsgIds.get(Constants.QUERY);

        logger.info("queryId=" + queryId + ", currentId=" + currentId);

        if (queryId <= currentId) {
            logger.debug("Already handles query: queryId=" + queryId + ", currentId=" + currentId);
            return;
        }

        senderMsgIds.put(Constants.QUERY, queryId);

        QueryProcessor qp = new QueryProcessor(peer, sender, queryId, ret_pid, key, queryHops);
        qp.start();
    }
}
