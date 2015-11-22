##########################
InterpreterProject
##########################

Forrest Meade (fmeade)
ITEC 380

(Current update -- Q2)

Language Q: 
  Expr       ::= Num | ParenExpr | BinExpr | ParityExpr | IfZeroExpr | Id | LetExpr
  ParenExpr  ::= [[ Expr ]]
  BinExpr    ::= ( Expr BinOp Expr )
  ParityExpr ::= parity Expr even: Expr odd: Expr ;
  IfZeroExpr ::= if Expr is zero then Expr else Expr @
  LetExpr    ::= say Id be Expr in Expr matey
  BinOp      ::= add | sub | mul | mod