#lang racket

; http://www.radford.edu/itec380/2015fall-ibarland/Homeworks/Project/
; Forrest Meade (fmeade)
; 2.Dec.2015

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


;;;;;;;;;;; language Q3 ;;;;;;;;;;;;;;

#|
Q4:
 Expr           ::= Num | ParenExpr | BinExpr | ParityExpr | IfZeroExpr | Id | LetExpr | FuncExpr | FuncApplyExpr
  ParenExpr     ::= [[ Expr ]]
  BinExpr       ::= ( Expr BinOp Expr )
  ParityExpr    ::= parity Expr even: Expr odd: Expr ;
  IfZeroExpr    ::= if Expr is zero then Expr else Expr @
  LetExpr       ::= say Id be Expr in Expr matey
  FuncExpr      ::= (Id) -> {Expr}
  FuncApplyExpr ::= <Expr @ Expr>
  BinOp         ::= add | sub | mul | mod
|#


; An Expr is one of:
; - a number
; - (make-paren-expr  [Expr])
; - (make-bin-expr    [Expr] [Bin-op] [Expr])
; - (make-parity-expr [Expr] [Expr] [Expr])
; - (make-ifzero-expr [Expr] [Expr] [Expr]) ;>>>Q1
; - a string                                ;>>>Q2
; - (make-let-expr [char] [Expr] [Expr])    ;>>>Q2
;
; A Bin-op is a string in the list BIN_OP_TOKENS:
;
(define BIN_OP_TOKENS (list "add" "sub" "mul" "mod"))

; bin-op? ANY -> boolean
; Is `any-val` a Bin-op ?
(define (bin-op? any-val) (member any-val BIN_OP_TOKENS))


