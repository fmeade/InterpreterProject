/** Our internal representation of a ParenExpr
* in the Q1 language.
* See http://www.radford.edu/~itec380/2015fall-ibarland/Homeworks/hw07.html
* 
* @author Forrest Meade (fmeade)
* @author Ian Barland
* @version 2015.Nov.30
*/
public class ParenExpr extends Expr {

	Expr e;

	ParenExpr( Expr _e ) {
		this.e  = _e;
	}

	public String toString(/* ParenExpr this */) {
		return "[["
			+ this.e.toString()
			+ "]]";
	}

	public Value eval() {
		return this.e.eval();
	}


	@Override
	public boolean equals( /* ParenExpr this, */ Object that) {
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
			ParenExpr thatt = (ParenExpr) that;
			return this.e.equals(thatt.e);
		}

	}

	@Override
	public int hashCode() {
		if (cachedHash == null) {
			int hashSoFar = 0x814F8614; // fingerprint
			hashSoFar += this.e.hashCode();
			cachedHash = hashSoFar;
		}
		return cachedHash;
	}

	private Integer cachedHash = null;

}
