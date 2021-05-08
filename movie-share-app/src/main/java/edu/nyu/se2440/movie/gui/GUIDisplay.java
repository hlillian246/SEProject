package edu.nyu.se2440.movie.gui;

import edu.nyu.se2440.peerbase.PeerInfo;

import javax.swing.*;
import java.awt.*;

public class GUIDisplay {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea peerWindow;
    private JTextArea messageWindow;
    private JTextArea outputWindow;
    private JTextField messageInput;
    private JTextField movieInput;
    private JTextField rateInput;
    private JButton msgSendButton;
    private JButton movieQueryButton;
    private JButton rateButton;

    private PeerInfo peer;

    public GUIDisplay(String title, PeerInfo peer) {
        this.peer = peer;

        frame = new JFrame(title);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel peerLabel = new JLabel("Peer List");
        peerLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        peerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(peerLabel);

        JPanel peerPanel = new JPanel();
        peerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        peerWindow = new JTextArea(5, 20);
        peerWindow.setAlignmentX(Component.CENTER_ALIGNMENT);
        peerWindow.setLineWrap(true);
        peerWindow.setWrapStyleWord(true);
        peerWindow.setEditable(false);
        peerWindow.setBackground(peerPanel.getBackground());

        peerWindow.append("               No peer detected");
        peerPanel.add(peerWindow);

        topPanel.add(peerPanel);

        mainPanel.add(topPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JPanel outputPanel = new JPanel();
        JLabel movieWindowLabel = new JLabel("Movie Output");
        movieWindowLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        movieWindowLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        outputWindow = new JTextArea(15, 30);
        outputWindow.setLineWrap(true);
        outputWindow.setWrapStyleWord(true);
        outputWindow.setEditable(false);

        JScrollPane qScroller = new JScrollPane(outputWindow);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        leftPanel.add(movieWindowLabel, Component.RIGHT_ALIGNMENT);
        outputPanel.add(qScroller);

        leftPanel.add(outputPanel);

        JPanel movieInputPanel = new JPanel();
        JLabel movieInputLabel = new JLabel("Movie");
        movieInput = new JTextField(8);

        JLabel rateInputLabel = new JLabel("Rate");
        rateInput = new JTextField(2);

        movieQueryButton = new JButton("Query");
        rateButton = new JButton("Rate");

        movieInputPanel.add(movieInputLabel);
        movieInputPanel.add(movieInput);

        movieInputPanel.add(rateInputLabel);
        movieInputPanel.add(rateInput);

        movieInputPanel.add(movieQueryButton);
        movieInputPanel.add(rateButton);

        leftPanel.add(movieInputPanel);

        bottomPanel.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JPanel messagePanel = new JPanel();

        JLabel msgWindowLabel = new JLabel("Message Output");
        msgWindowLabel.setFont(new Font("Verdana", Font.BOLD, 14));

        msgWindowLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        messageWindow = new JTextArea(15, 30);
        messageWindow.setLineWrap(true);
        messageWindow.setWrapStyleWord(true);
        messageWindow.setEditable(false);

        qScroller = new JScrollPane(messageWindow);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        rightPanel.add(msgWindowLabel, Component.LEFT_ALIGNMENT);
        messagePanel.add(qScroller);

        JPanel inputPanel = new JPanel();
        JLabel inputLabel = new JLabel("Message");

        messageInput = new JTextField(16);
        msgSendButton = new JButton("Send");

        inputPanel.add(inputLabel);
        inputPanel.add(messageInput);
        inputPanel.add(msgSendButton);

        rightPanel.add(messagePanel);
        rightPanel.add(inputPanel);

        bottomPanel.add(rightPanel);

        mainPanel.add(bottomPanel);
        frame.add(mainPanel);

        frame.setSize(810, 500);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public JTextArea getPeerWindow() {
        return peerWindow;
    }

    public void setPeerWindow(JTextArea peerWindow) {
        this.peerWindow = peerWindow;
    }

    public JTextArea getMessageWindow() {
        return messageWindow;
    }

    public void setMessageWindow(JTextArea messageWindow) {
        this.messageWindow = messageWindow;
    }

    public JTextArea getOutputWindow() {
        return outputWindow;
    }

    public void setOutputWindow(JTextArea outputWindow) {
        this.outputWindow = outputWindow;
    }

    public JTextField getMessageInput() {
        return messageInput;
    }

    public void setMessageInput(JTextField messageInput) {
        this.messageInput = messageInput;
    }

    public JTextField getMovieInput() {
        return movieInput;
    }

    public void setMovieInput(JTextField movieInput) {
        this.movieInput = movieInput;
    }

    public JTextField getRateInput() {
        return rateInput;
    }

    public void setRateInput(JTextField rateInput) {
        this.rateInput = rateInput;
    }

    public JButton getMsgSendButton() {
        return msgSendButton;
    }

    public void setMsgSendButton(JButton msgSendButton) {
        this.msgSendButton = msgSendButton;
    }

    public JButton getMovieQueryButton() {
        return movieQueryButton;
    }

    public void setMovieQueryButton(JButton movieQueryButton) {
        this.movieQueryButton = movieQueryButton;
    }

    public JButton getRateButton() {
        return rateButton;
    }

    public void setRateButton(JButton rateButton) {
        this.rateButton = rateButton;
    }
}
