import static org.junit.Assert.*;

/**
 * Test all the methods provided in the Model Class. The intelligence of the AI player is tested by
 * creating some board patterns to make sure that the AI player can make the right decision as
 * expected.
 */
public class ModelTest {
  private Model model;

  /** Set up the model used for each test. */
  @org.junit.Before
  public void setUp() {
    model = new Model();
  }

  /** The boardSize should be 15. */
  @org.junit.Test
  public void getBoardSizeTest() {
    assertEquals(15, model.getBoardSize());
  }

  /** Test the getter of the turn. */
  @org.junit.Test
  public void getTurnTest() {
    assertEquals(0, model.getTurn());
    model.setTurn(1);
    assertEquals(1, model.getTurn());
  }

  /** Test the getter of the mode. */
  @org.junit.Test
  public void getModeTest() {
    assertEquals(0, model.getMode());
    model.setMode(1);
    assertEquals(1, model.getMode());
  }

  /**
   * Test the getter of lastPosition. After place a new piece, the lastPosition should be updated.
   */
  @org.junit.Test
  public void getLastPositionTest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    assertEquals(0, model.getLastPosition().getRow());
    assertEquals(0, model.getLastPosition().getColumn());
  }

  /** Test the setter of the turn field. */
  @org.junit.Test
  public void setTurnTest() {
    model.setTurn(1);
    assertEquals(1, model.getTurn());
  }

  /** Test the setter of the mode field. */
  @org.junit.Test
  public void setModeTest() {
    model.setMode(1);
    assertEquals(1, model.getMode());
  }

  /** Test the setter of h. */
  @org.junit.Test
  public void setHTest() {
    model.setH(4);
    assertEquals(4, model.getH());
  }

  /** Test the getter of h. */
  @org.junit.Test
  public void getHTest() {
    assertEquals(3, model.getH());
  }

  /** Test the getter of step. */
  @org.junit.Test
  public void getStepTest() {
    assertEquals(0, model.getStep());
  }

  /** Test the setter of step. */
  @org.junit.Test
  public void setStepTest() {
    model.setStep(1);
    assertEquals(1, model.getStep());
  }

  /** Test place chess on one position and get the chess on one position. */
  @org.junit.Test
  public void placeChessOnPositionTest() {
    model.placeChessOnPosition(new Position(3, 4), Player.X);
    assertEquals(Player.X, model.getStateOnPosition(new Position(3, 4)));
  }

  /** Test place chess on one position while the index is out of range. */
  @org.junit.Test(expected = IndexOutOfBoundsException.class)
  public void placeChessOnPositionExceptionTest() {
    model.placeChessOnPosition(new Position(23, 4), Player.X);
  }

  /** Test get chess on one position while the index is out of range. */
  @org.junit.Test(expected = IndexOutOfBoundsException.class)
  public void getStateOnPositionExceptionTest() {
    model.getStateOnPosition(new Position(-1, -2));
  }

  /** Test the get clone board of the board field of the Model. */
  @org.junit.Test
  public void getCloneBoardTest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.O);
    Player[][] cloneBoard = model.getCloneBoard();
    assertEquals(Player.X, cloneBoard[0][0]);
    assertEquals(Player.O, cloneBoard[0][1]);
  }

  /** Test the get clone board of the board field of the Model. */
  @org.junit.Test
  public void getCloneBoardWithInputTest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.O);
    Player[][] cloneBoard = model.getCloneBoard();
    Player[][] cloneCloneBoard = model.getCloneBoard(cloneBoard);
    assertEquals(Player.X, cloneCloneBoard[0][0]);
    assertEquals(Player.O, cloneCloneBoard[0][1]);
  }

  /** Test clear the board of the Model. */
  @org.junit.Test
  public void clearBoardTest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.O);
    model.clearBoard();
    assertEquals(Player.EMPTY, model.getStateOnPosition(new Position(0, 0)));
    assertEquals(Player.EMPTY, model.getStateOnPosition(new Position(0, 1)));
  }

  /** Test check whether there is empty position on the board. */
  @org.junit.Test
  public void checkEmptyTest() {
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        model.placeChessOnPosition(new Position(i, j), Player.O);
      }
    }
    assertFalse(model.checkEmpty());
  }

  /**
   * Test get the winner method. Place 5 consecutive black pieces one by one. After placing the
   * first 4 pieces, the getWinner should return Player.EMPTY. After place the fifth piece, the
   * getWinner should return Player.X.
   */
  @org.junit.Test
  public void getWinnerTest() {
    for (int i = 0; i < 5; i++) {
      model.placeChessOnPosition(new Position(i, i), Player.X);
      if (i < 4) {
        assertEquals(Player.EMPTY, model.getWinner());
      } else {
        assertEquals(Player.X, model.getWinner());
      }
    }
  }

  /** Test get score for a specified sequence. */
  @org.junit.Test
  public void getConsecutiveScoreTest() {
    assertEquals(0, model.getConsecutiveScore(4, 2, true));
    assertEquals(100000000, model.getConsecutiveScore(5, 2, true));
    assertEquals(1000000, model.getConsecutiveScore(4, 1, true));
    assertEquals(1000000 / 4, model.getConsecutiveScore(4, 0, false));
    assertEquals(200, model.getConsecutiveScore(4, 1, false));
    assertEquals(50000, model.getConsecutiveScore(3, 0, true));
    assertEquals(200, model.getConsecutiveScore(3, 0, false));
    assertEquals(10, model.getConsecutiveScore(3, 1, true));
    assertEquals(5, model.getConsecutiveScore(3, 1, false));
    assertEquals(7, model.getConsecutiveScore(2, 0, true));
    assertEquals(5, model.getConsecutiveScore(2, 0, false));
    assertEquals(3, model.getConsecutiveScore(2, 1, true));
    assertEquals(3, model.getConsecutiveScore(2, 1, false));
    assertEquals(1, model.getConsecutiveScore(1, 1, false));
  }

  /**
   * Test evaluate the score of all rows. Place 3 pieces X at the first 3 position of each row and
   * set XTurn to be true. Then for each row the score should be 10. For the all rows, the sum
   * should be 150.
   */
  @org.junit.Test
  public void evaluateRowTest() {
    for (int i = 0; i < 15; i++) {
      model.placeChessOnPosition(new Position(i, 0), Player.X);
      model.placeChessOnPosition(new Position(i, 1), Player.X);
      model.placeChessOnPosition(new Position(i, 2), Player.X);
    }
    assertEquals(150, model.evaluateRow(model.getCloneBoard(), true, true));
  }

  /**
   * Test evaluate the score of all columns. Place 3 pieces O at the first 3 position of each column
   * and set XTurn to be false. Then for each row the score should be 10. For the all columns, the
   * sum should be 150.
   */
  @org.junit.Test
  public void evaluateColumnTest() {
    for (int i = 0; i < 15; i++) {
      model.placeChessOnPosition(new Position(0, i), Player.O);
      model.placeChessOnPosition(new Position(1, i), Player.O);
      model.placeChessOnPosition(new Position(2, i), Player.O);
    }
    assertEquals(150, model.evaluateColumn(model.getCloneBoard(), false, false));
  }

  /**
   * Test evaluate the score of diagonals. Place 3 pieces X at the main column with two sides being
   * empty. Then score should be 50003 if the currentTurn is true. The score should be 203 if the
   * currentTurn is false.
   */
  @org.junit.Test
  public void evaluateDiagonalTest() {
    model.placeChessOnPosition(new Position(1, 1), Player.X);
    model.placeChessOnPosition(new Position(2, 2), Player.X);
    model.placeChessOnPosition(new Position(3, 3), Player.X);
    assertEquals(50003, model.evaluateDiagonal(model.getCloneBoard(), true, true));
    assertEquals(203, model.evaluateDiagonal(model.getCloneBoard(), true, false));
    model.clearBoard();
    // another diagonal
    model.placeChessOnPosition(new Position(2, 13), Player.X);
    model.placeChessOnPosition(new Position(3, 12), Player.X);
    model.placeChessOnPosition(new Position(4, 11), Player.X);
    assertEquals(50003, model.evaluateDiagonal(model.getCloneBoard(), true, true));
    assertEquals(203, model.evaluateDiagonal(model.getCloneBoard(), true, false));
  }

  /**
   * Test evaluate the score of the following board state (only show the left top part of the board,
   * the rest are empty): _____ _XXX_ _XX__ _____ Set the current turn to be X. The score of all
   * rows is 50007. The score of all columns is 15. The score of all diagonals is 25. So the total
   * score is 50047 for Player.X. And it is zero for Player.O. So the evaluate method should return
   * 0. If the current turn is to be O and replace all the X with O on the board, the evaluate
   * method should return 50047.0.
   */
  @org.junit.Test
  public void evaluateTest() {
    model.placeChessOnPosition(new Position(1, 1), Player.X);
    model.placeChessOnPosition(new Position(1, 2), Player.X);
    model.placeChessOnPosition(new Position(1, 3), Player.X);
    model.placeChessOnPosition(new Position(2, 1), Player.X);
    model.placeChessOnPosition(new Position(2, 2), Player.X);
    assertEquals(0.0, model.evaluate(model.getCloneBoard(), true), 0.1);
    model.clearBoard();
    model.placeChessOnPosition(new Position(1, 1), Player.O);
    model.placeChessOnPosition(new Position(1, 2), Player.O);
    model.placeChessOnPosition(new Position(1, 3), Player.O);
    model.placeChessOnPosition(new Position(2, 1), Player.O);
    model.placeChessOnPosition(new Position(2, 2), Player.O);
    assertEquals(50047.0, model.evaluate(model.getCloneBoard(), false), 0.1);
  }

  /**
   * Test whether the minimax find the best move. Given the following board (only show the left top
   * part of the board, the rest are empty): XXXX___ _______ The minimax method should return the
   * move to block the four XXXX on the right side.
   */
  @org.junit.Test
  public void minimaxTest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.X);
    model.placeChessOnPosition(new Position(0, 2), Player.X);
    model.placeChessOnPosition(new Position(0, 3), Player.X);
    model.setTurn(2);
    Object[] best = new Object[3];
    best = model.minimax(model.getH(), model.getCloneBoard(), false, -1.0, 100000000);
    assertEquals(0, best[1]);
    assertEquals(4, best[2]);
  }

  /**
   * Test moveAI which place a new piece on the board based on the minimax algorithm with the
   * following board (only show the left top part of theboard, the rest are empty) XXXX___ _______ A
   * O should be placed on board[0][4] to block the XXXX. If it is the X turn, an X should be placed
   * there to win.
   */
  @org.junit.Test
  public void moveAITest() {
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.X);
    model.placeChessOnPosition(new Position(0, 2), Player.X);
    model.placeChessOnPosition(new Position(0, 3), Player.X);
    model.setTurn(2);
    model.moveAI();
    assertEquals(Player.O, model.getStateOnPosition(new Position(0, 4)));
    model.clearBoard();
    model.placeChessOnPosition(new Position(0, 0), Player.X);
    model.placeChessOnPosition(new Position(0, 1), Player.X);
    model.placeChessOnPosition(new Position(0, 2), Player.X);
    model.placeChessOnPosition(new Position(0, 3), Player.X);
    model.setTurn(1);
    model.moveAI();
    assertEquals(Player.X, model.getStateOnPosition(new Position(0, 4)));
    assertEquals(Player.X, model.getWinner());
  }
}