(define-struct bin-expr (left op right) #:transparent)
(define-struct paren-expr (e) #:transparent)
(define-struct parity-expr (cond even odd) #:transparent)
(define-struct ifzero-expr (cond zero other) #:transparent) ;>>>Q1
(define-struct let-expr (id be in) #:transparent)           ;>>>Q2
;
; The keyword 'transparent' makes structs with 'public' fields;
; in particular check-expect can inspect these structs.

#| Examples of the data:
   8                                 (written as "8")
   (make-paren-expr 8)               (written as "[[8]]")
   (make-bin-expr 8 "add" 4)         (written as "( 8 add 4 )")
   (make-bin-expr (make-bin-expr 8
                                 "add"
                                 4)
                  "mul"
                  5)  
                                      (written as "( ( 8 add 4 ) mul 5 )"
|#




; parse!: ( scanner -> expr)
; Return (our internal, parse-tree representation of) the first Q expr
; at the front of the scanner.
; Side effect: Consumes tokens from scnr corresponding exactly
; to the returned expr.
;
; "recursive descent parsing"
;
(define (parse! scnr)
  (cond ; The following cond-branches correspond directly to the grammar.
        [(number? (peek scnr)) (pop! scnr)]
        [(string=? (peek scnr) "parity")
         (let* {[open-parity (pop! scnr)]  ; Pop off the "parity" we just peeked at.  Ignore it.
                [subexpr1 (parse! scnr)]
                [the-even-keyword (pop! scnr)] ; pop off the "even:";  ignore it.
                [subexpr2 (parse! scnr)]
                [the-odd-keyword (pop! scnr)] ; pop off the "odd:"; ignore it.
                [subexpr3 (parse! scnr)]
                [the-semi (pop! scnr)] ; pop off the ";"; ignore it.
                }
           (make-parity-expr subexpr1 subexpr2 subexpr3))]
        [(string=? (peek scnr) "[")
         (let* {[open-square1 (pop! scnr)]
                [open-square2 (pop! scnr)]
                [the-inside-expr (parse! scnr)]
                [close-square1 (pop! scnr)]
                [close-square2 (pop! scnr)]
                }
           (make-paren-expr the-inside-expr))]
        [(string=? (peek scnr) "(")
         (let* {[open-paren (pop! scnr)]  ; Pop off the "(" we just peeked at.  Ignore it.
                [subexpr1 (parse! scnr)]  ; Read an *entire* expr, even if deeply nested!
                [operator (pop! scnr)]
                [subexpr2 (parse! scnr)]
                [closing-paren (pop! scnr)]
                }
           (when (not (string=? closing-paren ")"))
             (error 'parse! "Expected ')', got: ~v." closing-paren))
           (when (not (bin-op? operator))
             (error 'parse! "Expected one of ~v, got ~v." BIN_OP_TOKENS operator))
           (make-bin-expr subexpr1 operator subexpr2))]
        [(string=? (peek scnr) "if")   ;>>>Q1
         (let* {[open-if (pop! scnr)]
                [subexpr1 (parse! scnr)]
                [the-is (pop! scnr)]
                [the-zero (pop! scnr)]
                [the-then (pop! scnr)]
                [subexpr2 (parse! scnr)]
                [the-else (pop! scnr)]
                [subexpr3 (parse! scnr)]
                [the-at (pop! scnr)]
                } (make-ifzero-expr subexpr1 subexpr2 subexpr3))]
        [(string=? (peek scnr) "say")  ;>>>Q2
         (let* {[open-say (pop! scnr)]
                [subid (parse! scnr)]
                [the-be (pop! scnr)]
                [subexpr1 (parse! scnr)]
                [the-in (pop! scnr)]
                [subexpr2 (parse! scnr)]
                [the-matey (pop! scnr)]
                } (make-let-expr subid subexpr1 subexpr2))]
        [(char? (string-ref (peek scnr) 0)) (pop! scnr)] ;>>>Q2
        [else (error 'parse! "Unrecognized expr: ~v." (peek scnr))]))



; string->expr (-> string expr)
; Convert the given string representing exactly *one* single Expr
; into our internal format (structs).
;
(define (string->expr str)
  (parse! (create-scanner str)))



; expr->string : (-> expr string) (a.k.a. toString)
; Return a human-readable version of our internal representaiton of Exprs.
; 
(define (expr->string e)
  (cond [(number? e) (format "~v" (if (inexact? e) (exact->inexact e) e))]
        [(paren-expr? e) (string-append
                          "[["
                          (expr->string (paren-expr-e e))
                          "]]"
                          )]
        [(bin-expr? e) (string-append
                        "(" 
                        (expr->string (bin-expr-left e))
                        " "
                        (bin-expr-op e)
                        " "
                        (expr->string (bin-expr-right e))
                        ")"
                        )]
        [(parity-expr? e) (string-append 
                           "parity "
                           (expr->string (parity-expr-cond e))
                           " even: "
                           (expr->string (parity-expr-even e))
                           " odd: "
                           (expr->string (parity-expr-odd e))
                           ";"
                          )]
        [(ifzero-expr? e) (string-append  ;>>>Q1
                          "if "
                          (expr->string (ifzero-expr-cond e))
                          " is"
                          " zero"
                          " then "
                          (expr->string (ifzero-expr-zero e))
                          " else "
                          (expr->string (ifzero-expr-other e))
                          " @")]
        [(let-expr? e) (string-append   ;>>>Q2
                       "say "
                       (expr->string (let-expr-id e))
                       " be "
                       (expr->string (let-expr-be e))
                       " in "
                       (expr->string (let-expr-in e))
                       " matey")]
        [(char? (string-ref e 0)) e] ;>>>Q2
        [else (error 'expr->string "unknown internal format?!: ~v" e)]
        ))



; eval-bin-expr : (-> bin-expr val)
;
(define (eval-bin-expr e)
  (let* {[left-value  (eval (bin-expr-left  e))]
         [right-value (eval (bin-expr-right e))]
         }
    (cond [(string=? (bin-expr-op e) "add") (+ left-value right-value)]
          [(string=? (bin-expr-op e) "mul") (* left-value right-value)]
          [(string=? (bin-expr-op e) "sub") (- left-value right-value)]
          [(string=? (bin-expr-op e) "mod") (* right-value
                                               (- (/ left-value right-value)
                                                  (floor (/ left-value right-value))))]
          [else (error 'eval-bin-expr
                       "Syntax error: unknown binary operator '~a' in ~a."
                       (bin-expr-op e)
                       (expr->string e))])))



; eval : (-> expr val)
; Evaluate the given expr.
;
(define (eval e)
  (cond [(number? e) e]
        [(paren-expr? e) (eval (paren-expr-e e))]
        [(bin-expr? e) (eval-bin-expr e)] ; defer to a helper
        [(parity-expr? e)
         (if (even? (eval (parity-expr-cond e)))
             (eval (parity-expr-even e))
             (eval (parity-expr-odd e)))]
        [(ifzero-expr? e) (if (equal? 0 (eval (ifzero-expr-cond e)))  ;>>>Q1
                              (eval (ifzero-expr-zero e))
                              (eval (ifzero-expr-other e)))]
        [(let-expr? e) (eval (substitute (let-expr-id e)      ;>>>Q2
                                         (eval (let-expr-be e))
                                         (let-expr-in e)))]
        [(string? e) (error 'eval "unknown id?!: ~v" e)]        ;>>>Q2
        [else (error 'eval "unknown internal format?!: ~v" e)]))

;say x be 5 in [[say x be (x add 1) in (x add 2) matey]] matey
;substitute "z" 7 (string->expr "say x be z in (x mul z) matey")



; substitute : string, num, expr -> expr
;
(define (substitute id num e)    ;>>>Q2
  (cond [(number? e) e]
        [(paren-expr? e) (make-paren-expr (substitute id num (paren-expr-e e)))]
        [(bin-expr? e) (make-bin-expr (substitute id num (bin-expr-left e))
                                      (bin-expr-op e)
                                      (substitute id num (bin-expr-right e)))]
        [(parity-expr? e) (make-parity-expr (substitute id num (parity-expr-cond e))
                                            (substitute id num (parity-expr-even e))
                                            (substitute id num (parity-expr-odd e)))]
        [(ifzero-expr? e) (make-ifzero-expr (substitute id num (ifzero-expr-cond e))
                                            (substitute id num (ifzero-expr-zero e))
                                            (substitute id num (ifzero-expr-other e)))]
        [(let-expr? e) (make-let-expr (let-expr-id e)
                                      (substitute id num (let-expr-be e))
                                      (if (string=? id (let-expr-id e)) ;>>>Q3
                                          (substitute (let-expr-id e) (substitute id num (let-expr-be e)) (let-expr-in e))
                                          (substitute id num (let-expr-in e))))]
        [(string? e) (if (string=? e id) num e)]
        [ else (error 'substitute "unknown internal format?!: ~v" e)]))



