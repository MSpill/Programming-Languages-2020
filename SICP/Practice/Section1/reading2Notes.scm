(define (main)
    (println "AUTHOR: Matthew Spillman matthewspillman@westminster.net")
)

; Use define to create variables
(define size 2)

; Reference them later
(inspect (+ size size))

; Evaluating combinations is recursive
; 1. Evaluate all the subexpressions
; 2. Apply the procedure of the leftmost subexpression to the other subexpressions
; Define statements are not combinations

; You can define procedures with the define keyword

(define (square x) (* x x))
(inspect (square 5))

; You can make compound procedures

(define (sum-of-squares x y) (+ (square x) (square y)))
(inspect (sum-of-squares 3 4))