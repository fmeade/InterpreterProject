/** Our internal representation of a BinExpr
 * in the Q1 language.
 * http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
 * 
 * @author Forrest Meade (fmeade)
 * @author Ian Barland
 * @version 2015.Nov.30
 */
public class BinExpr extends Expr {

  Expr left, right;
  String op;

  BinExpr( Expr _left, String _op, Expr _right ) {
    this.left  = _left;
    this.op    = _op;
    this.right = _right;
  }
  
  public String toString( /* BinExpr this */) {
    return "("
         + this.left.toString()
         + " "
         + op
         + " "
         + this.right.toString()
         + ")";
  }
    
  public Value eval( /* BinExpr this */) {

    double leftValue  = ((Num)(this.left .eval())).doubleValue();
    double rightValue = ((Num)(this.right.eval())).doubleValue();

    if (this.op.equals("mul")) {
      return new Num(leftValue * rightValue);
    }
    else if (this.op.equals("add")) {
      return new Num(leftValue + rightValue);
    }
    else if (this.op.equals("sub")) {
      return new Num(leftValue - rightValue);
    }
    else if (this.op.equals("mod")) {
      return new Num(rightValue * ((leftValue/rightValue) - (Math.floor(leftValue/rightValue)))); // y*(x/y-floor(x/y))
    }
    else {
      throw new RuntimeException("BinExpr.eval(): unknown binary operator `" + this.op + "`");
    }
  }
  
  @Override
  public boolean equals( /* BinExpr this, */ Object that) {
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
      BinExpr thatt = (BinExpr) that;

      return this.left.equals(thatt.left)
          && this.op.equals(thatt.op)
          && this.right.equals(thatt.right);
    }
    
  }
    
  @Override
  public int hashCode() {
    if (cachedHash == null) {
      int hashSoFar = 0x814F8614; // fingerprint
      hashSoFar += this.left.hashCode();
      hashSoFar *= 31;
      hashSoFar += this.op.hashCode();
      hashSoFar *= 31;
      hashSoFar += this.right.hashCode();
      cachedHash = hashSoFar;
    }
    return cachedHash;
  }

  private Integer cachedHash = null;

}
