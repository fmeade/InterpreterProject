#lang racket

(require "Q0.rkt")
(require "scanner.rkt")
(require rackunit) ; use `check-equal?`, not `check-expect`.
;
; We use `check-equal?` because it can be passed as a function (e.g. to `map`).
; The one advantage of `check-expect` we lose, is that all check-expects are
; delayed until the end of the file (hence, you can have a `check-expect`
; *before* the function it tests -- not so, with `check-equal?`.)


;;;;;;;;;;;;;;;;;;; TEST CASES: Q0 ;;;;;;;;;;;;;;;;

; Some expressions to test in a non-automated way:
(define e0 "43")
(define e1 "[[43]]")
(define e2 "(4 add 3)")
(define e3 "[[[[ [[ (4 add 3)]]]]]]")
(define e4 "([[43]] add    ( 42 mul 3))")

(check-equal? (string->expr "39") 39)
(check-equal? (eval (string->expr e4)) 169)

;;; three types of test we want to make, for many different exprs:
(check-equal? (string->expr "(4 add 3)") (make-bin-expr 4 "add" 3))
(check-equal? (eval (string->expr "(4 add 3)")) 7)
(check-equal? (expr->string (string->expr "(4 add 3)"))
              "(4 add 3)")


;; If you wanted to test/verify that exceptions really do get thrown
;; for particular inputs, you can use `check-exn`.
;; (This requires advanced-student, since the exception-throwing-code
;;  needs to be wrapped in a "thunk" -- a lambda of 0 arguments.)
;;
(check-exn #rx"parse!: Unrecognized expr"
           (λ() (string->expr "[[]]")))
;;
;; You don't need to do this -- I'm just including it to show
;; what things a unit-testing library can/should check for.
;;
;; Btw, I should call `(regexp (regexp-quote ...the-err-message...))`
;;   or at least quote the "." in my #rx above.
;; Althougy regexp-quote isn't in the student languages,
;; you can `(require "student-extras.rkt")` to get it.


