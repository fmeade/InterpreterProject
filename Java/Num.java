import java.util.*;

/** Our internal representation of a number in the Q1 language.
 * See http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
 * 
 * @author Forrest Meade (fmeade)
 * @author Ian Barland
 * @version 2015.Nov.30
 */
public class Num extends Value {
    private double x;
    Num( double _x ) { this.x = _x; }


    public String toString() { 
        // Let's make ints look better than, say, "43.0":
        // Omit trailing 0's after decimal, rounding to 9 places:
        return formatter.format(this.x);
    }

    private static java.text.DecimalFormat formatter = new java.text.DecimalFormat("#.#########");
      
    /** Return the Java double this Value represents
    *  (for use by other primitives in our language.)
    * @return the Java double this Value represents.
    */
    double doubleValue() { return this.x; }

    @Override
    public boolean equals( /* Num this, */ Object that) {
        if (this == that) {
            return true;
        }
        else if (that==null) {
            return false;
        }
        else if (this.getClass() != that.getClass()) {
            return false;
        }
        else {
            Num thatt = (Num) that;
            return UtilIan.equalsApprox(this.x,thatt.x);
        }

    }


    @Override
    public int hashCode() {
        if (cachedHash == null) {
            int hashSoFar = 0x814F8614; // fingerprint
            hashSoFar += new Double(this.x).hashCode();
            hashSoFar *= 31;
            cachedHash = hashSoFar;
        }
        return cachedHash;
    }

    private Integer cachedHash = null; 
 
}
