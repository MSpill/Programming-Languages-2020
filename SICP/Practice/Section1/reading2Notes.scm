(define (main)
    (println "AUTHOR: Matthew Spillman matthewspillman@westminster.net")
)

; Use define to create variables
(define size 2)

; Reference them later
(inspect (* 5 size))
(define pi 3.14159)
(define radius 10)
(inspect (* pi (* radius radius)))
(define circumference (* 2 pi radius))
(inspect circumference)

; Evaluating combinations is recursive
; 1. Evaluate all the subexpressions
; 2. Apply the procedure of the leftmost subexpression to the other subexpressions
; Define statements are not combinations

; You can define procedures with the define keyword

(define (square x) (* x x))
(inspect (square 21))
(inspect (square (+ 2 5)))
(inspect (square (square 3)))

; You can make compound procedures

(define (sum-of-squares x y) (+ (square x) (square y)))
(inspect (sum-of-squares 3 4))

(define (f a)
    (sum-of-squares (+ a 1) (* a 2)))
(inspect (f 5))