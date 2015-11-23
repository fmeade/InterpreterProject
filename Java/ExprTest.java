/** Test Exprs.
* http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
*
* Meant to cover most syntax situations for Q0-Q2.
* HOWEVER, the testing approach has some systemic flaws --
* we never test against the expected internal representation.
* (For example, testParseToString just checks that parse and
* 'toString' are inverses of each other; if they are both
* the identity function, it would pass all tests.)
*
* Compiling this function will generate a warning
* ('unchecked generic array creation', due to using Arrays.asList
*  and varargs).
*  
* For compiling with junit (if your IDE doesn't automatically recognize it),
* see the note at end of this file.
*
* @author  Forrest Meade (fmeade)
* @author Ian Barland
* @version 2015.Nov.30
*/
public class ExprTest extends junit.framework.TestCase {

    static final double TOLERANCE = 0.000001;
    java.util.List<Pair<String,Object>> allTests;
    /* allTests should be a list of pairs:
    * a Q0 Expr, and what that expression evaluates to.
    * Use a Double, for Num values.
    */


    /**
    * Sets up the test fixture.
    *
    * Called before every test case method.
    */
    protected void setUp() {
        /* allTests should be a list of pairs:
        * a Q0 Expr, and what that expression evaluates to.
        * Use a Double, for Num values.
        */
        allTests = java.util.Arrays.asList(
            /* Q0 tests: */
            new Pair<String,Object>( "7", 7 )
            ,new Pair<String,Object>( "7.0", 7 )  // parseString("7.0").toString() return "7", not "7.0".
            ,new Pair<String,Object>( "(3 add 4 )", 7 )
            ,new Pair<String,Object>( "( 3 mul   4)", 12 )
            ,new Pair<String,Object>( "((3 add 4) add (  3 mul 4  ))", 19)
            ,new Pair<String,Object>( "parity 0 even: 1 odd: 2;", 1 )
            ,new Pair<String,Object>( "parity 1 even: 1 odd: 2;", 2 )
            ,new Pair<String,Object>( "parity (3 add -3) even: 1 odd: 2;", 1 )
            ,new Pair<String,Object>( "parity (parity parity -1 even: 1 odd: 2; even: 3 odd: 4 ; add -3) even: 1 odd: 2;", 1 )

            /* *** Q1 tests: *** */
            ,new Pair<String,Object>( "(3 mod 4)", 3)
            ,new Pair<String,Object>( "((5 add 6) mod 3)", 2)
            ,new Pair<String,Object>( "(8.1 mod 3)", 2.1)      
            ,new Pair<String,Object>( "(8 mod 3.1)", 1.8)    
            ,new Pair<String,Object>( "(-8.1 mod 3)", 0.9)     
            ,new Pair<String,Object>( "(-8 mod 3.1)", 1.3)    
            ,new Pair<String,Object>( "(8.1 mod -3)", -0.9)    
            ,new Pair<String,Object>( "(8 mod -3.1)", -1.3)  
            ,new Pair<String,Object>( "(-8.1 mod -3)", -2.1)
            ,new Pair<String,Object>( "(-8 mod -3.1)", -1.8)
            ,new Pair<String,Object>( "(8 mod 2)", 0)
            ,new Pair<String,Object>( "(-8 mod 2)", 0)
            ,new Pair<String,Object>( "(8 mod -2)", 0)
            ,new Pair<String,Object>( "(-8 mod -2)", 0)
            ,new Pair<String,Object>( "(8 mod 3)", 2)
            ,new Pair<String,Object>( "(-8 mod 3)", 1)
            ,new Pair<String,Object>( "(8 mod -3)", -1)
            ,new Pair<String,Object>( "(-8 mod -3)", -2)



        //YOU MUST CREATE SOME TESTS FOR whenPosExprs
        //*** */
        ); 
    }

    /** For every element in allTests,
    * parse the string, and.even call toString on the result,
    * checking that we get back exactly the input string
    * (up to whitespace).
    */
    public void testParseToString() {
        for ( Pair<String,Object> t : allTests ) {
            String expected = t.getFirst();
            String actual   = Expr.parse( t.getFirst() ).toString();
            if (! UtilIan.equalsIgnoreWhitespace(expected, actual, Expr.PUNCTUATION)) {
    // This assert will fail; just present it to the user:
                assertEquals( expected, actual );
            }
        }
    }  


    /** For every element in allTests,
    * parse the string and eval the result,
    * checking that we get back the second item in the pair.
    * If the second item is a number, we check within {@value TOLERANCE}.
    * If it's not a number (as in O2, once function-values are introduced),
    * this code doesn't actually test anything, and asserts an error saying so.
    */    
    public void testEval() {
        for ( Pair<String,Object> t : allTests ) {
            if (Number.class.isInstance( t.getSecond() )) {
                assertEquals( ((Number)t.getSecond()).doubleValue(),
                    ((Num)(Expr.parse( t.getFirst() ).eval())).doubleValue(),
                    TOLERANCE );
            }
            else {
                assertEquals( t.getFirst() + " didn't evaluate to a double",
                    "How to express the expected Fun value?" );
            }
        }
    }  


    public void testMyStuff() {
        assertEquals( Expr.parse("2").eval(), new Num(2) );
    // Add more specific tests here,
    // if you want things more specific that provided via adding to `allTests` above.
    }


    /**
    * Tears down the test fixture.
    *
    * Called after every test case method.
    */
    protected void tearDown()
    {
    }


    void testAFew() {
        String prog01 = "(8.1 mod 3)";
        assertEquals( Expr.parse(prog01), new BinExpr( 8.1, "mod", 3 ) );
        assertEquals( Expr.parse(prog01).toString(), "( 8.1 mod 3 )" );
        assertEquals( Expr.parse(prog01).eval(), 2.1 );


        String prog02 = "if ( 5 sub 5 ) is zero then 0 else -1 @";
        assertEquals( Expr.parse(prog02), new IfZeroExpr( new BinExpr(5, "sub", 5), 0, -1 ) );
        assertEquals( Expr.parse(prog02).toString(), "if ( 5 sub 5 ) is zero then 0 else -1 @" );
        assertEquals( Expr.parse(prog02).eval(), 0 );
    }
  
  

}

    /*
    In the Q0-java code, a couple of classes extend junit.framework.TestCase.

    In order to compile these, you'll need to have that class (and others) in your CLASSPATH.

    I know that BlueJ comes with the junit-classes automatically, and I imagine that Eclipse does, as well.

    If you're getting an error "package junit.framework does not exist", you'll want to download 'junit.jar' (I got it from junit.org), and extract it somewhere in your classpath (e.g., in the same dir as your project).
    To run the tests [again, BlueJ and Eclipse have buttons to do this] from the command line, you'd type:

    java  org.junit.runner.JUnitCore  TestExpr
    */
