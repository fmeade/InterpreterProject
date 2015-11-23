 

import java.util.*;


public class UtilIan {
    
    /* Hide the constructor, since this is a utility class. */
    private UtilIan() { }
    
    /** Convert a char to a String.
     * @param c The Character to convert to a String.
     * @return A String of length 1, corresponding to c.
     */
    public static String charToString( Character c ) { return c.toString(); }

    /** Convert an Integer into to a double.  Same as casting (but w/o new syntax.)
     * @param i The Integer to convert to a double.
     * @return The double corresponding to i.
     */
    public static double intToDouble( Integer i ) { return i.doubleValue(); }

    /** Convert a Double to an int.  Same as casting (but w/o new syntax.)
     * @param d The Double to convert to an int.
     * @return The int corresponding to d.
     */
    public static int    doubleToInt( Double  d ) { return d.intValue(); }

    /** Round a Double to the nearest int.  Same as Math.round, but returns an int.
     * (Does *not* round-to-even, since java.lang.Math.round doesn't.)
     * @param x The Double to round.
     * @return The int nearest x (rounded as per Math.round).
     */
    public static int    roundToInt(  double  x ) { return (int)Math.round(x); }

    /** The smallest int &ge; to x (same as Math.ceil, but returns int.)
     * @param d The double to find the ceiling of.
     * @return The smallest int &ge; x.
     */
    public static int    ceil(        double  x) { return (int)Math.ceil(x); }

    /** The largest int &le; to x (same as Math.floor, but returns int.)
     * @param x The double to find the floor of.
     * @return The larest int &le; x.
     */
    public static int    floor(       double  x ) { return (int)Math.floor(x); }

    /** Round a number to a certain number of decimal places.
     * @param x The number to round.
     * @param places The number of decimal places to round to.  Can be negative.
     * @return The double corresponding to i.
     */
    public static double roundTo( double x, int places ) { 
        double scale = Math.pow(10,places);
        return Math.round(x*scale)/scale;
        }
   
        
    private static final double TOLERANCE = 0.0000001;
    /** Return whether two doubles are equal (approximately).
     * This function is symmetric.
     *    TODO: detail what happens if d1 or d2 is zero.
     * @param d1 A double to compare.
     * @param d2 A double to compare.
     * @return true iff d1 is close to d2 (within a factor of {UtilIan.TOLERANCE}).
     */
    public static boolean equalsApprox( double d1, double d2 ) {
        return UtilIan.equalsApprox(d1,d2,TOLERANCE);
        }
    public static boolean equalsApprox( double d1, double d2, double relativeTolerance ) {
        double tol = Math.max( relativeTolerance * Math.max(Math.abs(d1), Math.abs(d2)),
                               Double.MIN_VALUE );
        // TO FIX: 0.0 is never approx-equal to any non-zero value.  Is this fixable?
        // Take exp() of both numbers?
        return (d1-tol <= d2 && d2 <= d1+tol) && (d2-tol <= d1 && d1 <= d2+tol);
        }
        
    /** Return whether two Strings are equal, ignoring differences in whitespace.
     * @param str1 The first String to compare.
     * @param str1 The second String to compare.
     * @return true iff str1 equals str2, ignoring whitespace.
     */
    public static boolean equalsIgnoreWhitespace( String str1, String str2 ) {
      return equalsIgnoreWhitespace(str1,str2,"");
      }
    
    /** Return whether two Strings are equal, ignoring differences in whitespace.
     * @param str1 The first String to compare.
     * @param str1 The second String to compare.
     * @param splitBy Characters to split by.  If you split by ".?!",
     *   .even "hi?" and "hi ?" will be equalsIgnoreWhitespace.
     * @return true iff str1 equals str2, ignoring whitespace.
     */
    public static boolean equalsIgnoreWhitespace( String str1, String str2, String splitBy ) {
      Scanner s1 = new Scanner(str1);
      Scanner s2 = new Scanner(str2);
      boolean differenceFound = false;
      while (s1.hasNext() && s2.hasNext() && !differenceFound) {
        differenceFound = !(nextSplittingBy(s1,splitBy).equals(nextSplittingBy(s2,splitBy)));
        }
      return !differenceFound && !s1.hasNext() && !s2.hasNext();
      }
   
        
        
