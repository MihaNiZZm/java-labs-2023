package ru.nsu.fit.mihanizzm.game2048.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Stopwatch implements ActionListener {
    private int elapsedTime = 0;
    private int secs = 0;
    private int mins = 0;
    private String secsString = String.format("%02d", secs);
    private String minsString = String.format("%01d", mins);
    private ActionListener timerListener = null;
    public String totalTimeString = "0:00";

    Timer timer;

    Stopwatch(ActionListener listener) {
        timer = new Timer(1000, listener);
    }

    public void setTimerListener(ActionListener listener) {
        this.timerListener = listener;
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void reset() {
        timer.stop();
        elapsedTime = 0;
        secs = 0;
        mins = 0;
        secsString = String.format("%02d", secs);
        minsString = String.format("%02d", mins);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
