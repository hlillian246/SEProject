package edu.nyu.se2440.movie.core;

import edu.nyu.se2440.movie.gui.GUIDisplay;
import edu.nyu.se2440.movie.handler.*;
import edu.nyu.se2440.peerbase.Node;
import edu.nyu.se2440.peerbase.PeerInfo;
import edu.nyu.se2440.peerbase.PeerMessage;
import edu.nyu.se2440.peerbase.RouterInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieShareNode extends Node {

    private GUIDisplay display;

    private PeerInfo neighborPeer;
    private int buildHops;
    private int queryHops;
    private int msgHops;

    private Map<String, Map<String, Integer>> peerMsgIds= new HashMap<>();
    private Map<String, Integer> myMsgIds = new HashMap<>();

    private NodeDBAccess dbAccess;

    private static final Logger logger = LoggerFactory.getLogger(MovieShareNode.class);


    public MovieShareNode(int maxPeers, PeerInfo myInfo, PeerInfo neighborPeer, int buildHops, int queryHops,
                          int msgHops, GUIDisplay display, NodeDBAccess dbAccess) {
        super(maxPeers, myInfo);

        this.neighborPeer = neighborPeer;
        this.buildHops = buildHops;
        this.queryHops = queryHops;
        this.msgHops = msgHops;
        this.display = display;
        this.dbAccess = dbAccess;

        this.addRouter(new Router(this));


        this.addHandler(Constants.PEERNAME, new NameHandler(this));
        this.addHandler(Constants.INSERTPEER, new JoinHandler(this));
        this.addHandler(Constants.LISTPEER, new ListHandler(this));

        this.addHandler(Constants.QUERY, new QueryHandler(this));
        this.addHandler(Constants.QRESPONSE, new QueryResponseHandler(this));

        this.addHandler(Constants.RATE, new RateHandler(this));
        this.addHandler(Constants.RRESPONSE, new RateResponseHandler(this));

        this.addHandler(Constants.MESG, new MessageHandler(this));

        myMsgIds.put(Constants.QUERY, 0);
        myMsgIds.put(Constants.RATE, 0);
        myMsgIds.put(Constants.MESG, 0);
    }


    public void buildPeers(String host, int port, int hops) {
//        LoggerUtil.getLogger().info("build peers");

        if (this.maxPeersReached() || hops <= 0) {
            return;
        }

        PeerInfo pd = new PeerInfo(host, port);

        List<PeerMessage> resplist = this.connectAndSend(pd, Constants.PEERNAME, "", true);

        if (resplist == null || resplist.size() == 0) {
            logger.warn("Neighbor Peer is unreachable: " + host + ", " + port);
            return;
        }

        String peerid = resplist.get(0).getMsgData();
        pd.setId(peerid);

        String resp = this.connectAndSend(pd, Constants.INSERTPEER,
                String.format("%s %s %d", this.getId(), this.getHost(), this.getPort()), true).get(0).getMsgType();

        if (!resp.equals(Constants.REPLY) || this.getPeerKeys().contains(peerid)) {
            return;
        }

        logger.info("Adding: " + pd.getId());

        this.addPeer(pd);

        Map<String, Integer> msgIds = new HashMap<>();
        msgIds.put(Constants.QUERY, 0);
        msgIds.put(Constants.RATE, 0);
        msgIds.put(Constants.MESG, 0);
        peerMsgIds.put(pd.getId(), msgIds);

        updatePeerListDisplay();

        // do recursive depth first search to add more peers
        resplist = this.connectAndSend(pd, Constants.LISTPEER, "", true);

        if (resplist.size() > 1) {

            resplist.remove(0);
            for (PeerMessage pm : resplist) {

                String[] data = pm.getMsgData().split("\\s");
                String nextpid = data[0];
                String nexthost = data[1];

                int nextport = Integer.parseInt(data[2]);

                if (!nextpid.equals(this.getId())) {
                    buildPeers(nexthost, nextport, hops - 1);
                }
            }
        }
    }

    public void updatePeerListDisplay() {
        JTextArea peerWindow = display.getPeerWindow();

        String msg = String.format("%5s\t%s\n", "Index", "Id");
        int i=1;
        for (String pid : getPeerKeys()) {
            msg += String.format("%-7d\t%s\n", i, pid);
            i++;
        }

        peerWindow.setText(msg);
    }


    private class Router implements RouterInterface {
        private Node peer;

        public Router(Node peer) {
            this.peer = peer;
        }

        public PeerInfo route(String peerid) {
            if (peer.getPeerKeys().contains(peerid)) return peer.getPeer(peerid);
            else return null;
        }
    }

    public int getBuildHops() {
        return buildHops;
    }

    public void setBuildHops(int buildHops) {
        this.buildHops = buildHops;
    }

    public int getQueryHops() {
        return queryHops;
    }

    public void setQueryHops(int queryHops) {
        this.queryHops = queryHops;
    }

    public int getMsgHops() {
        return msgHops;
    }

    public void setMsgHops(int msgHops) {
        this.msgHops = msgHops;
    }

    public NodeDBAccess getDbAccess() {
        return dbAccess;
    }

    public void setDbAccess(NodeDBAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

    public GUIDisplay getDisplay() {
        return display;
    }

    public void setDisplay(GUIDisplay display) {
        this.display = display;
    }

    public Map<String, Map<String, Integer>> getPeerMsgIds() {
        return peerMsgIds;
    }

    public void setPeerMsgIds(Map<String, Map<String, Integer>> peerMsgIds) {
        this.peerMsgIds = peerMsgIds;
    }

    public Map<String, Integer> getMyMsgIds() {
        return myMsgIds;
    }

    public void setMyMsgIds(Map<String, Integer> myMsgIds) {
        this.myMsgIds = myMsgIds;
    }

    public PeerInfo getNeighborPeer() {
        return neighborPeer;
    }

    public void setNeighborPeer(PeerInfo neighborPeer) {
        this.neighborPeer = neighborPeer;
    }
}
