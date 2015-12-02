import static org.junit.Assert.*;
import org.junit.*;

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

// From Expr.java 
else if (UtilIan.hasNextSplittingBy(s, IfZeroExpr.TOKEN, PUNCTUATION)) {
    UtilIan.nextSplittingBy(s, PUNCTUATION);  // consume the "if"
    Expr subExpr1 = parse(s);
    UtilIan.nextSplittingBy(s, PUNCTUATION);  // Consume the `is zero then`
    Expr subExpr2 = parse(s);
    UtilIan.nextSplittingBy(s, PUNCTUATION);  // Consume the `else`
    Expr subExpr3 = parse(s);
    assert UtilIan.hasNextChar(s,'@') : "`@` must close " + IfZeroExpr.TOKEN;
    UtilIan.nextChar(s,'@');  // Consume the closing ;
    return new IfZeroExpr(subExpr1,subExpr2,subExpr3);
}

// From BinExpr.java
else if (this.op.equals("mod")) {
    return new Num(rightValue * ((leftValue/rightValue) - (Math.floor(leftValue/rightValue)))); // y*(x/y-floor(x/y))
}