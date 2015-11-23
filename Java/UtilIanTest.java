import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * The test class UtilIanTest.
 *
 * @author  Forrest Meade (fmeade)
 * @author Ian Barland
 * @version 2015.Nov.30
 */
public class UtilIanTest {

    @Test
    public void mathTests() {
        assertEquals(3.000, UtilIan.intToDouble(3), 0.00001);
        assertEquals(-2.0, UtilIan.intToDouble(-2), 0.00001);
        assertEquals(4, UtilIan.doubleToInt(4.001));
        assertEquals(4, UtilIan.doubleToInt(4.9999));
        assertEquals(-4, UtilIan.doubleToInt(-4.0001));
        assertEquals(-3, UtilIan.doubleToInt(-3.9999));
        assertEquals(4, UtilIan.roundToInt(4.0001));
        assertEquals(4, UtilIan.roundToInt(3.9999));
        assertEquals(-4, UtilIan.roundToInt(-3.99999));
        assertEquals(-4, UtilIan.roundToInt(-4.0001));
        assertEquals(5, UtilIan.roundToInt(4.5));
        assertEquals(6, UtilIan.roundToInt(5.5));
        assertEquals(-3, UtilIan.roundToInt(-3.5));
        assertEquals(-4, UtilIan.roundToInt(-4.5));
        assertEquals(4.0, UtilIan.roundTo(4.001,0),0.000001);
        assertEquals(4.0, UtilIan.roundTo(3.999,0),0.000001);
        assertEquals(-4.0, UtilIan.roundTo(-4.001,0),0.000001);
        assertEquals(-4.0, UtilIan.roundTo(-3.999,0),0.000001);
        assertEquals(-4.01, UtilIan.roundTo(-4.009,2),0.000001);
        assertEquals(-3.99, UtilIan.roundTo(-3.991,2),0.000001);
        assertEquals(-4.00, UtilIan.roundTo(-4.001,2),0.000001);
        assertEquals(-4.00, UtilIan.roundTo(-3.996,2),0.000001);
        assertEquals(-3.50, UtilIan.roundTo(-3.505,2),0.000001);
        assertEquals(200.0, UtilIan.roundTo(170.0,-2),0.000001);
        assertEquals(200.0, UtilIan.roundTo(249.9,-2),0.000001);
        assertEquals(1e1, UtilIan.roundTo(1.23e1,-1),0.000001);
        assertEquals(1e18, UtilIan.roundTo(1.23e18,-18),0.000000001e18);
        }
        
    public void maxMinTests() {
        assertEquals( new Integer(50), UtilIan.max( new Integer[] {20,50,30}) );
        assertEquals( new Integer(50), UtilIan.max( Arrays.asList(20,50,30 )) );
        assertEquals( new Integer(20), UtilIan.min( new Integer[] {20,50,30}) );
        assertEquals( new Integer(20), UtilIan.min( Arrays.asList(20,50,30 )) );
        assertEquals( new Integer(20), UtilIan.min( Arrays.asList(20)) );
        // Causes an exception:
        //assertEquals( new Integer(20), UtilIan.min( new Integer[] {} ) );
        
        }
        
    @Test
    public void scannerTest() {
        Scanner s = new Scanner(" hi there     all" );
        UtilIan.skipWhitespace(s);
        assertEquals( true, s.hasNext("hi") );
        UtilIan.skipWhitespace(s);
        assertEquals( true, s.hasNext("hi") );
        assertEquals( true, UtilIan.hasNextChar(s,'h') );
        assertEquals( true, UtilIan.hasNextChar(s,'h') );
        assertEquals( true, UtilIan.hasNextChar(s) );
        assertEquals( true, UtilIan.hasNextChar(s,'h') );
        assertEquals( new Character('h'),  UtilIan.nextChar(s,'h') );
        assertEquals( true, UtilIan.hasNextChar(s,'i') );
        assertEquals( new Character('i'), UtilIan.nextChar(s,'i') );
        assertEquals( true, UtilIan.hasNextChar(s, 't') );
        assertEquals( new Character('t'),  UtilIan.nextChar(s) );
        assertEquals( true,  UtilIan.hasNextChar(s,'h') );
        s.next();
        s.next();
        assertEquals( false,  UtilIan.hasNextChar(s,'h') );
        assertEquals( false,  UtilIan.hasNextChar(s) );
        UtilIan.skipWhitespace(s);
        UtilIan.skipWhitespace(s);
        assertEquals( false,  UtilIan.hasNextChar(s,'h') );
        assertEquals( false,  UtilIan.hasNextChar(s) );
        }
     
