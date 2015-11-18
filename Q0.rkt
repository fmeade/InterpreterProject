#lang racket
; http://www.radford.edu/itec380/2015fall-ibarland/Homeworks/Project/
; Ian Barland, 2008.Nov.27, updates through 2015.Nov.09

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


;;;;;;;;;;; language Q0 ;;;;;;;;;;;;;;

; An Expr is one of:
; - a number
; - (make-paren-expr  [Expr])
; - (make-bin-expr    [Expr] [Bin-op] [Expr])
; - (make-parity-expr [Expr] [Expr] [Expr])
;
; A Bin-op is a string in the list BIN_OP_TOKENS:
;
(define BIN_OP_TOKENS (list "add" "sub" "mul"))

; bin-op? ANY -> boolean
; Is `any-val` a Bin-op ?
(define (bin-op? any-val) (member any-val BIN_OP_TOKENS))


(define-struct bin-expr (left op right) #:transparent)
(define-struct paren-expr (e) #:transparent)
(define-struct parity-expr (cond even odd) #:transparent)
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
; Return (our internal, parse-tree representation of) the first Q0 expr
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
  (cond [(number? e) (format "~v" e)]
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
        [else (error 'expr->string "unknown internal format?!: ~v" e)]
        ))



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
        [else (error 'eval "unknown internal format?!: ~v" e)]))

; eval-bin-expr : (-> bin-expr val)
;
(define (eval-bin-expr e)
  (let* {[left-value  (eval (bin-expr-left  e))]
         [right-value (eval (bin-expr-right e))]
         }
    (cond [(string=? (bin-expr-op e) "add") (+ left-value right-value)]
          [(string=? (bin-expr-op e) "mul") (* left-value right-value)]
          [(string=? (bin-expr-op e) "sub") (- left-value right-value)]
          [else (error 'eval-bin-expr
                       "Syntax error: unknown binary operator '~a' in ~a."
                       (bin-expr-op e)
                       (expr->string e))])))
