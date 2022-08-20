import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * The View class implements the graphical user interface for the Five In A Row game by extending the JPanel class. It
 * implements all the methods in the IView interface. It displays the buttons and one box in the panel to configure the
 * game and provides the board to show the state of the board by black and white filled circles. It labels the row by
 * capital letter on the right of the board and labels the column by number on the bottom of the board. The step numbers
 * will be shown on the pieces of the mark.
 */
public class View implements IView{
    // The frame of the GUI
    private JFrame jFrame;
    // The left board panel
    private JPanel jPanelLeft;
    // The right options panel
    private JPanel jPanelRight;
    // The buttons on the right options panel
    private JButton[] buttons;
    // The box on the right options panel
    private JComboBox boxes;
    // The size of the right options panel
    private Dimension dimensionRight;
    // The size of the left board
    private Dimension dimensionLeft;
    // The size of the buttons on the right panel
    private Dimension dimensionButton;
    // The clone board updated with the board in the model
    private Player[][] cloneBoard;

    /**
     * Constructor of the View class, which will initialize all the fields in the class and customize the graphical
     * user interface.
     *
     * @param caption The title of the GUI passed in
     */
    public View(String caption){
        // Initialize the clone board
        cloneBoard = new Player[15][15];
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                cloneBoard[i][j] = Player.EMPTY;
            }
        }
        // Create the frame for the GUI
        jFrame = new JFrame();
        jFrame.setTitle(caption);
        jFrame.setSize(800, 650);
        // Centering the window
        jFrame.setLocationRelativeTo(null);
        // option for th close button - Exit the application
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());

        dimensionRight = new Dimension(150,0);
        dimensionLeft = new Dimension(550, 0);
        dimensionButton = new Dimension(140, 40);

        // implement left panel
        jPanelLeft = new JPanel();
        jPanelLeft.setPreferredSize(dimensionLeft);
        jPanelLeft.setBackground(Color.LIGHT_GRAY);
        jFrame.add(jPanelLeft, BorderLayout.CENTER);

        // implement right panel
        jPanelRight = new JPanel();
        jPanelRight.setPreferredSize(dimensionRight);
        jPanelRight.setBackground(Color.white);
        jFrame.add(jPanelRight, BorderLayout.EAST);
        jPanelRight.setLayout(new FlowLayout());

        // add the buttons to the right panel
        String[] buttonsName = {"New Game", "Abort", "Author", "README", "Source"};
        buttons = new JButton[5];
        for(int i = 0; i < 5; i++){
            buttons[i] = new JButton(buttonsName[i]);
            buttons[i].setPreferredSize(dimensionButton);
            jPanelRight.add(buttons[i]);
        }
        String[] boxesName = {"Human vs Human", "Human vs AI", "AI vs AI"};
        boxes = new JComboBox(boxesName);
        jPanelRight.add(boxes);

        // add two text labels
        String author = "Author: Yuting Shao";
        String project = "CS5004 Final Project";
        jPanelRight.add(new JLabel(author));
        jPanelRight.add(new JLabel(project));
    }

    /**
     * Getter of the boxes in the View.
     *
     * @return the JComboBox boxes in the View class.
     */
    @Override
    public JComboBox getBoxes() {
        return boxes;
    }

    /**
     * Getter of the newGameButton in the View.
     *
     * @return the newGameButton in the View class.
     */
    @Override
    public JButton getNewGameButton(){
        return buttons[0];
    }

    /**
     * Getter of the abortButton in the View.
     *
     * @return the abortButton in the View class.
     */
    @Override
    public JButton getAbortButton(){
        return buttons[1];
    }

    /**
     * Getter of the authorButton in the View.
     *
     * @return the authorButton in the View class.
     */
    @Override
    public JButton getAuthorButton(){
        return buttons[2];
    }

    /**
     * Getter of the readmeButton in the View.
     *
     * @return the readmeButton in the View class.
     */
    @Override
    public JButton getReadmeButton(){
        return buttons[3];
    }

    /**
     * Getter of the sourceButton in the View.
     *
     * @return the sourceButton in the View class.
     */
    @Override
    public JButton getSourceButton(){
        return buttons[4];
    }

    /**
     * Getter of the left panel object in the View.
     *
     * @return the jPanelLeft in the View class.
     */
    @Override
    public JPanel getJPanelLeft(){
        return jPanelLeft;
    }

    /**
     * Getter of the right panel object in the View.
     *
     * @return the jPanelRight in the View class.
     */
    @Override
    public JPanel getJPanelRight(){
        return jPanelRight;
    }

    /**
     * Getter of the frame object in the View.
     *
     * @return the jFrame in the View class.
     */
    @Override
    public JFrame getJFrame(){
        return jFrame;
    }

    /**
     * Get the cloneBoard of the View.
     *
     * @return the clone board of the View
     */
    @Override
    public Player[][] getCloneBoard(){
        return cloneBoard;
    }

    /**
     * Update the cloneBoard of the View class by the input board.
     *
     * @param board The input board.
     */
    @Override
    public void updateCloneBoard(Player[][] board){
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                cloneBoard[i][j] = board[i][j];
            }
        }
    }

    /**
     * Display the GUI by the setVisible method in JFrame.
     */
    @Override
    public void display(){
        jFrame.setVisible(true);
        repaint();
    }

    /**
     * Set the listener of the buttons and boxes in the right panel. Set the mouseListener for the left board panel.
     *
     * @param actionListener The input actionListener
     * @param mouseListener The input mouseListener
     */
    @Override
    public void setListener(ActionListener actionListener, MouseListener mouseListener){
        for(int i = 0; i < 5; i++){
            buttons[i].addActionListener(actionListener);
        }
        boxes.addActionListener(actionListener);
        jPanelLeft.addMouseListener(mouseListener);
    }

    /**
     * Repaint the left panel on the GUI.
     *
     */
    @Override
    public void repaint(){
        Graphics graphics = jPanelLeft.getGraphics();
        jPanelLeft.paint(graphics);
        // Paint the board and labels of the row and column on the board
        graphics.setColor(Color.black);
        int boardSize = 15;
        for(int i = 0; i < boardSize; i++){
            graphics.setColor(Color.blue);
            graphics.drawLine(20, 20 + 40 * i, 20 + 40 * (boardSize - 1), 20 + 40 * i);
            String [] rowLabel = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"};
            graphics.drawString(rowLabel[i],620,40 * i+25);
        }
        for(int j = 0; j < boardSize; j++){
            graphics.setColor(Color.blue);
            graphics.drawLine(20 + 40 * j, 20, 20 + 40 * j, 20 + 40 * (boardSize - 1));
            graphics.drawString(String.valueOf(j+1),15 + 40*j,610);
        }
        // Paint the pieces of marks on the board
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                if(cloneBoard[i][j] == Player.X){
                    graphics.setColor(Color.black);
                    graphics.fillOval(40 * j, 40 * i, 40, 40);
                }
                else if(cloneBoard[i][j] == Player.O){
                    graphics.setColor(Color.white);
                    graphics.fillOval(40 * j, 40 * i, 40, 40);
                }
            }
        }
    }

    /**
     * Paint a mark on the input position of the board and label the mark with the step numbers.
     *
     * @param i the input row
     * @param j the input column
     * @param player the input player
     * @param step the input step number
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     * @throws IllegalArgumentException When the player is Player.EMPTY or the step is not positive
     */
    @Override
    public void paintMark(int i, int j, Player player, int step) throws IndexOutOfBoundsException, IllegalArgumentException{
        if(i < 0 || i > 14){
            throw new IndexOutOfBoundsException("Index of row must between 0 and 14.");
        }
        if(j < 0 || j > 14){
            throw new IndexOutOfBoundsException("Index of column must between 0 and 14.");
        }
        if(player == Player.EMPTY){
            throw new IllegalArgumentException("The input player can not be Player.EMPTY.");
        }
        if(step <= 0){
            throw new IllegalArgumentException("The input step number must be positive.");
        }
        Graphics graphics = jPanelLeft.getGraphics();
        graphics.setColor((player == Player.X) ? Color.black : Color.white);
        graphics.fillOval(40 * j, 40 * i, 40, 40);
        graphics.setColor(Color.red);
        graphics.drawString(String.valueOf(step),40 * j+15,40 * i+20);
    }

    /**
     * Pop out a message on the GUI.
     *
     * @param result The input String that will display on the GUI
     */
    @Override
    public void popUp(String result){
        JOptionPane.showMessageDialog(null, result, "Message", JOptionPane.PLAIN_MESSAGE);
    }
}
