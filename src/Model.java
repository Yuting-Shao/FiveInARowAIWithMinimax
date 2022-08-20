/**
 * The Model class is the model of the Five In A Row game. It represents the board of the game using a two dimensional
 * array. It provides the minimax algorithm alpha-beta pruning supporting the AI player to find the best move given a
 * board state. It provides the method to check if the game is over and return who is the winner. It provides the evaluate
 * method to get the score of one board state, which is used in the minimax algorithm while it reaches the leaf node.
 */
public class Model implements IModel{
    // Represent the board using a 2D array
    private Player[][] board;
    // The number of row or column in the board
    private int boardSize;
    // turn = 0 means the game is not started yet; turn = 1 means this is Player.X turn; turn = 2 means this is Player.O turn
    private int turn;
    // mode = 0 means it is the human vs human mode; mode = 1 means it is the human vs AI mode; mode = 2 means it is
    // the AI vs AI mode.
    private int mode;
    // Record the newest added piece on the board
    private Position lastPosition;
    // The depth of the minimax algorithm
    private int h;
    // The current step number of the game
    private int step;

    /**
     * Constructor of the Model class. It will initialize the fields of the Model class.
     */
    public Model(){
        boardSize = 15;
        board = new Player[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                board[i][j] = Player.EMPTY;
            }
        }
        // Game not start yet
        turn = 0;
        // Default mode: human vs human
        mode = 0;
        // Depth of the minimax algorithm
        h = 3;
        // Initial step
        step = 0;
    }

    /**
     * Get the boardSize.
     *
     * @return the boardSize of the Model
     */
    @Override
    public int getBoardSize(){
        return boardSize;
    }

    /**
     * Get the turn.
     *
     * @return the current turn of the Model
     */
    @Override
    public int getTurn() {
        return turn;
    }

    /**
     * Get the mode.
     *
     * @return the mode of the Model
     */
    @Override
    public int getMode(){
        return mode;
    }

    /**
     * Get the lastPosition.
     *
     * @return the lastPosition of the Model
     */
    @Override
    public Position getLastPosition(){
        return lastPosition;
    }

    /**
     * Set the turn of the Model.
     *
     * @param i the input turn
     */
    @Override
    public void setTurn(int i){
        turn = i;
    }

    /**
     * Set the mode of the Model.
     *
     * @param i the input model
     */
    @Override
    public void setMode(int i){
        mode = i;
    }

    public void setH(int i) {
        h = i;
    }

    /**
     * Get the h of the Model.
     *
     * @return the h of the Model
     */
    @Override
    public int getH(){
        return h;
    }

    /**
     * Get the step of the Model.
     *
     * @return the current step number of the Model
     */
    @Override
    public int getStep(){
        return step;
    }

    /**
     * Set the step of the Model.
     *
     * @param i the input step
     */
    @Override
    public void setStep(int i){
        step = i;
    }

    /**
     * Place a new piece on the input position.
     *
     * @param position where to place the new piece
     * @param player whose turn
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    @Override
    public void placeChessOnPosition(Position position, Player player) throws IndexOutOfBoundsException{
        if(position.getRow() < 0 || position.getRow() > 14){
            throw new IndexOutOfBoundsException("Index of row must between 0 and 14.");
        }
        if(position.getColumn() < 0 || position.getColumn() > 14){
            throw new IndexOutOfBoundsException("Index of column must between 0 and 14.");
        }
        board[position.getRow()][position.getColumn()] = player;
        lastPosition = position;
    }

    /**
     * Get the piece of the input position on the board.
     *
     * @param position the input position
     * @return the whose piece is on that position
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    @Override
    public Player getStateOnPosition(Position position) throws IndexOutOfBoundsException{
        if(position.getRow() < 0 || position.getRow() > 14){
            throw new IndexOutOfBoundsException("Index of row must between 0 and 14.");
        }
        if(position.getColumn() < 0 || position.getColumn() > 14){
            throw new IndexOutOfBoundsException("Index of column must between 0 and 14.");
        }
        return board[position.getRow()][position.getColumn()];
    }

    /**
     * Get a clone board of the current board of the Model.
     *
     * @return the clone board of the current board
     */
    @Override
    public Player[][] getCloneBoard(){
        Player[][] cloneBoard;
        cloneBoard = new Player[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                cloneBoard[i][j] = board[i][j];
            }
        }
        return cloneBoard;
    }

    /**
     * Get a clone board of the input board.
     *
     * @param board the input board
     * @return a clone board of the input board
     */
    @Override
    public Player[][] getCloneBoard(Player[][] board){
        Player[][] cloneBoard;
        cloneBoard = new Player[boardSize][boardSize];
        for(int i = 0; i < boardSize; i++){
            for(int j = 0; j < boardSize; j++){
                cloneBoard[i][j] = board[i][j];
            }
        }
        return cloneBoard;
    }

    /**
     * Clear the board.
     */
    @Override
    public void clearBoard(){
        for(int i = 0; i < getBoardSize(); i++){
            for(int j = 0; j < getBoardSize(); j++){
                placeChessOnPosition(new Position(i, j), Player.EMPTY);
            }
        }
    }

    /**
     * Check whether there is empty position on the board.
     *
     * @return true iif there is empty position on the board
     */
    @Override
    public boolean checkEmpty(){
        boolean hasEmpty = false;
        for(int i = 0; i < getBoardSize(); i++){
            for(int j = 0; j < getBoardSize(); j++){
                if(board[i][j] == Player.EMPTY){
                    hasEmpty = true;
                }
            }
        }
        return hasEmpty;
    }

    /**
     * Check whether the game is over and return the winner. Looping over the board by row, column, and diagonal to
     * figure out whether there is consecutive five.
     *
     * @return the winner if there is a winner, otherwise return Player.EMPTY
     */
    @Override
    public Player getWinner() {
        // Check the row
        for(int c = Math.max(lastPosition.getColumn() - 4, 0); c < Math.min(11, lastPosition.getColumn() + 5); c++){
            if(getStateOnPosition(new Position(lastPosition.getRow(), c)) == Player.X &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+1)) == Player.X &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+2)) == Player.X &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+3)) == Player.X &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+4)) == Player.X){
                //Player X win because five in a row
                return Player.X;
            }
            else if(getStateOnPosition(new Position(lastPosition.getRow(), c)) == Player.O &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+1)) == Player.O &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+2)) == Player.O &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+3)) == Player.O &&
                    getStateOnPosition(new Position(lastPosition.getRow(), c+4)) == Player.O){
                //Player O win because five in a row
                return Player.O;
            }
        }
        // Check the column
        for(int r = Math.max(lastPosition.getRow() - 4, 0); r < Math.min(11, lastPosition.getRow() + 5); r++){
            if(getStateOnPosition(new Position(r, lastPosition.getColumn())) == Player.X &&
                    getStateOnPosition(new Position(r+1, lastPosition.getColumn())) == Player.X &&
                    getStateOnPosition(new Position(r+2, lastPosition.getColumn())) == Player.X &&
                    getStateOnPosition(new Position(r+3, lastPosition.getColumn())) == Player.X &&
                    getStateOnPosition(new Position(r+4, lastPosition.getColumn())) == Player.X){
                //Player X win because five in a column
                return Player.X;
            }
            else if(getStateOnPosition(new Position(r, lastPosition.getColumn())) == Player.O &&
                    getStateOnPosition(new Position(r+1, lastPosition.getColumn())) == Player.O &&
                    getStateOnPosition(new Position(r+2, lastPosition.getColumn())) == Player.O &&
                    getStateOnPosition(new Position(r+3, lastPosition.getColumn())) == Player.O &&
                    getStateOnPosition(new Position(r+4, lastPosition.getColumn())) == Player.O){
                //Player O win because five in a column
                return Player.O;
            }
        }
        // Check the diagonal
        for(int r = Math.max(lastPosition.getRow() - 4, 0); r < Math.min(11, lastPosition.getRow() + 5); r++){
            // Skip the outbound index
            try{
                if(getStateOnPosition(new Position(r, lastPosition.getColumn() - lastPosition.getRow() + r)) == Player.X &&
                        getStateOnPosition(new Position(r+1, lastPosition.getColumn() - lastPosition.getRow() + r + 1)) == Player.X &&
                        getStateOnPosition(new Position(r+2, lastPosition.getColumn() - lastPosition.getRow() + r + 2)) == Player.X &&
                        getStateOnPosition(new Position(r+3, lastPosition.getColumn() - lastPosition.getRow() + r + 3)) == Player.X &&
                        getStateOnPosition(new Position(r+4, lastPosition.getColumn() - lastPosition.getRow() + r + 4)) == Player.X){
                    //Player X win because five in diagonal
                    return Player.X;
                }
                else if(getStateOnPosition(new Position(r, lastPosition.getColumn() - lastPosition.getRow() + r)) == Player.O &&
                        getStateOnPosition(new Position(r+1, lastPosition.getColumn() - lastPosition.getRow() + r + 1)) == Player.O &&
                        getStateOnPosition(new Position(r+2, lastPosition.getColumn() - lastPosition.getRow() + r + 2)) == Player.O &&
                        getStateOnPosition(new Position(r+3, lastPosition.getColumn() - lastPosition.getRow() + r + 3)) == Player.O &&
                        getStateOnPosition(new Position(r+4, lastPosition.getColumn() - lastPosition.getRow() + r + 4)) == Player.O){
                    //Player O win because five in diagonal
                    return Player.O;
                }
            }
            catch (Exception e){

            }
        }
        // Check another diagonal
        for(int r = 0; r < Math.max(11, lastPosition.getRow() - 4); r++){
            // Skip the outbound index
            try{
                if(getStateOnPosition(new Position(r, lastPosition.getColumn() + lastPosition.getRow() - r)) == Player.X &&
                        getStateOnPosition(new Position(r+1, lastPosition.getColumn() + lastPosition.getRow() - r - 1)) == Player.X &&
                        getStateOnPosition(new Position(r+2, lastPosition.getColumn() + lastPosition.getRow() - r - 2)) == Player.X &&
                        getStateOnPosition(new Position(r+3, lastPosition.getColumn() + lastPosition.getRow() - r - 3)) == Player.X &&
                        getStateOnPosition(new Position(r+4, lastPosition.getColumn() + lastPosition.getRow() - r - 4)) == Player.X){
                    //Player X win because five in diagonal
                    return Player.X;
                }
                else if(getStateOnPosition(new Position(r, lastPosition.getColumn() + lastPosition.getRow() - r)) == Player.O &&
                        getStateOnPosition(new Position(r+1, lastPosition.getColumn() + lastPosition.getRow() - r - 1)) == Player.O &&
                        getStateOnPosition(new Position(r+2, lastPosition.getColumn() + lastPosition.getRow() - r - 2)) == Player.O &&
                        getStateOnPosition(new Position(r+3, lastPosition.getColumn() + lastPosition.getRow() - r - 3)) == Player.O &&
                        getStateOnPosition(new Position(r+4, lastPosition.getColumn() + lastPosition.getRow() - r - 4)) == Player.O){
                    //Player O win because five in diagonal
                    return Player.O;
                }
            }
            catch (Exception e){

            }
        }
        return Player.EMPTY;
    }

    /**
     * A helper function to evaluate an input sequence. Return a higher score if there are more consecutive pieces.
     * Return a lower score if there are more blocks on the two sides.
     *
     * @param count how many consecutive pieces
     * @param blocks how many blocks on the two sides
     * @param currentTurn is the player's current turn?
     * @return the score calculated from the input parameters
     */
    @Override
    public int getConsecutiveScore(int count, int blocks, boolean currentTurn){
        // If both sides are blocked by the opponent and the consecutive pieces is less than 5, return zero score
        // because there is no possibility of win
        if(blocks == 2 && count < 5){
            return 0;
        }
        switch (count){
            case 5:{
                // This is 5 consecutive pieces, so the player is already win, returns a very high score
                return 100000000;
            }
            case 4:{
                // This is 4 consecutive pieces, if the current turn is the player's turn, then the next move will
                // lead to win, so returns a high score;
                if(currentTurn){
                    return 1000000;
                }
                else{
                    // If the current turn is the opponent's turn and both sides are empty, the opponent can not
                    // block two sides in one step, so the next move will lead to win. Returns a high score.
                    // If only one side is empty, the opponent can block in next step, so return 200 score.
                    if(blocks == 0){
                        return 1000000 / 4;
                    } else{
                        return 200;
                    }
                }
            }
            case 3:{
                // This is 3 consecutive pieces, if the current turn is the player and the two side are empty, one move
                // will lead to the 4 consecutive pieces case, then it is win. Returns 50000 score. If the current turn
                // is the opponent's turn, it will be blocked after the opponent's move. Returns 200 score. If one sides
                // is blocked and it is the player's turn, returns 10 score. If one sides is blocked and it is the
                // opponent's turn, returns 5 score.
                if(blocks == 0){
                    if(currentTurn) {
                        return 50000;
                    }
                    else{
                        return 200;
                    }
                }
                else{
                    if(currentTurn){
                        return 10;
                    }
                    else{
                        return 5;
                    }
                }
            }
            case 2:{
                // This is 2 consecutive pieces, if the current turn is the player and two sides are empty, returns 7 score.
                // If one side is blocked, returns 3 score. If it is the opponent's turn and two sides are empty, returns
                // 5 score.
                if(blocks == 0){
                    if(currentTurn){
                        return 7;
                    }
                    else{
                        return 5;
                    }
                }
                else{
                    return 3;
                }
            }
            case 1:{
                // This is 1 piece. Returns 1 score.
                return 1;
            }
        }
        // This is more than 5 consecutive pieces, which should not happen.
        return 100000000*2;
    }

    /**
     * Evaluate the score of one input board by calculated all the sequences of all rows.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all rows
     */
    @Override
    public int evaluateRow(Player[][] board, boolean forX, boolean XTurn){
        // how many consecutive piece forming a sequence
        int consecutive = 0;
        // how many blocks on the two sides of the sequence
        int block = 2;
        // sum of the scores
        int score = 0;
        for(int i=0;i<boardSize;i++){
            for(int j=0;j<boardSize;j++){
                if(board[i][j] == (forX ? Player.X : Player.O)){
                    consecutive++;
                }
                else if(board[i][j] == Player.EMPTY){
                    if(consecutive > 0){
                        block--;
                        score += getConsecutiveScore(consecutive, block, forX == XTurn);
                        consecutive = 0;
                        block = 1;
                    }
                    else{
                        block = 1;
                    }
                }
                else if(consecutive > 0){
                    score += getConsecutiveScore(consecutive, block, forX == XTurn);
                    consecutive = 0;
                    block = 2;
                }
                else{
                    block = 2;
                }
            }
            if(consecutive > 0){
                score += getConsecutiveScore(consecutive, block, forX == XTurn);
            }
            consecutive = 0;
            block = 2;
        }
        return score;
    }

    /**
     * Evaluate the score of one input board by calculated all the sequences of all columns.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all columns
     */
    @Override
    public int evaluateColumn(Player[][] board, boolean forX, boolean XTurn){
        // how many consecutive piece forming a sequence
        int consecutive = 0;
        // how many blocks on the two sides of the sequence
        int block = 2;
        // sum of the scores
        int score = 0;
        for(int j=0;j<boardSize;j++){
            for(int i=0;i<boardSize;i++){
                if(board[i][j] == (forX ? Player.X : Player.O)){
                    consecutive++;
                }
                else if(board[i][j] == Player.EMPTY){
                    if(consecutive > 0){
                        block--;
                        score += getConsecutiveScore(consecutive, block, forX == XTurn);
                        consecutive = 0;
                        block = 1;
                    }
                    else{
                        block = 1;
                    }
                }
                else if(consecutive > 0){
                    score += getConsecutiveScore(consecutive, block, forX == XTurn);
                    consecutive = 0;
                    block = 2;
                }
                else{
                    block = 2;
                }
            }
            if(consecutive > 0){
                score += getConsecutiveScore(consecutive, block, forX == XTurn);
            }
            consecutive = 0;
            block = 2;
        }
        return score;
    }

    /**
     * Evaluate the score of one input board by calculated all the sequences of all diagonals.
     *
     * @param board the input board
     * @param forX whether it is calculating the score for Player.X
     * @param XTurn whether it is the Player.X's turn
     * @return the sum of the scores of all sequences of all diagonals
     */
    @Override
    public int evaluateDiagonal(Player[][] board, boolean forX, boolean XTurn){
        // how many consecutive piece forming a sequence
        int consecutive = 0;
        // how many blocks on the two sides of the sequence
        int block = 2;
        // sum of the scores
        int score = 0;
        // bottom-left to top-right
        for(int k = 0; k < 2 * (boardSize-1);k++){
            int iStart = Math.max(0, k-boardSize+1);
            int iEnd = Math.min(boardSize-1,k);
            for(int i = iStart; i <= iEnd; i++){
                int j = k -i;
                if(board[i][j] == (forX ? Player.X : Player.O)){
                    consecutive++;
                }
                else if(board[i][j] == Player.EMPTY){
                    if(consecutive > 0){
                        block--;
                        score += getConsecutiveScore(consecutive, block, forX == XTurn);
                        consecutive = 0;
                        block = 1;
                    }
                    else{
                        block = 1;
                    }
                }
                else if(consecutive > 0){
                    score += getConsecutiveScore(consecutive, block, forX == XTurn);
                    consecutive = 0;
                    block = 2;
                }
                else{
                    block = 2;
                }
            }
            if(consecutive > 0){
                score += getConsecutiveScore(consecutive, block, forX == XTurn);
            }
            consecutive = 0;
            block = 2;
        }

        // top-left to bottom-right
        for(int k = 1-boardSize;k<boardSize;k++){
            int iStart = Math.max(0, k);
            int iEnd = Math.min(boardSize+k-1,boardSize-1);
            for(int i = iStart;i<=iEnd;i++){
                int j = i - k;
                if(board[i][j] == (forX ? Player.X : Player.O)){
                    consecutive++;
                }
                else if(board[i][j] == Player.EMPTY){
                    if(consecutive > 0){
                        block--;
                        score += getConsecutiveScore(consecutive, block, forX == XTurn);
                        consecutive = 0;
                        block = 1;
                    }
                    else{
                        block = 1;
                    }
                }
                else if(consecutive > 0){
                    score += getConsecutiveScore(consecutive, block, forX == XTurn);
                    consecutive = 0;
                    block = 2;
                }
                else{
                    block = 2;
                }
            }
            if(consecutive > 0){
                score += getConsecutiveScore(consecutive, block, forX == XTurn);
            }
            consecutive = 0;
            block = 2;
        }
        return score;
    }

    /**
     * Evaluate the input board using the evaluate functions on three directions: row, column, and diagonal.
     *
     * @param board The input board
     * @param XTurn whether the current turn is the Player.X
     * @return the relative score of the Player.O to Player.X by dividing
     */
    @Override
    public double evaluate(Player[][] board, boolean XTurn){
        double Xscore = evaluateRow(board, true, XTurn) + evaluateColumn(board, true, XTurn) + evaluateDiagonal(board,true, XTurn);
        double Oscore = evaluateRow(board,false, XTurn) + evaluateColumn(board,false, XTurn) + evaluateDiagonal(board,false, XTurn);
        if(Xscore == 0){
            Xscore = 1.0;
        }
        return Oscore / Xscore;
    }

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
    @Override
    public Object[] minimax(int depth, Player[][] board,  boolean maximizingPlayer, double alpha, double beta){
        // base case: leaf node in the minimax tree
        if(depth == 0){
            Object[] x = {evaluate(board,!maximizingPlayer), null, null};
            return x;
        }
        Object[] best = new Object[3];
        int emptyCount = 0;
        if(maximizingPlayer){
            best[0] = -1.0;
            for(int i = 0; i < boardSize; i++){
                for(int j = 0; j < boardSize; j++){
                    // Looping over the board to figure out possible moves
                    if(board[i][j] == Player.EMPTY){
                        Player[][] cloneBoard = getCloneBoard(board);
                        emptyCount++;
                        cloneBoard[i][j] = Player.O;
                        // Calculate the score of the new board after this move
                        Object[] temp = minimax(depth-1, cloneBoard, !maximizingPlayer, alpha, beta);
                        if((Double)(temp[0]) > alpha){
                            alpha = (Double)(temp[0]);
                        }
                        if((Double)(temp[0]) >= beta){
                            return temp;
                        }
                        // Update the best if this is a better move based on the score
                        if((Double)temp[0] > (Double)best[0]){
                            best = temp;
                            best[1] = i;
                            best[2] = j;
                        }
                    }
                }
            }
        }
        else{
            best[0] = 100000000.0;
            for(int i = 0; i < boardSize; i++){
                for(int j = 0; j < boardSize; j++){
                    // Looping over the board to figure out possible moves
                    if(board[i][j] == Player.EMPTY){
                        Player[][] cloneBoard = getCloneBoard(board);
                        emptyCount++;
                        cloneBoard[i][j] = Player.X;
                        // Calculate the score of the new board after this move
                        Object[] temp = minimax(depth-1, cloneBoard, !maximizingPlayer, alpha, beta);
                        if((Double)(temp[0]) < beta){
                            beta = (Double)(temp[0]);
                        }
                        if((Double)(temp[0]) <= alpha){
                            return temp;
                        }
                        // Update the best if this is a better move based on the score
                        if((Double)temp[0] < (Double)best[0]){
                            best = temp;
                            best[1] = i;
                            best[2] = j;
                        }
                    }
                }
            }
        }
        // The board is full, this is a leaf node
        if(emptyCount == 0){
            Object[] x = {evaluate(board,!maximizingPlayer), null, null};
            return x;
        }
        return best;
    }

    /**
     * Finish an AI move using the best move calculated from the minimax method.
     */
    @Override
    public void moveAI(){
        Position newPosition = new Position();
        Position tempPosition = new Position();
        // Looping over the whole board to see that whether there is a possible move to lead to win
        if(getTurn() == 1){
            for(int i = 0; i < boardSize; i++){
                for(int j = 0; j < boardSize; j++){
                    if(board[i][j] == Player.EMPTY){
                        board[i][j] = Player.X;
                        tempPosition = lastPosition;
                        lastPosition = new Position(i, j);
                        if(getWinner() == Player.X){
                            return;
                        }
                        board[i][j] = Player.EMPTY;
                        lastPosition = tempPosition;
                    }
                }
            }
        }
        else if(getTurn() == 2){
            for(int i = 0; i < boardSize; i++){
                for(int j = 0; j < boardSize; j++){
                    if(board[i][j] == Player.EMPTY){
                        board[i][j] = Player.O;
                        tempPosition = lastPosition;
                        lastPosition = new Position(i, j);
                        if(getWinner() == Player.O){
                            return;
                        }
                        board[i][j] = Player.EMPTY;
                        lastPosition = tempPosition;
                    }
                }
            }
        }

        // Using the minimax method to find the best move of the current board
        Object[] best = new Object[3];
        if(getTurn() == 1) {
            best = minimax(h, board, true, -1.0, 100000000);
        }
        else if(getTurn() == 2){
            best = minimax(h, board, false, -1.0, 100000000);
        }
        newPosition = new Position((Integer)(best[1]), (Integer)(best[2]));
        // update the board based on the best move
        if(getTurn() == 1){
            board[newPosition.getRow()][newPosition.getColumn()] = Player.X;
        }
        else if(getTurn() == 2){
            board[newPosition.getRow()][newPosition.getColumn()] = Player.O;
        }
        lastPosition = newPosition;
        return;
    }
}
