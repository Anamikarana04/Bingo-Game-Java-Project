import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BingoGameGUI extends JFrame {
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static final int MAX_NUMBER = 25;

    private JButton callButton;
    private JTextField numberField;
    private JLabel nextNumberLabel;
    private JPanel userBoardPanel;
    private JLabel[][] userLabels;

    private int[][] userBoard;
    private boolean[][] userMarked;

    private Random random;

    public BingoGameGUI() {
        setTitle("Bingo Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        initComponents();
        initializeGame();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel controlPanel = new JPanel();
        callButton = new JButton("Call Number");
        nextNumberLabel = new JLabel("Next Number: ");
        numberField = new JTextField(5);
        controlPanel.add(nextNumberLabel);
        controlPanel.add(numberField);
        controlPanel.add(callButton);

        userBoardPanel = new JPanel(new GridLayout(ROWS, COLS));

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(userBoardPanel, BorderLayout.WEST);

        add(mainPanel);

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callNumber();
            }
        });
    }

    private void initializeGame() {
        random = new Random();
        userBoard = generateBoard();
        userMarked = new boolean[ROWS][COLS];
        userLabels = new JLabel[ROWS][COLS];

        initializeBoard(userBoard, userBoardPanel, userLabels);
        callNextNumber();
    }

    private void initializeBoard(int[][] board, JPanel boardPanel, JLabel[][] labels) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                JLabel label = new JLabel(Integer.toString(board[i][j]));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                labels[i][j] = label;
                boardPanel.add(label);
            }
        }
    }

    private int[][] generateBoard() {
        int[][] board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int number;
                do {
                    number = random.nextInt(MAX_NUMBER) + 1;
                } while (contains(board, number));
                board[i][j] = number;
            }
        }
        return board;
    }

    private boolean contains(int[][] board, int number) {
        for (int[] row : board) {
            for (int num : row) {
                if (num == number) {
                    return true;
                }
            }
        }
        return false;
    }

    private void callNextNumber() {
        int nextNumber = random.nextInt(MAX_NUMBER) + 1;
        nextNumberLabel.setText("Next Number: " + nextNumber);
    }

    private void callNumber() {
        int number = Integer.parseInt(numberField.getText());
        markNumber(userBoard, userMarked, number);
        updateBoard(userBoard, userMarked, userLabels);
        checkForBingo(userMarked, "You");
        callNextNumber();
    }

    private void markNumber(int[][] board, boolean[][] marked, int number) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (board[i][j] == number) {
                    marked[i][j] = true;
                    return;
                }
            }
        }
    }

    private void updateBoard(int[][] board, boolean[][] marked, JLabel[][] labels) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (marked[i][j]) {
                    labels[i][j].setBackground(Color.GREEN);
                }
            }
        }
    }

    private void checkForBingo(boolean[][] marked, String playerName) {
        if (hasBingo(marked)) {
            JOptionPane.showMessageDialog(this, playerName + " won the game!");
            resetGame();
        }
    }

    private boolean hasBingo(boolean[][] marked) {
        // Check rows
        for (int i = 0; i < ROWS; i++) {
            boolean bingo = true;
            for (int j = 0; j < COLS; j++) {
                if (!marked[i][j]) {
                    bingo = false;
                    break;
                }
            }
            if (bingo) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < COLS; j++) {
            boolean bingo = true;
            for (int i = 0; i < ROWS; i++) {
                if (!marked[i][j]) {
                    bingo = false;
                    break;
                }
            }
            if (bingo) {
                return true;
            }
        }

        // Check diagonals
        boolean diag1 = true;
        boolean diag2 = true;
        for (int i = 0; i < ROWS; i++) {
            if (!marked[i][i]) {
                diag1 = false;
            }
            if (!marked[i][COLS - 1 - i]) {
                diag2 = false;
            }
        }
        return diag1 || diag2;
    }

    private void resetGame() {
        userBoardPanel.removeAll();
        initializeGame();
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BingoGameGUI();
            }
        });
    }
}

