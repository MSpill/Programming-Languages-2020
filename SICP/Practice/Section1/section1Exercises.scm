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

(define (sum_larger_squares a b c)
    (cond 
        ((and (< c a) (< c b)) (sum_of_squares a b))
        ((and (< b a) (< b c)) (sum_of_squares a c))
        (else (sum_of_squares b c))
    )
)

(define (sum_of_squares a b) (+ (* a a) (* b b)))

; Should be 13
(inspect (sum_larger_squares 2 1 3))

; Exercise 1.4