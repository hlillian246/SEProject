package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class JoinHandler implements HandlerInterface {
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(JoinHandler.class);


    public JoinHandler(MovieShareNode peer) { this.peer = peer; }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
        if (peer.maxPeersReached()) {
            LoggerUtil.getLogger().info("maxpeers reached " +
                    peer.getMaxPeers());
            peerconn.sendData(new PeerMessage(Constants.ERROR, "Join: " +
                    "too many peers"));
            return;
        }

        // check for correct number of arguments
        String[] data = msg.getMsgData().split("\\s");
        if (data.length != 3) {
            peerconn.sendData(new PeerMessage(Constants.ERROR, "Join: " +
                    "incorrect arguments"));
            return;
        }

        // parse arguments into PeerInfo structure
        PeerInfo info = new PeerInfo(data[0], data[1],
                Integer.parseInt(data[2]));

        if (peer.getPeer(info.getId()) != null)
            peerconn.sendData(new PeerMessage(Constants.ERROR, "Join: " +
                    "peer already inserted"));
        else if (info.getId().equals(peer.getId()))
            peerconn.sendData(new PeerMessage(Constants.ERROR, "Join: " +
                    "attempt to insert self"));
        else {
            logger.info("Adding: " + info.getId());
            peer.addPeer(info);

            Map<String, Integer> msgIds = new HashMap<>();
            msgIds.put(Constants.QUERY, 0);
            msgIds.put(Constants.RATE, 0);
            msgIds.put(Constants.MESG, 0);
            peer.getPeerMsgIds().put(info.getId(), msgIds);

            peer.updatePeerListDisplay();
            peerconn.sendData(new PeerMessage(Constants.REPLY, "Join: " +
                    "peer added: " + info.getId()));
        }
    }
}
