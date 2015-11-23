import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BinExprTest {
    
    @Test
    public void equalsTest() {
      BinExpr e1 = new BinExpr(new Num(4), "add", new Num(5));
      BinExpr e2 = new BinExpr(new Num(4), "add", new Num(5));
      BinExpr e3 = new BinExpr(new Num(5), "add", new Num(4));
      assertEquals( e1, e1 );
      assertEquals( e1, e2 );
      assertFalse(  e1.equals(e3) );
      assertEquals( e1.hashCode(), e1.hashCode() );
      assertEquals( e1.hashCode(), e2.hashCode() );
      assertFalse(  e1.hashCode() == e3.hashCode() );
      }
    
    }


