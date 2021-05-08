package edu.nyu.se2440.movie.handler;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import edu.nyu.se2440.peerbase.HandlerInterface;
import edu.nyu.se2440.peerbase.PeerConnection;
import edu.nyu.se2440.peerbase.PeerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MessageHandler implements HandlerInterface {

    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(MovieShareNode peer) { this.peer = peer; }

    public void handleMessage(PeerConnection peerconn, PeerMessage msg) {

        logger.info("Received: " + msg.getMsgData());

        String[] data = msg.getMsgData().split("\\s");

        String sender = data[0];
        int msgId = Integer.parseInt(data[1]);

        peerconn.sendData(new PeerMessage(Constants.REPLY, peer.getId()));

        if (!peer.getPeerMsgIds().containsKey(sender)) {
            peer.getPeerMsgIds().put(sender, new HashMap<>());
        }

        Map<String, Integer> senderMsgIds = peer.getPeerMsgIds().get(sender);

        if (!senderMsgIds.containsKey(Constants.MESG)) {
            senderMsgIds.put(Constants.MESG, 0);
        }

        Integer currentId = senderMsgIds.get(Constants.MESG);
        if (msgId <= currentId) {
            logger.debug("Already handles message: msgId=" + msgId + ", currentId=" + currentId);
            return;
        }

        senderMsgIds.put(Constants.MESG, msgId);

        String retId = data[2];
        String result = data[3].replaceAll("%20", " ");

        logger.info("Received a message: " + result);

        JTextArea outputWindow = peer.getDisplay().getMessageWindow();
        outputWindow.append("[FROM: " + sender + "]\n" + result + "\n\n");

        int msgHops = Integer.parseInt(data[4]);
        msgHops--;

        if (msgHops > 0) {
            for (String pid : peer.getPeerKeys()) {
                String newMsg = sender + " " + msgId + " " + peer.getId() + " " + data[3] + " " + msgHops;

                logger.info("Sending message to " + pid + ": " + result);
                peer.sendToPeer(pid, Constants.MESG, newMsg, true);
            }
        }
    }
}
