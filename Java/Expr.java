/**
 * Class Expr, Our internal representation of an expression
 * in the Q language.
 * 
 * See http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
 * 
 * @author Forrest Meade (fmeade)
 * @version 2015.Nov.30
 */
public abstract class Expr {
	
	/** Evaluate a given Expr.
	 * @return the Value this Expr evaluates to.
	 *  (In Q0, all values are numbers (doubles), but
	 *   in Q3 that will change, which is why we have
	 *   pre-emptively made the return type 'Value'.)
	 */
	abstract public Value eval();

	/** Return a String representation of this Expr.
	 *  The result will be something which can be
	 *  passed into 'parse(String)' to get the same
	 *  Expr back.  That is, toString and parse are
	 *  inverses of each other.
	 *  @return a String representation of this Expr.
	 */
	abstract public String toString();


	/** Return (our internal representation of) the expression s.
	 * @param s The source code for exactly one Expr.  Must by syntactically correct.
	 * @return (our internal representation of) the expression s.
	 */
	public static Expr parse(String s) { return Expr.parse(new java.util.Scanner(s)); }


	public static final String PUNCTUATION = "(){}<>!@$^&~=;'\"?,#" 
	                                    	 + java.util.regex.Pattern.quote("[]");
	                                     	// I am not sure why the [] needs to be quoted,
                                     		// but nothing can be quoted.  It's a bug in UtilIan. --Ian


	/** Return (our internal representation of) the expression s.
	 * @param s A scanner reading the source code for exactly one Expr.
	 *          Must by syntactically correct.
	 * @return (our internal representation of) the expression s.
	 */
	public static Expr parse(java.util.Scanner s) {      
		if (UtilIan.hasNextDoubleSplittingBy(s,PUNCTUATION)) {
			return new Num( UtilIan.nextDoubleSplittingBy(s,PUNCTUATION) );
		}
		else if (UtilIan.hasNextSplittingBy(s, ParityExpr.TOKEN, PUNCTUATION)) {
			UtilIan.nextSplittingBy(s, PUNCTUATION);  // consume the "parity"
	    	Expr subExpr1 = parse(s);
	    	UtilIan.nextSplittingBy(s, PUNCTUATION);  // Consume the `even:`
	    	Expr subExpr2 = parse(s);
	    	UtilIan.nextSplittingBy(s, PUNCTUATION);  // Consume the `odd:`
	    	Expr subExpr3 = parse(s);
	    	assert UtilIan.hasNextChar(s,';') : "`;` must close " + ParityExpr.TOKEN;
	    	UtilIan.nextChar(s,';');  // Consume the closing ;
	    	return new ParityExpr(subExpr1,subExpr2,subExpr3);
	    }
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
	    else if (UtilIan.hasNextChar(s,'<') ) { // a ParenExpr
	    	UtilIan.nextChar(s,'<');  // Consume the opening "<" and continue.
	    	Expr subExpr1 = parse(s);
	    	UtilIan.nextChar(s,'>');  // consume the '>'
	    	return new ParenExpr(subExpr1);
	    }
	    else if (UtilIan.hasNextChar(s,'(') ) { // a BinExpr
	    	UtilIan.nextChar(s,'(');  // Consume the opening "<" and continue.
	    	UtilIan.nextChar(s,':');  // Consume the opening ":" and continue.
	    	Expr subExpr1 = parse(s);
	      	// Consume the operator; each of the three characters reads as
	      	// one punctuation character:
	      	String operator = UtilIan.nextSplittingBy(s,PUNCTUATION);
	      	Expr subExpr2 = parse(s);
	      	UtilIan.nextChar(s,':');  // consume the ':'
	      	UtilIan.nextChar(s,')');  // consume the ')'
	      	return new BinExpr(subExpr1, operator, subExpr2);
	    }
	    else { /* Unknown syntax! */
	      	String tokens = "";
	      	while (s.hasNext()) { tokens += s.next(); }
	      	throw new IllegalArgumentException( "Syntax error: Couldn't parse " + tokens );
	    }
    }




}