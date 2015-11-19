PROJECT TITLE: O0 interpreter
See  http://www.radford.edu/itec380/2009fall-ibarland/Homeworks/hw06.html
VERSION or DATE: 2009.Nov.03
AUTHORS: Ian Barland
USER INSTRUCTIONS:
  Test cases are in ExprTest.java.
  The testing framework takes a list of pairs --
  strings (must be legal O0 syntax) and the expected value.
  The testing framework then makes two test for each string:
    it parses the string and toString's the parse tree, expecting back the 
    same input string (up to white space),
  and it parses and evals the string, checking for the expected value.
