import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class ParityExprTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class ParityExprTest {
    
    @Test
    public void equalsTest() {
      ParityExpr e1 = new ParityExpr( new Num(3), new Num(4), new Num(5) );
      ParityExpr e2 = new ParityExpr( new Num(3), new Num(4), new Num(5) );
      ParityExpr e3 = new ParityExpr( new Num(7), new Num(4), new Num(5) );
      assertEquals(e1,e1);
      assertEquals(e1,e2);
      assertFalse( e1.equals(e3) );
      assertEquals( e1.hashCode(), e1.hashCode() );
      assertEquals( e1.hashCode(), e2.hashCode() );
      assertFalse(  e1.hashCode() == e3.hashCode() );
      }
    
    }
