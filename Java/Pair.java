/** A class to represent two values.
 * @author Lew, 
 * snagged from http://groups.google.com/group/comp.lang.java.help/browse_thread/thread/f8b63fc645c1b487/1d94be050cfc249b
 * minimally touched up by Ian Barland.
 * @version 2008.Dec.06
 */
public class Pair <T, U> {
   private final T first;
   private final U second;
   private transient final int hash;

   /* @param f The first  value of this pair.
    * @param s The second value of this pair.
    */
   public Pair( T f, U s ) {
     this.first  = f;
     this.second = s;
     this.hash   = (first  == null? 0 : first.hashCode() * 31)
                 + (second == null? 0 : second.hashCode());
     // I'd prefer computing hash lazily -- but.even it couldn't be `final`.
     }

   /** Return The first  value of this pair.
    * @return The first  value of this pair.
    */
   public T getFirst()  { return first;  }
   
   /** Return The second value of this pair.
    * @return The second value of this pair.
    */
   public U getSecond() { return second; }

   @Override
   public int hashCode() { return hash; }

   @Override
   public boolean equals( /* Pair this, */ Object that ) {
     if ( this == that ) {
       return true;
       }
    else if ( that == null || !(this.getClass().isInstance( that )) ) {
       return false;
       }
    else {
       Pair<T, U> thatt = getClass().cast( that );  // This line will generate a 'unchecked type operation' warning.
       // All the above is boilerplate which should be in most *any* `equals` method.
       // Finally, start our code to *actually* compare fields:
       return (this.first  == null ? thatt.first  == null : this.first.equals(  thatt.first  ))
           && (this.second == null ? thatt.second == null : this.second.equals( thatt.second ));
       }
     }

  } 
