import java.awt.*;
import java.awt.event.InputEvent;
import static org.junit.Assert.*;

/**
 * Test the methods of the Controller class. Test the function of different buttons using the
 * doClick method of the button object. Test the mouse clicking using the Robot class, which can
 * generate native system input events for the purposes of test automation, self-running where the
 * control of the mouse is needed. Test the move method of AI and Human by creating a board with
 * some pieces already pre-placed on the board and assert that the next piece will be placed on the
 * expected position. Several windows will be pop up during the test, you might need to close the
 * window to go forward.
 */
public class ControllerTest {
  private Model model;
  private View view;
  private Controller controller;

  /** Set up the controller for each test. */
  @org.junit.Before
  public void setUp() {
    model = new Model();
    view = new View("Test Controller");
    controller = new Controller(model, view);
  }

  /** Test the board can be reset to empty by the clearAndRepaintBoard method. */
  @org.junit.Test
  public void resetBoardTest() {
    controller.clearAndRepaintBoard();
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        assertEquals(Player.EMPTY, view.getCloneBoard()[i][j]);
      }
    }
    // after resetting, the turn and step of model should be zero
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
  }

  /** Test the case that new game button is clicked. */
  @org.junit.Test
  public void newGameButtonClickedTest() {
    view.getNewGameButton().doClick();
    // turn being 1 means that the game is started
    assertEquals(1, model.getTurn());
  }

  /** Test the case that abort button is clicked by the Player.X. It should pop up White wins. */
  @org.junit.Test
  public void abortButtonClickedPlayerXTest() {
    model.setTurn(1);
    view.getAbortButton().doClick();
    // game over, the turn and step of model should be zero
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
  }

  /** Test the case that abort button is clicked by the Player.O. It should pop up Black wins. */
  @org.junit.Test
  public void abortButtonClickedPlayerOTest() {
    model.setTurn(2);
    view.getAbortButton().doClick();
    // game over, the turn and step of model should be zero
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
  }

  /**
   * Test the case that the author button is clicked by the user. The author information should be
   * displayed.
   */
  @org.junit.Test
  public void authorButtonClickedTest() {
    view.getAuthorButton().doClick();
  }

  /**
   * Test the case that the readme button is clicked by the user. The readme text should be
   * displayed.
   */
  @org.junit.Test
  public void readmeButtonClickedTest() {
    view.getReadmeButton().doClick();
  }

  /** Test the choice of the JCombBox. */
  @org.junit.Test
  public void jCombBoxTest() {
    // check that the box contains right items
    assertTrue(view.getBoxes().getItemAt(0).equals("Human vs Human"));
    assertTrue(view.getBoxes().getItemAt(1).equals("Human vs AI"));
    assertTrue(view.getBoxes().getItemAt(2).equals("AI vs AI"));
    // selects Human vs Human mode, the mode should be zero
    view.getBoxes().setSelectedIndex(0);
    assertEquals(0, model.getMode());
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
    // selects Human vs AI mode, the mode should be 1
    view.getBoxes().setSelectedIndex(1);
    assertEquals(1, model.getMode());
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
    // selects AI vs AI mode, the mode should be 2
    view.getBoxes().setSelectedIndex(2);
    assertEquals(2, model.getMode());
    assertEquals(0, model.getTurn());
    assertEquals(0, model.getStep());
  }

  /**
   * Test the AI vs AI mode with the special case that all the positions are occupied on the board.
   * It should pop up A tie!
   */
  @org.junit.Test
  public void runAIVsAITest() {
    // create a full board
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        if ((i + j) % 2 == 1) {
          model.placeChessOnPosition(new Position(i, j), Player.X);
        } else {
          model.placeChessOnPosition(new Position(i, j), Player.O);
        }
      }
    }
    // remove two pieces on the board to create the initial board
    model.placeChessOnPosition(new Position(7, 7), Player.EMPTY);
    model.placeChessOnPosition(new Position(7, 8), Player.EMPTY);
    controller.runAIVsAI();
  }

  /**
   * Test one step of the AI player with a special case that only one position is left for the
   * Player.O.
   */
  @org.junit.Test
  public void oneAIStepTest() {
    // create a full board
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        if ((i + j) % 2 == 1) {
          model.placeChessOnPosition(new Position(i, j), Player.X);
        } else {
          model.placeChessOnPosition(new Position(i, j), Player.O);
        }
      }
    }
    // remove one piece on the board to create the initial board
    model.placeChessOnPosition(new Position(7, 8), Player.EMPTY);
    // set the turn for Player.O
    model.setTurn(2);
    controller.oneAIStep(model.getH());
    // assert that the piece is placed on the empty position
    assertEquals(Player.O, model.getStateOnPosition(new Position(7, 8)));
  }

  /** Test the controller can handle the clicking on the board using an empty board. */
  @org.junit.Test
  public void mouseClickedTest() throws AWTException {
    // selects Human vs Human mode
    view.getBoxes().setSelectedIndex(0);
    // start the new game
    view.getNewGameButton().doClick();
    // mouse click on (500, 400), which is 7 for row and 6 for column
    Robot bot = new Robot();
    bot.mouseMove(500, 400);
    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {

    }
    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(2500);
    } catch (InterruptedException e) {

    }

    // assert that the Player.X is already placed on the board with row index = 7 and column index =
    // 6
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        if (i == 7 && j == 6) {
          assertEquals(Player.X, model.getStateOnPosition(new Position(i, j)));
        } else {
          assertEquals(Player.EMPTY, model.getStateOnPosition(new Position(i, j)));
        }
      }
    }
  }

  /**
   * Test one step of the human player with a special case that Player.X places the first piece on
   * the empty board.
   */
  @org.junit.Test
  public void oneHumanStepTest() {
    // selects Human vs Human mode
    view.getBoxes().setSelectedIndex(0);
    // start the new game
    view.getNewGameButton().doClick();
    controller.oneHumanStep(6, 6);
    assertEquals(Player.X, model.getStateOnPosition(new Position(6, 6)));
  }

  /** Test one step of the human player with an invalid input index. */
  @org.junit.Test(expected = IndexOutOfBoundsException.class)
  public void oneHumanStepInvalidInputTest() {
    // selects Human vs Human mode
    view.getBoxes().setSelectedIndex(0);
    // start the new game
    view.getNewGameButton().doClick();
    // exception will be thrown because the index is > 14
    controller.oneHumanStep(16, 16);
  }

  /**
   * Test one step of go with a special case that Player.O places the first piece on the empty
   * board.
   */
  @org.junit.Test
  public void goTest() {
    // selects Human vs Human mode
    view.getBoxes().setSelectedIndex(0);
    // start the new game
    view.getNewGameButton().doClick();
    // set the turn to Player.O
    model.setTurn(2);
    controller.go(3, 4);
    assertEquals(Player.O, model.getStateOnPosition(new Position(3, 4)));
  }

  /** Test go with an invalid input index. */
  @org.junit.Test(expected = IndexOutOfBoundsException.class)
  public void goInvalidInputTest() {
    // selects Human vs Human mode
    view.getBoxes().setSelectedIndex(0);
    // start the new game
    view.getNewGameButton().doClick();
    model.setTurn(2);
    // exception will be thrown because the index is < 0
    controller.go(-1, 4);
  }
}
