import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/** Test the Position class. */
public class PositionTest {
  private Position position;

  /** Set up the object used for the test. */
  @Before
  public void setUp() {
    position = new Position(3, 4);
  }

  /** Test getter of the row field in the Position class. */
  @Test
  public void getRowTest() {
    assertEquals(3, position.getRow());
  }

  /** Test getter of the column field in the Position class. */
  @Test
  public void getColumnTest() {
    assertEquals(4, position.getColumn());
  }
}
