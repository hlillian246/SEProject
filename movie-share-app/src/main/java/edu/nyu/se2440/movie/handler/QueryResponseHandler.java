package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.HandlerInterface;
import edu.nyu.se2440.peerbase.PeerConnection;
import edu.nyu.se2440.peerbase.PeerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class QueryResponseHandler implements HandlerInterface {
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(QueryResponseHandler.class);

    public QueryResponseHandler(MovieShareNode peer) { this.peer = peer; }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
        String[] data = msg.getMsgData().split("\\|");

        String result = data[1].replaceAll("%20", " ");

        logger.info("Received query response from " + data[0] + ": " + result);

        JTextArea outputWindow = peer.getDisplay().getOutputWindow();
        outputWindow.append("[FROM: " + data[0] + "]\n" + result + "\n\n");

        peerconn.sendData(new PeerMessage(Constants.REPLY, "Resp: "
                + "Received query result"));

        String[] newPeer = data[0].split(":");
        String host = newPeer[0];
        int port = Integer.parseInt(newPeer[1]);

        logger.info("New peer: host=" + host + ", port=" + port);

        peer.buildPeers(host, port, peer.getBuildHops());
    }
}
