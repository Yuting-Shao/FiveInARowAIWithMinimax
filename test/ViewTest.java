import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseListener;
import static org.junit.Assert.*;

/**
 * Test the View Class which provides the graphical user interface for the game. Make sure that the
 * button can be clicked by calling the doClick method of each button object. Make sure that the
 * mouse can be clicked on the board panel by the Robot class, which can generate native system
 * input events for the purposes of test automation, self-running where the control of the mouse is
 * needed. One message window will be pop up at last while testing the popUp method. You have to
 * close that manually.
 * References:
 * button clicked test -
 * https://stackoverflow.com/questions/62614105/how-to-junit-test-an-actionlistener
 * mouse clicked on board test -
 * https://stackoverflow.com/questions/16411823/junit-tests-for-gui-in-java
 */
public class ViewTest {
  private View view;

  /** Set up the view for each test. */
  @org.junit.Before
  public void setUp() {
    view = new View("test view");
  }

  /**
   * Test that all the buttons can be clicked. Reference:
   * https://stackoverflow.com/questions/62614105/how-to-junit-test-an-actionlistener
   */
  @org.junit.Test
  public void buttonsTest() {
    view.getNewGameButton().doClick();
    view.getAbortButton().doClick();
    view.getAuthorButton().doClick();
    view.getReadmeButton().doClick();
    view.getSourceButton().doClick();
  }

  /**
   * Test that the left board panel can be clicked. Reference:
   * https://stackoverflow.com/questions/16411823/junit-tests-for-gui-in-java
   */
  @org.junit.Test
  public void boardClickTest() throws AWTException {
    Robot bot = new Robot();
    bot.mouseMove(500, 400);
    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {

    }
    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
  }

  /** Test the display of the GUI. */
  @org.junit.Test
  public void displayTest() {
    view.display();
    view.getJFrame().setVisible(false);
  }

  /** Test the update of the clone board of the View. */
  @org.junit.Test
  public void updateCloneBoardTest() {
    Player[][] testBoard;
    testBoard = new Player[15][15];
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        testBoard[i][j] = Player.X;
      }
    }
    view.updateCloneBoard(testBoard);
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        assertEquals(testBoard[i][j], view.getCloneBoard()[i][j]);
      }
    }
  }

  /** Test that the JComboBox works. */
  @org.junit.Test
  public void jComboBoxTest() {
    view.getBoxes().getSelectedItem();
  }

  /** Test that the listeners can be set for the View. */
  @org.junit.Test
  public void setListenerTest() {
    Model m = new Model();
    View v = new View("testListener");
    ActionListener actionListener = new Controller(m, v);
    MouseListener mouseListener = new Controller(m, v);
    view.setListener(actionListener, mouseListener);
  }

  /** Test the repaint of the board. */
  @org.junit.Test
  public void repaintTest() {
    Player[][] testBoard;
    testBoard = new Player[15][15];
    for (int i = 0; i < 15; i++) {
      for (int j = 0; j < 15; j++) {
        testBoard[i][j] = Player.X;
      }
    }
    view.updateCloneBoard(testBoard);
    // Make sure that display the board before repaint
    view.display();
    view.repaint();
  }

  /** Test paint one piece on the board with valid inputs. */
  @org.junit.Test
  public void paintMarkTest() {
    view.display();
    view.paintMark(5, 5, Player.X, 1);
  }

  /** Test paint one piece on the board with invalid position inputs. */
  @org.junit.Test(expected = IndexOutOfBoundsException.class)
  public void paintMarkInvalidPositionTest() {
    view.display();
    view.paintMark(15, 15, Player.X, 1);
  }

  /** Test paint one piece on the board with invalid Player inputs. */
  @org.junit.Test(expected = IllegalArgumentException.class)
  public void paintMarkInvalidPlayerTest() {
    view.display();
    view.paintMark(10, 10, Player.EMPTY, 1);
  }

  /** Test paint one piece on the board with invalid step inputs. */
  @org.junit.Test(expected = IllegalArgumentException.class)
  public void paintMarkInvalidStepTest() {
    view.display();
    view.paintMark(10, 10, Player.X, -1);
  }

  /** Test the message can be pop up. */
  @org.junit.Test
  public void popUpTest() {
    view.popUp("TestPopUp");
  }
}