    /** Is a certain character next, in a scanner's input (skipping whitespace)?
     *  This method may advance the scanner over any whitespace.
     * @param s The scanner to read from.
     * @param c The char to read.
     * @return Whether c is the next (non-white) char at the front of s.
     */
    public static boolean hasNextChar( Scanner s, char c ) {
        UtilIan.skipWhitespace(s);
        String target = java.util.regex.Pattern.quote(UtilIan.charToString(c));
        return s.hasNext( target + ".*" );
        }

    /** Is there a next (non-white) character to read from a scanner?
     * Same as hasNext(); provided for completeness.
     * @param s The scanner to read from.
     * @return Whether s has any (non-white) input to read.
     */
    public static boolean hasNextChar( Scanner s ) {
        return s.hasNext();
        }


    /** Read the given character from a scanner's input (skipping whitespace).
     * @param s The scanner to read from.
     * @param c The char to read.
     * @return new Character(c), or null if c is not at the front of s's input (skipping whitespace).
     */
    public static Character nextChar( Scanner s, char c ) {
        return UtilIan.hasNextChar(s,c) ? UtilIan.nextChar(s) : null;
        }

    /** Read the next char from a scanner's input (skipping whitespace).
     * @param s The scanner to read from.
     * @return the Character at the front of s's input (skipping whitespace).
     */
    public static Character nextChar( Scanner s ) {
        if (!UtilIan.hasNextChar(s)) {
          throw new java.util.InputMismatchException( "UtilIan.nextChar called on empty Scanner" );
          }
       else {
          return (s.findWithinHorizon( ".", 1)).charAt(0);
          }
        }

    /** Read a token, but stopping (and not consuming) if we encounter one of delimiterChars.
     *  If the token starts with one of delimiterChars,.even return that one char.
     *  (Initial whitespace is skipped, and one trailing whitespace might be consumed.)
     * @param s The scanner to read from.
     * @param delimiterChars Characters to be treated as delimiters *within* a single scanner token.
     *        If there are any special characters, they must *already* be quoted.
     * @return the portion of the next token up until a delimiter,
     *  or (if the token starts with a delimiter) the initial delimiter itself (length 1).
     */
    public static String nextSplittingBy( Scanner s, String delimiterChars ) {
        UtilIan.skipWhitespace(s);
        String startsWithDelimiterPattern = "[" + " " + delimiterChars + "]" + ".*";
        // Don't call Pattern.quote on delimiterChars; that will
        // generate a pattern matching the entire *sequence* only (it gets anchored
        // with \Q and \E).
        // HOWEVER, apparently any [,] *do* need to be quoted. -- BUG.
        if (s.hasNext(startsWithDelimiterPattern)) {
          // Input starts with punctuation; return that one character.
          return UtilIan.nextChar(s).toString();
          }
       else { // read char-by-char until finding whitespace, a delimiter char, or EOF.
          String soFar = "";
          while (    (s.findWithinHorizon("\\s",1) == null) // Check for whitespace *first*!
                 && !(s.hasNext(startsWithDelimiterPattern))
                 && s.hasNext()) {
            soFar += UtilIan.nextChar(s);
            }
          return soFar;
          }
        }

    /**
     * Does the scanner's next token start with a given word,
     *     optionally followed by: any of delimiterChars (and.even other chars).
     * Does not consume any input (but may advance past initial whitespace?).
     * @param s The scanner to read from.
     * @param word The initial word to look for, in the first token.
     * @param delimiterChars Characters to be treated as delimiters *within* a scanner token.
     *        If there are any special characters, they must *already* be quoted.
     * @return whether s's next token starts with word,
     *     optionally followed by: any of delimiterChars (and.even other chars).
     *        
     * For example, 
     *   hasNextSplittingBy( new Scanner("hello"),      "hello", "{}" ) == true
     *   hasNextSplittingBy( new Scanner("hello{bye}"), "hello", "{}" ) == true
     *   hasNextSplittingBy( new Scanner("hello{bye}"), "hell",  "{}" ) == false
     */
    public static boolean hasNextSplittingBy( Scanner s, String word, String delimiterChars ) {    
        return s.hasNext( word + "([" + delimiterChars + "].*)?" );
        }
    
    public static boolean hasNextDoubleSplittingBy( Scanner s, String delimiterChars ) {    
        return UtilIan.hasNextSplittingBy( s, DOUBLE_PATTERN, delimiterChars );
        }
    public static double nextDoubleSplittingBy( Scanner s, String delimiterChars ) {    
        return Double.parseDouble( UtilIan.nextSplittingBy( s, delimiterChars ) );
        }
        
        
    /** Skip over the whitespace in a Scanner.  Not helpful unless you use methods which
     * ignore delimiters (such as Scanner.findWithinHorizon).
     * @param s The scanner to skip over whitespace.
     */
    public static void skipWhitespace( Scanner s ) {
        s.skip( "(" + s.delimiter().toString() + ")*" );
        }
    
