package edu.nyu.se2440.movie.listener;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieQueryButtonListener implements ActionListener {

    private JTextField movieInput;
    private MovieShareNode peer;

    private static final Logger logger = LoggerFactory.getLogger(MovieQueryButtonListener.class);

    public MovieQueryButtonListener(JTextField movieInput, MovieShareNode peer) {
        this.movieInput = movieInput;
        this.peer = peer;
    }

    public void actionPerformed(ActionEvent event){
        try{
            String key = movieInput.getText().trim();
            key = key.replaceAll(" ", "%20");

            logger.info("Querying for movie: " + key);

            int queryId = peer.getMyMsgIds().get(Constants.QUERY) + 1;

            for (String pid : peer.getPeerKeys()) {

                String msg = peer.getId() + " " + queryId + " " + peer.getId() + " " + key + " " + peer.getQueryHops();

                logger.info("Sending query to " + pid + ": " + msg);
                peer.sendToPeer(pid, Constants.QUERY, msg, true);
            }

            peer.getMyMsgIds().put(Constants.QUERY, queryId);

            movieInput.requestFocusInWindow();
            movieInput.setText("");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
