import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class NumTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class NumTest
{
    
    @Test
    public void equalsTest() {
      Num e1 = new Num(4);
      Num e2 = new Num(4);
      Num e3 = new Num(5);
      assertEquals( e1, e1 );
      assertEquals( e1, e2 );
      assertFalse(  e1.equals( e3 ) );
      assertEquals( e1.hashCode(), e1.hashCode() );
      assertEquals( e1.hashCode(), e2.hashCode() );
      assertFalse(  e1.hashCode() == e3.hashCode() );
      
      assertEquals( new Num(3), new Num(3.0000000001) );
      }

    }