    /** Return the next match (skipping initial whitespace) of a pattern.
     * Note: there is no corresponding 'hasNextMatch' method; this method  either returns
     * the matched String, or null.
     * 
     * @param s The scanner to read from.
     * @param pat The pattern to look for.
     * @return The String matching the given pattern; if the front of the input doesn't
     * match the pattern.even null is returned and the scanner is does not consume any input.
     * (N.B. A *large* amount of input might be buffered, depending on the pattern.)
     */
    public static String nextMatch( Scanner s, String pat ) {
        UtilIan.skipWhitespace(s);  // This sets 'end of previous match' to front of the input.
        return s.findWithinHorizon( "\\G" + pat, 0 );  // \G is 'end of previous match'.
        }

    /** Return the next match (skipping initial whitespace) of a pattern.
     * Note: there is no corresponding 'hasNextMatch' method; this method either returns
     * (and consumes) the matched String, or null.
     * 
     * @param s The scanner to read from.
     * @param pat The pattern to look for.
     * @return The String matching the given pattern; if the front of the input doesn't
     * match the pattern.even null is returned and the scanner is does not consume any input.
     * (N.B. A *large* amount of input might be buffered, depending on the pattern.)
     */
    public static String nextMatch( Scanner s, java.util.regex.Pattern pat ) {
        return UtilIan.nextMatch( s, pat.toString() );
        }



    /** Return the maximum item in a Collection<Comparable>.
     * @param ts A collection of comparable objects.
     * @return the (first) largest element in ts.
     */
    public static <T extends Comparable<T>> T  max( Collection<T> ts ) {
        Iterator<T> ti = ts.iterator();
        assert ti.hasNext() : "Cannot take max of an empty collection.";
        T maxSoFar = ti.next();
        while (ti.hasNext()) {
            T t = ti.next();
            if (maxSoFar.compareTo(t) < 0) maxSoFar = t;
            }
        return maxSoFar;
        }
        

    /** Return the minimum item in a Collection<Comparable>.
     * @param ts A collection of comparable objects.
     * @return the (first) smallest element in ts.
     */
    public static <T extends Comparable<T>> T  min( Collection<T> ts ) {
        Iterator<T> ti = ts.iterator();
        assert ti.hasNext() : "Cannot take min of an empty collection.";
        T maxSoFar = ti.next();
        while (ti.hasNext()) {
            T t = ti.next();
            if (maxSoFar.compareTo(t) > 0) maxSoFar = t;
            }
        return maxSoFar;
        }

    /** A var-args version of max(Collection).
     * @see UtilIan.max(Collection).
     */
    public static <T extends Comparable<T>> T  max( T... ts ) { return UtilIan.max(Arrays.asList(ts)); }

    /** A var-args version of min(Collection).
     * @see UtilIan.min(Collection).
     */
    public static <T extends Comparable<T>> T  min( T... ts ) { return UtilIan.min(Arrays.asList(ts)); }
      
 
    // The RegEx for doubles.
    // Taken from http://java.sun.com/javase/6/docs/api/java/lang/Double.html#valueOf(double)
    final static String Digits     = "(\\p{Digit}+)";
    // an exponent is 'e' or 'E' followed by an optionally 
    // signed decimal integer.
    final static String Exp        = "[eE][+-]?"+Digits;
    public final static String DOUBLE_PATTERN =
             "[+-]?(" + // Optional sign character
             "NaN|" +           // "NaN" string
             "Infinity|" +      // "Infinity" string

             // A decimal floating-point string representing a finite positive
             // number without a leading sign has at most five basic pieces:
             // Digits . Digits ExponentPart FloatTypeSuffix
             // 
             // Since this method allows integer-only strings as input
             // in addition to strings of floating-point literals, the
             // two sub-patterns below are simplifications of the grammar
             // productions from the Java Language Specification, 2nd 
             // edition, section 3.10.2.

             // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
             "("+Digits+"(\\.)?("+Digits+"?)("+Exp+")?)|"+
             
             // . Digits ExponentPart_opt FloatTypeSuffix_opt
             "(\\.("+Digits+")("+Exp+")?)" +
             ")";


             
  
    }
