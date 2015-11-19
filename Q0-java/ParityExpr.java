/** Our internal representation of a ParityExpr
 * in the Q0 language.
 * See http://www.radford.edu/itec380/2015fall-ibarland/Project
 * 
 * @author Ian Barland
 * @version 2015.Nov.09
 */
public class ParityExpr extends Expr {
  static final String TOKEN = "parity";
  
  Expr cond, evenPart, oddPart;
  
  ParityExpr( Expr _cond, Expr _evenPart, Expr _oddPart) {
    this.cond = _cond;
    this.evenPart = _evenPart;
    this.oddPart = _oddPart;
    }
  
  public String toString() {
    return TOKEN
         + " "
         + this.cond.toString()
         + " even: "
         + this.evenPart.toString()
         + " odd: "
         + this.oddPart.toString()
         + ";";
    }
    
    
  public Value eval() {
    if (((Num)(cond.eval())).doubleValue() %2 == 0) {
      return this.evenPart.eval();
      }
   else {
      return this.oddPart.eval();
      }
    }
    
  @Override
  public boolean equals( /* ParityExpr this, */ Object that) {
    if (this==that) {
      return true;
      }
    else if (that==null) {
      return false;
      }
   else if (this.getClass() != that.getClass()) {
      return false;
      }
   else {
      ParityExpr thatt = (ParityExpr) that;
      return this.cond.equals(thatt.cond)
          && this.evenPart.equals(thatt.evenPart)
          && this.oddPart.equals(thatt.oddPart);
      }
    
    }
    
  @Override
  public int hashCode() {
    if (cachedHash == null) {
      int hashSoFar = 0x814F8614; // fingerprint
      hashSoFar += this.cond.hashCode();
      hashSoFar *= 31;
      hashSoFar += this.evenPart.hashCode();
      hashSoFar *= 31;
      hashSoFar += this.oddPart.hashCode();
      cachedHash = hashSoFar;
      }
    return cachedHash;
    }
  private Integer cachedHash = null; 
  }
