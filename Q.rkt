#lang racket

; http://www.radford.edu/itec380/2015fall-ibarland/Homeworks/Project/
; Forrest Meade (fmeade), November 30, 2015

; We'll run this program in language-level 'Racket', *not* 'Advanced Student'.
; (This is because we are exporting our functions
;  as a module, so the test-cases can use them.)

; This next line exports all functions
; (in Java terms: a package where everything is public).
; Alternately, you could `provide` just the specific names you wanted public.
;
(provide (all-defined-out))

(require "scanner.rkt")  ; scanner.rkt must be in the same dir as this file.
(require test-engine/scheme-tests)

#|
Q1:
 Expr       ::= Num | ParenExpr | BinExpr | ParityExpr | IfZeroExpr
  ParenExpr  ::= [[ Expr ]]
  BinExpr    ::= ( Expr BinOp Expr )
  ParityExpr ::= parity Expr even: Expr odd: Expr ;
  IfZeroExpr ::= if Expr is zero then Expr else Expr @
  BinOp      ::= add | sub | mul | mod
|#

;;;;;;;;;;; language Q1 ;;;;;;;;;;;;;;

; parse : 
; which turns source-code into an internal representation
;

; expr->string :
; aka toString
; which turns internal representation back into source-code
;

; eval : 
; which actually interprets the program, returning a value (a number for Q0-Q2, and a number-or-function in Q3)
; 






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

;;;;;;;;;;; language Q2 ;;;;;;;;;;;;;;

; parse : 
; which turns source-code into an internal representation
;

; expr->string :
; aka toString
; which turns internal representation back into source-code
;

; eval : 
; which actually interprets the program, returning a value (a number for Q0-Q2, and a number-or-function in Q3)
; 



