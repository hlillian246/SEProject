package edu.nyu.se2440.movie.listener;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageButtonListener implements ActionListener {

    private JTextField messageInput;
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(MessageButtonListener.class);

    public MessageButtonListener(JTextField messageInput, MovieShareNode peer) {
        this.messageInput = messageInput;
        this.peer = peer;
    }

    public void actionPerformed(ActionEvent event){
        try{
            String msg = messageInput.getText().trim();
            msg = msg.replaceAll(" ", "%20");

            logger.info("Sending message: " + msg);

            int msgId = peer.getMyMsgIds().get(Constants.MESG) + 1;

            for (String pid : peer.getPeerKeys()) {
                String newMsg = peer.getId() + " " + msgId +
                        " " + peer.getId() + " " + msg + " " + peer.getMsgHops();

                logger.info("Sending message to " + pid + ": " + msg);
                peer.sendToPeer(pid, Constants.MESG, newMsg, true);
            }

            peer.getMyMsgIds().put(Constants.MESG, msgId);

            messageInput.requestFocusInWindow();
            messageInput.setText("");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
