package se.nackademin;

import javax.swing.*;

public class Tile extends JButton {


    private int indexValue = 0;

    public int getIndexValue() {
        return indexValue;
    }

    public void setIndexValue(int indexValue) {
        this.indexValue = indexValue;
    }

    public Tile(String text) {
        super(text);
    }
}

