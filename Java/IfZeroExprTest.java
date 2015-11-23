import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class IfZeroExprTest.
 *
 * @author  Forrest Meade (fmeade)
 * @version 2015.Nov.30
 */
public class IfZeroExprTest {
    
    @Test
    public void equalsTest() {
      IfZeroExpr e1 = new IfZeroExpr( new Num(3), new Num(4), new Num(5) );
      IfZeroExpr e2 = new IfZeroExpr( new Num(3), new Num(4), new Num(5) );
      IfZeroExpr e3 = new IfZeroExpr( new Num(7), new Num(4), new Num(5) );
      assertEquals(e1,e1);
      assertEquals(e1,e2);
      assertFalse( e1.equals(e3) );
      assertEquals( e1.hashCode(), e1.hashCode() );
      assertEquals( e1.hashCode(), e2.hashCode() );
      assertFalse(  e1.hashCode() == e3.hashCode() );
      }
    
    }
