package se.nackademin;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class FifteenPuzzleGame extends JFrame implements ActionListener {

    private JPanel grid = new JPanel();
    private Tile[][] tiles = new Tile[4][4];
    private Tile tile0 = new Tile("");

    private int emptyIndex;
    private int sourceIndex;
    private int sourceRow;
    private int sourceCol;
    private int blankRow;
    private int blankCol;

    public FifteenPuzzleGame() {

        setLayout(new BorderLayout());
        add(grid, BorderLayout.CENTER);

        grid.setBackground(new Color(34, 37, 101));
        grid.setLayout(new GridLayout(4, 4));
        grid.setBorder(new EmptyBorder(50, 50, 50, 50));
        grid.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            GraphicsEnvironment ge =
                    GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("street cred.ttf")));
        } catch (IOException | FontFormatException e) {
            JOptionPane.showMessageDialog(null, "Filen hittade ej.");
        }

        int i = 1;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (row == 3 && col == 3) {
                    tiles[row][col] = tile0;
                    tile0.setIndexValue(0);
                    grid.add(tiles[row][col]);
                    tiles[row][col].setBackground(Color.WHITE);
                    tiles[row][col].setName("tile0");
                } else {
                    tiles[row][col] = new Tile(i + "");
                    tiles[row][col].setIndexValue(i);
                    grid.add(tiles[row][col]);
                    tiles[row][col].addActionListener(this);
                    tiles[row][col].setBackground(new Color(235, 204, 37));
                    tiles[row][col].setName("tile" + i);
                    tiles[row][col].setFont(new Font("Street Cred", Font.PLAIN, 40));
                    i++;
                }
            }
        }

        try {
            setIconImage(ImageIO.read(new File("icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setTitle("PUZZLE GAME");
        setResizable(false);
        setLocation(500, 200);
        setSize(600, 600);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public boolean isSwappable(JButton button) {

        // för att hitta platsen på knappen man trycker och även den blanka platsen
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (tiles[row][col] == button) {
                    sourceRow = row;
                    sourceCol = col;
                } else if (tiles[row][col] == tile0) {
                    blankRow = row;
                    blankCol = col;
                }
            }
        }
        sourceIndex = (sourceRow * 4) + sourceCol;
        emptyIndex = (blankRow * 4) + blankCol;
        // om den är till höger
        if (sourceCol != 3 && sourceRow == blankRow && tiles[sourceRow][sourceCol + 1] == tile0) {
            return true;
        }
        // om den är till vänster
        else if (sourceCol != 0 && sourceRow == blankRow && tiles[blankRow][sourceCol - 1] == tile0) {
            return true;
        }
        //om den är nedanför
        else if (sourceRow != 0 && sourceCol == blankCol && tiles[sourceRow - 1][sourceCol] == tile0) {
            return true;
        }
        //om den är ovanför
        else if (sourceRow != 3 && sourceCol == blankCol && tiles[sourceRow + 1][sourceCol] == tile0) {
            return true;
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton source = (JButton) e.getSource();

        if (isSwappable(source)) {
        }
    }
}