(define tests
  ; Each entry in the list is either
  ; [str val] (where val is the result of interpreting the string str), or
  ; [str val expr] (as above, but expr is the internal (struct) representation).
  `{["7" 7 7]
    ["(3 add 4)" 7 ,(make-bin-expr 3 "add" 4)]
    ["(3 mul   4)" 12 ,(make-bin-expr 3 "mul" 4)]
    ["((3 add 4) add(  3  mul 4 ) )" 19]
    ["parity 0 even: 1 odd: 2;" 1 ,(make-parity-expr 0 1 2)]
    ["parity 1 even: 1 odd: 2;" 2 ,(make-parity-expr 1 1 2)]
    ["parity (3 add -3) even: 1 odd: 2;" 1 ,(make-parity-expr (make-bin-expr 3 "add" -3) 1 2)]
    ["parity (parity parity 0 even: 1 odd: 2; even: 3 odd: 4 ; add -3) even:  1 odd: 2;" 
     2
     ,(make-parity-expr (make-bin-expr (make-parity-expr (make-parity-expr 0 1 2) 3 4) "add" -3) 1 2)]
    
    #| Further tests, for Q1:
    ["(3.0 mod 4.0)" 3]
    ["(( 5.0 add 6.0 ) mod 3.0)" 2]
    ["(8.1 mod 3.0)" 2.1]
    ["(8.0 mod 3.1)" 1.8]
    ["(-8.1 mod 3.0)" 0.9]
    ["(-8.0 mod 3.1)" 1.3]
    ["(8.1 mod -3)" -0.9]
    ["(8.0 mod -3.1)" -1.3]
    ["(-8.1 mod -3.0)" -2.1]
    ["(-8.0 mod -3.1)" -1.8]
    ["(8.0  mod  2.0)" 0]
    ["(-8.0  mod 2.0)" 0]
    ["(8.0 mod  -2.0)" 0]
    ["(-8.0 mod -2.0)" 0]
    ["(8.0  mod  3.0)" 2]
    ["(-8.0  mod 3.0)" 1]
    ["(8.0 mod  -3.0)" -1]
    ["(-8.0 mod -3.0)" -2]
    |#
    })

; For info on backquote, see documentations and/or:
;   http://www.radford.edu/itec380/2014fall-ibarland/Lectures/backquote.html


; Given a string, return a list of tokens (of Q).
;
(define (string->Q-tokens str)
  ; Don't use scheme's built-in `read`, because our string might contain semicolons etc.
  (let loop {[scnr (create-scanner str)]} ; See "named let" -- advanced-student.
    (if (eof-object? (peek scnr))
        empty
        (cons (pop! scnr) (loop scnr)))))
        ; N.B. We RELY on left-to-right eval here:
        ; the pop happens before looping back.



; Test the internal representations:
(for-each (λ (t) (check-equal? (string->expr (first t))
                               (third t)))
          (filter (λ(t) (>= (length t) 3)) tests))



; Test that expr->string and string->expr are inverses:
(for-each (λ (t) (check-equal? (string->Q-tokens (expr->string (string->expr (first t))))
                               (string->Q-tokens (first t))))
          tests)

; Now, test `eval`:
(for-each (λ (t) (check-equal? (eval (string->expr (first t)))
                               (second t)))
          tests)



(require "Q.rkt")

;; Forrest Meade (fmeade)
;; HW07 Test Cases
;; ITEC 380
;; November 20, 2015


#|
Q1:
  Expr       ::= Num | ParenExpr | BinExpr | ParityExpr | IfZeroExpr
  ParenExpr  ::= [[ Expr ]]
  BinExpr    ::= ( Expr BinOp Expr )
  ParityExpr ::= parity Expr even: Expr odd: Expr ;
  IfZeroExpr ::= if Expr is zero then Expr else Expr @
  BinOp      ::= add | sub | mul | mod
|#

;;;;;;;;;;;;;;;;;;; TEST CASES: Q1 ;;;;;;;;;;;;;;;;


(require rackunit)


(define prog01 "( 8.1 mod [[3]])")
(check-equal? (string->expr prog01) (make-bin-expr 8.1 "mod" 3))
(check-equal? (expr->string (string->expr prog01)) "( 8.1 mod [[3]] )" )
(check-equal? (eval (string->expr prog01))  2.1 )

(define prog02 "if ( 5 sub 5 ) is zero then 0 else -1 @")
(check-equal? (string->expr prog02) (make-ifzero-expr (make-bin-expr 5 "sub" 5) 0 -1))
(check-equal? (expr->string (string->expr prog02)) "if ( 5 sub 5 ) is zero then 0 else -1 @" )
(check-equal? (eval (string->expr prog02))  0 )

#|
Java Test Cases for Q1
 @Test
  void testAFew() {
    String prog01 = "(8.1 mod 3)";
    assertEquals( Expr.parse(prog01), new BinExpr( 8.1, "mod", 3 ) );
    assertEquals( Expr.parse(prog01).toString(), "( 8.1 mod 3 )" );
    assertEquals( Expr.parse(prog01).eval(), 2.1 );

    
   String prog02 = "if ( 5 sub 5 ) is zero then 0 else -1 @";
    assertEquals( Expr.parse(prog02), new ifZeroExpr( new BinExpr(5, "sub", 5), 0, -1 ) );
    assertEquals( Expr.parse(prog02).toString(), "if ( 5 sub 5 ) is zero then 0 else -1 @" );
    assertEquals( Expr.parse(prog02).eval(), 0 );
  }
|#





#|
Q2:
 Expr       ::= Num | ParenExpr | BinExpr | ParityExpr | IfZeroExpr | Id | LetExpr
  ParenExpr  ::= [[ Expr ]]
  BinExpr    ::= ( Expr BinOp Expr )
  ParityExpr ::= parity Expr even: Expr odd: Expr ;
  IfZeroExpr ::= if Expr is zero then Expr else Expr @
  LetExpr ::= say Id be Expr in Expr matey
  BinOp      ::= add | sub | mul | mod
|#

;;;;;;;;;;;;;;;;;;; TEST CASES: Q2 ;;;;;;;;;;;;;;;;



(define prog11 "say x be 5 in (4 mul x) matey") 
(check-equal? (string->expr prog11) (make-let-expr 5 (make-bin-expr 4 "mul" 5)))
(check-equal? (expr->string (string->expr prog11)) "say x be 5 in (4 mul x) matey" )
(check-equal? (eval (string->expr prog11))  20 )

; Make an additional example, where the `say` is *not* the top-level expression:
; Then, have the three tests for it, as above.
;
(define prog12 "( 5 add say x be 5 in (4 mul x) matey )")
(check-equal? (string->expr prog12) (make-bin-expr 5 "add" (make-let-expr 5 (make-bin-expr 4 "mul" 5))))
(check-equal? (expr->string (string->expr prog12)) "( 5 add say x be 5 in (4 mul x) matey )" )
(check-equal? (eval (string->expr prog12))  25 )




; The last paragraph of #2 on hw07 mentions that you'll have to do substitution in a tree.
; Although `substitute` returns a *tree* (an Expr), 
; we can use `parse` (a.k.a. string->expr) (already tested!) to help us generate our expected-results.
;
(check-equal? (substitute "x" 9 (string->expr "3"))   (string->expr "3") )
(check-equal? (substitute "x" 9 (string->expr "x"))   (string->expr "9") )
(check-equal? (substitute "z" 7 (string->expr "x"))   (string->expr "x") )
(check-equal? (substitute "z" 7 (string->expr "(4 add z)"))   (string->expr (make-bin-expr 4 "add" 7)) )
(check-equal? (substitute "z" 7 (string->expr "say x be z in (x mul z) matey"))   (string->expr (make-let-expr 7 (make-bin-expr 7 "mul" 7))) )
; Give at least one more interesting tree, to test `substitute` on,
; with parse-tree of height of 2 or more.
; You do *not* need to do `substitute` on a parse tree containing a `say` inside of it ... yet.
; (But you are encouraged to start thinking about what you want to happen, in that situation.)
;
(check-equal? (substitute "y" 5 (string->expr "( ( y add 5 ) sub ( [[10]] sub y ) )"))
              (string->expr (make-bin-expr (make-bin-expr 5 "add" 5) "sub" (make-bin-expr (make-paren-expr 10) "sub" 5))))










