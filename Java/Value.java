/**
 * Abstract class Value -
 * The type of all Expressions which self-evaluate:
 * Numbers, and (later, in Q3) functions.
 * 
 * See http://www.radford.edu/itec380/2009fall-ibarland/Hw06/hw06.html
 *
 * (This architecture works as long as every value is a type of
 * Expression, which is a nice feature to have in a language;
 * it means that any result can be pasted/substituted into
 * larger expressions, which promotes a substitution model
 * of evaluation.)
 * 
 * @author Ian Barland
 * @version 2014.Nov.04
 */
public abstract class Value extends Expr { 
  public Value eval() { return this; } // We evaluate to ourselves, since we're a Value.

  
  /** @override.  @see Expr#hashCode */
  public int hashCode() {
    return super.hashCode()+(int)3141949305892159L;
    }
  }
