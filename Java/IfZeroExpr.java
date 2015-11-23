/** Our internal representation of a IfZeroExpr
* in the Q1 language.
* See http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
* 
* @author Forrest Meade (fmeade)
* @author Ian Barland
* @version 2015.Nov.30
*/
public class IfZeroExpr extends Expr {
    static final String TOKEN = "if";

    Expr cond, zeroPart, notPart;

    IfZeroExpr( Expr _cond, Expr _zeroPart, Expr _notPart) {
        this.cond = _cond;
        this.zeroPart = _zeroPart;
        this.notPart = _notPart;
    }

    public String toString() {
        return TOKEN
        + " "
        + this.cond.toString()
        + " is zero then "
        + this.zeroPart.toString()
        + " else "
        + this.notPart.toString()
        + "@";
    }


    public Value eval() {
        if (((Num)(cond.eval())).doubleValue() %2 == 0) {
            return this.zeroPart.eval();
        }
        else {
            return this.notPart.eval();
        }
    }

    @Override
    public boolean equals( /* IfZeroExpr this, */ Object that) {
        if (this == that) {
            return true;
        }
        else if (that == null) {
            return false;
        }
        else if (this.getClass() != that.getClass()) {
            return false;
        }
        else {
            IfZeroExpr thatt = (IfZeroExpr) that;
            return this.cond.equals(thatt.cond)
            && this.zeroPart.equals(thatt.zeroPart)
            && this.notPart.equals(thatt.notPart);
        }

    }

    @Override
    public int hashCode() {
        if (cachedHash == null) {
            int hashSoFar = 0x814F8614; // fingerprint
            hashSoFar += this.cond.hashCode();
            hashSoFar *= 31;
            hashSoFar += this.zeroPart.hashCode();
            hashSoFar *= 31;
            hashSoFar += this.notPart.hashCode();
            cachedHash = hashSoFar;
        }
        return cachedHash;
    }
    
    private Integer cachedHash = null; 
}
