/**
 * The IModel interface is the model of the Five In A Row game. It provides the minimax algorithm alpha-beta pruning
 * supporting the AI player to find the best move given a board state. It provides the method to check if the game
 * is over and return who is the winner. It provides the evaluate method to get the score of one board state, which
 * is used in the minimax algorithm while it reaches the leaf node.
 */
public interface IModel {
    /**
     * Get the boardSize.
     *
     * @return the boardSize of the Model
     */
    int getBoardSize();

    /**
     * Get the turn.
     *
     * @return the current turn of the Model
     */
    int getTurn();

    /**
     * Get the mode.
     *
     * @return the mode of the Model
     */
    int getMode();

    /**
     * Get the lastPosition.
     *
     * @return the lastPosition of the Model
     */
    Position getLastPosition();

    /**
     * Set the turn of the Model.
     *
     * @param i the input turn
     */
    void setTurn(int i);

    /**
     * Set the mode of the Model.
     *
     * @param i the input model
     */
    void setMode(int i);

    /**
     * Get the h of the Model.
     *
     * @return the h of the Model
     */
    int getH();

    /**
     * Get the step of the Model.
     *
     * @return the current step number of the Model
     */
    int getStep();

    /**
     * Set the step of the Model.
     *
     * @param i the input step
     */
    void setStep(int i);

    /**
     * Place a new piece on the input position.
     *
     * @param position where to place the new piece
     * @param player whose turn
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    void placeChessOnPosition(Position position, Player player) throws IndexOutOfBoundsException;

    /**
     * Get the piece of the input position on the board.
     *
     * @param position the input position
     * @return the whose piece is on that position
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    Player getStateOnPosition(Position position) throws IndexOutOfBoundsException;

    /**
     * Get a clone board of the current board of the Model.
     *
     * @return the clone board of the current board
     */
    Player[][] getCloneBoard();

    /**
     * Get a clone board of the input board.
     *
     * @param board the input board
     * @return a clone board of the input board
     */
    Player[][] getCloneBoard(Player[][] board);

    /**
     * Clear the board.
     */
    void clearBoard();

    /**
     * Check whether there is empty position on the board.
     *
     * @return true iif there is empty position on the board
     */
    boolean checkEmpty();

    /**
     * Check whether the game is over and return the winner. Looping over the board by row, column, and diagonal to
     * figure out whether there is consecutive five.
     *
     * @return the winner if there is a winner, otherwise return Player.EMPTY
     */
    Player getWinner();

    /**
     * A helper function to evaluate an input sequence. Return a higher score if there are more consecutive pieces.
     * Return a lower score if there are more blocks on the two sides.
     *
     * @param count how many consecutive pieces
     * @param blocks how many blocks on the two sides
     * @param currentTurn is the player's current turn?
     * @return the score calculated from the input parameters
     */
    int getConsecutiveScore(int count, int blocks, boolean currentTurn);

    /**
     * Evaluate the score of one input board by calculated all the sequences of all rows.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all rows
     */
    int evaluateRow(Player[][] board, boolean forX, boolean XTurn);

    /**
     * Evaluate the score of one input board by calculated all the sequences of all columns.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all columns
     */
    int evaluateColumn(Player[][] board, boolean forX, boolean XTurn);

    /**
     * Evaluate the score of one input board by calculated all the sequences of all diagonals.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all diagonals
     */
    int evaluateDiagonal(Player[][] board, boolean forX, boolean XTurn);

    /**
     * Evaluate the input board using the evaluate functions on three directions: row, column, and diagonal.
     *
     * @param board The input board
     * @param XTurn whether the current turn is the Player.X
     * @return the relative score of the Player.O to Player.X by dividing
     */
    double evaluate(Player[][] board, boolean XTurn);

    /**
     * Calculate the best move for the AI player using the minimax algorithm alpha-beta pruning.
     *
     * @param depth the depth of the minimax algorithm
     * @param board the input board
     * @param maximizingPlayer whether the turn is maximizer
     * @param alpha the alpha value for alpha-beta pruning
     * @param beta the beta value for alpha-beta pruning
     * @return {the score of the input board, row index of the best move, column index of the best move}
     */
    Object[] minimax(int depth, Player[][] board,  boolean maximizingPlayer, double alpha, double beta);

    /**
     * Finish an AI move using the best move calculated from the minimax method.
     */
    void moveAI();
}
