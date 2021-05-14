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


public class RateHandler implements HandlerInterface {
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(RateHandler.class);

    public RateHandler(MovieShareNode peer) {
        this.peer = peer;
    }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {

        logger.info("Received: " + msg);

        String[] data = msg.getMsgData().split("\\s");

        if (data.length != 6) {
            logger.info("length: " + data.length);

            peerconn.sendData(new PeerMessage(Constants.ERROR, "Rate: incorrect arguments"));
            return;
        }

        String sender = data[0].trim();
        int rateId = Integer.parseInt(data[1].trim());

        String ret_pid = data[2].trim();
        String key = data[3].trim();
        Float rate = Float.parseFloat(data[4].trim());

        int queryHops = Integer.parseInt(data[5].trim());

        if(sender.equals(peer.getId()))
        {
            logger.info("Sent by me. Ignore the message");
            return;
        }

        if (!peer.getPeerMsgIds().containsKey(sender)) {
            peer.getPeerMsgIds().put(sender, new HashMap<>());
        }

        Map<String, Integer> senderMsgIds = peer.getPeerMsgIds().get(sender);

        if (!senderMsgIds.containsKey(Constants.RATE)) {
            senderMsgIds.put(Constants.RATE, 0);
        }

        Integer currentId = senderMsgIds.get(Constants.RATE);
        logger.debug("rateId=" + rateId + ", currentId=" + currentId);

        if (rateId <= currentId) {
            logger.debug("Already handles query: queryId=" + rateId + ", currentId=" + currentId);
            return;
        }

        senderMsgIds.put(Constants.RATE, rateId);

        logger.info("Received movie rate message. id: " + key + ", rate: " + rate);

        peerconn.sendData(new PeerMessage(Constants.REPLY, "Rate: ACK"));

        RateProcessor rp = new RateProcessor(peer, sender, rateId, ret_pid, key, rate, queryHops);
        rp.start();
    }
}
