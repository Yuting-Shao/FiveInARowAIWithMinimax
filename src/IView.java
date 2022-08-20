import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * The IView interface implements the graphical user interface for the Five In A Row game.
 */
public interface IView {
    /**
     * Getter of the boxes in the View.
     *
     * @return the JComboBox boxes in the View class.
     */
    JComboBox getBoxes();

    /**
     * Getter of the newGameButton in the View.
     *
     * @return the newGameButton in the View class.
     */
    JButton getNewGameButton();

    /**
     * Getter of the abortButton in the View.
     *
     * @return the abortButton in the View class.
     */
    JButton getAbortButton();

    /**
     * Getter of the authorButton in the View.
     *
     * @return the authorButton in the View class.
     */
    JButton getAuthorButton();

    /**
     * Getter of the readmeButton in the View.
     *
     * @return the readmeButton in the View class.
     */
    JButton getReadmeButton();

    /**
     * Getter of the sourceButton in the View.
     *
     * @return the sourceButton in the View class.
     */
    JButton getSourceButton();

    /**
     * Getter of the left panel object in the View.
     *
     * @return the jPanelLeft in the View class.
     */
    JPanel getJPanelLeft();

    /**
     * Getter of the right panel object in the View.
     *
     * @return the jPanelRight in the View class.
     */
    JPanel getJPanelRight();

    /**
     * Getter of the frame object in the View.
     *
     * @return the jFrame in the View class.
     */
    JFrame getJFrame();

    /**
     * Get the cloneBoard of the View.
     *
     * @return the clone board of the View
     */
    Player[][] getCloneBoard();

    /**
     * Update the cloneBoard of the View class by the input board.
     *
     * @param board The input board.
     */
    void updateCloneBoard(Player[][] board);

    /**
     * Display the GUI by the setVisible method in JFrame.
     */
    void display();

    /**
     * Set the listener of the buttons and boxes in the right panel. Set the mouseListener for the left board panel.
     *
     * @param actionListener The input actionListener
     * @param mouseListener The input mouseListener
     */
    void setListener(ActionListener actionListener, MouseListener mouseListener);

    /**
     * Repaint the left panel on the GUI.
     *
     */
    void repaint();

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
    void paintMark(int i, int j, Player player, int step) throws IndexOutOfBoundsException, IllegalArgumentException;

    /**
     * Pop out a message on the GUI.
     *
     * @param result The input String that will display on the GUI
     */
    void popUp(String result);
}
