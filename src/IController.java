/**
 * The IController interface controls the process of the Five in A Row game.
 */
public interface IController {
    /**
     * Clear the board and repaint the board.
     */
    void clearAndRepaintBoard();

    /**
     * Run the AI vs AI game mode.
     */
    void runAIVsAI();

    /**
     * Moves one step of AI player.
     *
     * @param depth The input depth for the minimax algorithm
     * @throws IllegalArgumentException When the depth not positive
     */
    void oneAIStep(int depth) throws IllegalArgumentException;

    /**
     * Moves one step of human player.
     *
     * @param row The input row on the board
     * @param column The input column on the board
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    void oneHumanStep(int row, int column) throws IndexOutOfBoundsException;

    /**
     * Implement the actions when one specified position is clicking on the board based on the game mode and player turn.
     *
     * @param row The row index of the clicking position on the board
     * @param column The column index of the clicking position on the board
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
   void go(int row, int column)  throws IndexOutOfBoundsException;
}
