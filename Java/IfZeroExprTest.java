import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IfZeroExprTest {
    
    @Test
    public void equalsTest() {
      ParenExpr e1 = new ParenExpr( new Num(3) );
      ParenExpr e2 = new ParenExpr( new Num(3) );
      ParenExpr e3 = new ParenExpr( new Num(7) );
      assertEquals(e1,e1);
      assertEquals(e1,e2);
      assertFalse(  e1.equals(e3) );
      assertEquals( e1.hashCode(), e1.hashCode() );
      assertEquals( e1.hashCode(), e2.hashCode() );
      assertFalse(  e1.hashCode() == e3.hashCode() );
      }
    }
