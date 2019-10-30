package se.nackademin;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class FifteenPuzzleGame extends JFrame implements ActionListener {

    private JPanel overHead = new JPanel();
    private JPanel gridTable = new JPanel();
    private Tile[][] tiles = new Tile[4][4];
    private Tile tile0 = new Tile("");
    private JLabel movesLabel = new JLabel("Moves LEFT");
    private JButton newGameButton = new JButton(" New Game ");

    private int sourceIndex;
    private int blankIndex;
    private int sourceRow;
    private int sourceCol;
    private int blankRow;
    private int blankCol;
    private int movesCounter = 250;

    public FifteenPuzzleGame() {

        setLayout(new BorderLayout());
        add(overHead, BorderLayout.NORTH);
        add(gridTable, BorderLayout.CENTER);

        overHead.setLayout(new BorderLayout());
        overHead.add(newGameButton, BorderLayout.WEST);
        overHead.add(movesLabel, BorderLayout.EAST);
        overHead.setBorder(new EmptyBorder(50, 50, 10, 50));
        overHead.setBackground(new Color(34, 37, 101));

        gridTable.setBackground(new Color(34, 37, 101));
        gridTable.setLayout(new GridLayout(4, 4));
        gridTable.setBorder(new EmptyBorder(50, 50, 50, 50));
        gridTable.setCursor(new Cursor(Cursor.HAND_CURSOR));

        movesLabel.setForeground(Color.WHITE);
        movesLabel.setFont(new Font("Street Cred", Font.PLAIN, 20));
        movesLabel.setBackground(new Color(235, 204, 37));

        newGameButton.setPreferredSize(new Dimension(100, 50));
        newGameButton.setFont(new Font("Street Cred", Font.PLAIN, 13));
        newGameButton.addActionListener(this);
        newGameButton.setBackground(new Color(34, 37, 101));
        newGameButton.setForeground(Color.WHITE);
        newGameButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
                    gridTable.add(tiles[row][col]);
                    tiles[row][col].setBackground(Color.WHITE);
                    tiles[row][col].setName("tile0");
                } else {
                    tiles[row][col] = new Tile(i + "");
                    tiles[row][col].setIndexValue(i);
                    gridTable.add(tiles[row][col]);
                    tiles[row][col].addActionListener(this);
                    tiles[row][col].setBackground(new Color(235, 204, 37));
                    tiles[row][col].setName("tile" + i);
                    tiles[row][col].setFont(new Font("Street Cred", Font.PLAIN, 40));
                    i++;
                }
            }
        }

        do {
            shuffleTiles();
        } while (!isGameSolvable());
        try {
            setIconImage(ImageIO.read(new File("icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        movesLabel.setText("<html>Moves LEFT<br><html>" + "------ " + movesCounter + " ------");
        setTitle("PUZZLE GAME");
        setResizable(false);
        setSize(600, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public boolean isGameSolvable() {
        int inv_counter = 0;
        int[] values = new int[16];
        // Lägger alla komponenters nummer i en int array
        for (int i = 0; i < gridTable.getComponents().length; i++) {
            Tile temp = (Tile) gridTable.getComponents()[i];
            values[i] = temp.getIndexValue();
        }

        for (int i = 0; i < values.length - 1; i++) {
            for (int j = i + 1; j < values.length; j++) {
                if (values[i] < values[j]) {
                    inv_counter++;
                }
            }
        }

        return inv_counter % 2 == 0;
    }

    public boolean areTilesSwappable(JButton button) {

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
        blankIndex = (blankRow * 4) + blankCol;
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

    // Byter plats på knappar
    public void swapTiles(JButton source) {
        Tile tempTile = tiles[sourceRow][sourceCol];
        tiles[sourceRow][sourceCol] = tiles[blankRow][blankCol];
        tiles[blankRow][blankCol] = tempTile;
        gridTable.remove(tile0);
        gridTable.remove(source);

        if (blankIndex < sourceIndex) {
            gridTable.add(source, blankIndex);
            gridTable.add(tile0, sourceIndex);
        } else if (blankIndex > sourceIndex) {
            gridTable.add(tile0, sourceIndex);
            gridTable.add(source, blankIndex);
        }
        revalidate();
        repaint();
    }

    public void shuffleTiles() {
        Random random = new Random();

        //randomize positions for 2D array buttons
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                int randomNumber = random.nextInt(16);
                int randomRow = randomNumber / 4;
                int randomCol = randomNumber % 4;
                Tile temp = tiles[row][col];
                tiles[row][col] = tiles[randomRow][randomCol];
                tiles[randomRow][randomCol] = temp;
            }
        }
        //remove all components from panel
        gridTable.removeAll();

        // add components with randomized position to panel
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                gridTable.add(tiles[row][col]);
            }
        }
        revalidate();
        repaint();
    }

    public boolean isGameCompleted() {

        int[] winPattern = {1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 0};
        int[] winPatternAlt = {0, 1, 2, 3, 4,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15};

        int counter = 0;
        int i = 0;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                if (tiles[row][col].getIndexValue() == winPattern[i] || tiles[row][col].getIndexValue() == winPatternAlt[i]) {
                    counter++;
                    i++;
                }
            }
        }

        if (counter == 16) {
            return true;
        } else {
            return false;
        }
    }

    public void updateMovesCounter() {
        movesCounter--;
        movesLabel.setText("<html>Moves LEFT<br><html>" + "------ " + movesCounter + " ------");
    }

    public void resetMovesCounter() {
        movesCounter = 250;
        movesLabel.setText("<html>Moves LEFT<br><html>" + "------ " + movesCounter + " ------");
    }

    public void newGame() {
        do {
            shuffleTiles();
            resetMovesCounter();
        } while (!isGameSolvable());
    }

    private void gameOverPlayMusic() {
        try
        {
            File file = new File("gameover.wav");
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = inputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        }

        catch (IOException | LineUnavailableException | UnsupportedAudioFileException e)
        {
            JOptionPane.showMessageDialog(null, "ERROR: Ett fel uppstod med musiken!");
        }
    }

    public void gameOverScreen() {

        gameOverPlayMusic();

        Object[] options = {"TRY AGAIN", "QUIT"};
        int inputOption = JOptionPane.showOptionDialog(null, "GAME OVER", "PUZZLE GAME",
                JOptionPane.YES_OPTION, JOptionPane.CANCEL_OPTION, null, options, options[1]);

        if (inputOption == 0) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton source = (JButton) e.getSource();

        if (source == newGameButton) {
            newGame();
        } else if (areTilesSwappable(source)) {
            swapTiles(source);
            updateMovesCounter();
        }

        if (isGameCompleted()) {
            JOptionPane.showMessageDialog(null, "YOU BEAT THE GAME!");
            newGame();
        } else if (movesCounter == 0) {
            gameOverScreen();
        }
    }
}