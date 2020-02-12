(define (main)
    (println "AUTHOR: Matthew Spillman matthewspillman@westminster.net")
)

; Exercise 1.2
; Expected answer: -0.246666667

(inspect
(/
    (+ 5 4 (- 2 (- 3 (+ 6 (/ 4.0 5.0)))))
    (* 3 (- 6 2) (- 2 7))
))

; Exercise 1.3

; Define larger squares function by figuring out smallest value and squaring the other two
(define (sum_larger_squares a b c)
    (cond 
        ((and (< c a) (< c b)) (sum_of_squares a b))
        ((and (< b a) (< b c)) (sum_of_squares a c))
        (else (sum_of_squares b c))
    )
)

; Auxiliary sum of squares function
(define (sum_of_squares a b) (+ (* a a) (* b b)))

; Should be 13
(inspect (sum_larger_squares 2 1 3))

; Exercise 1.4

(define (a_plus_abs_b a b)
    ((if (> b 0) + -) a b))

; This procedure returns a+b if b is positive and a-b otherwise.
; It does this by turning the operator on line 2 (34) into an if statement
; which evaluates to + if b is positive and - if b is negative. It then
; applies that operator to operands a and b.

