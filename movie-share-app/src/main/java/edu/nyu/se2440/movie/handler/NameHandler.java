package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.HandlerInterface;
import edu.nyu.se2440.peerbase.PeerConnection;
import edu.nyu.se2440.peerbase.PeerMessage;

public class NameHandler implements HandlerInterface {

    private MovieShareNode peer;

    public NameHandler(MovieShareNode peer) { this.peer = peer; }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
        peerconn.sendData(new PeerMessage(Constants.REPLY, peer.getId()));
    }
}
