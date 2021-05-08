package edu.nyu.se2440.movie.listener;

import edu.nyu.se2440.movie.core.Constants;
import edu.nyu.se2440.movie.core.MovieShareNode;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RateMovieButtonListener implements ActionListener {

    private JTextField movieInput;
    private JTextField rateInput;
    private MovieShareNode peer;

    public RateMovieButtonListener(JTextField movieInput, JTextField rateInput, MovieShareNode peer) {
        this.movieInput = movieInput;
        this.rateInput = rateInput;
        this.peer = peer;
    }

    public void actionPerformed(ActionEvent event){
        try{
            String key = movieInput.getText().trim();
            float rate = Float.parseFloat(rateInput.getText().trim());

            int rateId = peer.getMyMsgIds().get(Constants.RATE) + 1;

            for (String pid : peer.getPeerKeys()) {

                String msgdata = String.format("%s %d %s %s %f %d",
                        peer.getId(), rateId, peer.getId(), key, rate, peer.getQueryHops());
                peer.sendToPeer(pid, Constants.RATE, msgdata, true);
            }

            peer.getMyMsgIds().put(Constants.RATE, rateId);

            movieInput.requestFocusInWindow();
            movieInput.setText("");
            rateInput.requestFocusInWindow();
            rateInput.setText("");
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
