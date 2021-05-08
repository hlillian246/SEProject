package edu.nyu.se2440.movie.core;

import edu.nyu.se2440.peerbase.Node;
import edu.nyu.se2440.peerbase.util.SimplePingStabilizer;

public class MovieShareStabilizer extends SimplePingStabilizer {

    public MovieShareStabilizer(Node peer) {
        super(peer);
    }

    @Override
    public void stabilizer() {
        super.stabilizer();

        MovieShareNode peer = (MovieShareNode) getPeer();
        peer.buildPeers(peer.getNeighborPeer().getHost(), peer.getNeighborPeer().getPort(), peer.getBuildHops());
    }
}
