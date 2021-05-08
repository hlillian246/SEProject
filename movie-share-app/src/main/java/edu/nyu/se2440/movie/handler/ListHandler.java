package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.HandlerInterface;
import edu.nyu.se2440.peerbase.PeerConnection;
import edu.nyu.se2440.peerbase.PeerMessage;

public class ListHandler implements HandlerInterface {
    private MovieShareNode peer;

    public ListHandler(MovieShareNode peer) { this.peer = peer; }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {
        peerconn.sendData(new PeerMessage(Constants.REPLY,
                String.format("%d", peer.getNumberOfPeers())));
        for (String pid : peer.getPeerKeys()) {
            peerconn.sendData(new PeerMessage(Constants.REPLY,
                    String.format("%s %s %d", pid, peer.getPeer(pid).getHost(),
                            peer.getPeer(pid).getPort())));
        }
    }
}