    @Test
    public void nextMatchTest() {
        Scanner s = new Scanner(" hi the9re99     9all9" );
        assertEquals("hi t",  UtilIan.nextMatch(s,"hi t") );
        assertEquals("he",    UtilIan.nextMatch(s,"[a-z]*") );
        assertEquals("",      UtilIan.nextMatch(s,"[a-z]*") );
        assertEquals(null,    UtilIan.nextMatch(s,"[a-z]+") );
        assertEquals(null,    UtilIan.nextMatch(s,"[a-z]+") );
        assertEquals("9re99 ",UtilIan.nextMatch(s,"\\d*[a-z]*\\d* ") );
        assertEquals("9all9", UtilIan.nextMatch(s,".*") );
        assertEquals("",      UtilIan.nextMatch(s,".*") );
        assertEquals(null,    UtilIan.nextMatch(s,".+") );
        assertEquals(false,   s.hasNext() );
        }
        
    @Test
    public void nextSplittingByTest() {
        Scanner s = new Scanner(" hi the9re99     9all9" );
        assertEquals("hi",   UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("the",  UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("9",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("re",   UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("9",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("9",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("9",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("a",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("l",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("l",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals("9",    UtilIan.nextSplittingBy(s,"l9") );
        assertEquals(false, s.hasNext() );
         
        assertEquals( true,  UtilIan.hasNextSplittingBy( new Scanner("hello"),      "hello", "{}" ) );
        assertEquals( true,  UtilIan.hasNextSplittingBy( new Scanner("hello{bye}"), "hello", "{}" ) );
        assertEquals( false, UtilIan.hasNextSplittingBy( new Scanner("hello{bye}"), "hell",  "{}" ) );
        }
        
    @Test
    public void equalsIgnoreWhitespaceTest() {
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi", "hi" ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "", "" ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( " ", " " ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi there", "hi there" ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( " ", "    \t\n  \t " ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "",  "    \t\n  \t " ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi", "\nhi" ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi", "hi\n" ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi", "  hi  " ));
        assertEquals( true,  UtilIan.equalsIgnoreWhitespace( "hi there", " hi    there   " ));
        
        assertEquals( false, UtilIan.equalsIgnoreWhitespace( "", "z" ));
        assertEquals( false, UtilIan.equalsIgnoreWhitespace( "a", "z" ));
        assertEquals( false, UtilIan.equalsIgnoreWhitespace( "hi there", "hithere" ));
        assertEquals( false, UtilIan.equalsIgnoreWhitespace( "hi\nthere", "hithere" ));
        assertEquals( false, UtilIan.equalsIgnoreWhitespace( "hi\n\tthere", "hithere" ));
        }
        
    @Test
    public void equalsApproxTest() {
      assertFalse(  UtilIan.equalsApprox(3.14,22.0/7.0) );
      assertTrue(   UtilIan.equalsApprox(3.1415926535,3.1415926536) );
      assertTrue(   UtilIan.equalsApprox(3.1415926535e-300,3.1415926536e-300) );
      assertTrue(   UtilIan.equalsApprox(3.1415926535e+300,3.1415926536e+300) );
      assertFalse(  UtilIan.equalsApprox(3.0, 4.0, 0.01) );
      assertTrue(   UtilIan.equalsApprox(3.0, 3.02, 0.01) ); // within 1%
      assertTrue(   UtilIan.equalsApprox(3.0, 4.0, 0.35) );  // within 35% of each other
      }
    
    }

