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

; Auxiliary functions
(define (sum_of_squares a b) (+ (square a) (square b)))

(define (square x) (* x x))

; Should be 13
(inspect (sum_larger_squares 2 1 3))

; Exercise 1.4

(define (a_plus_abs_b a b)
    ((if (> b 0) + -) a b))

; This procedure returns a+b if b is positive and a-b otherwise.
; It does this by turning the operator on line 2 (34) into an if statement
; which evaluates to + if b is positive and - if b is negative. It then
; applies that operator to operands a and b.

; Exercise 1.6

; Using the new if statement would cause the sqrt-iter procedure to be carried out
; in an infinite loop until the program ran out of memory. This is because a normal
; if statement first evaluates the predicate, then if the predicate is true it evaluates
; the consequent and returns it. Otherwise it evaluates the alternative and returns it.
; Since new-if is a normal procedure the order is different. First the predicate, consequent
; and alternative are all evaluated, then either the consequent or the alternative is returned.
; In the new sqrt-iter function using new-if, the alternative is another call to sqrt-iter.
; Using a normal if statement, this would only be evaluated if necessary. Using new-if,
; it is automatically evaluated, so each call to sqrt-iter soon calls sqrt-iter again
; no matter what the parameters are. This results in endless recursion which eventually
; causes the program to run out of memory.

(define (sqrt-iter guess x)
    (new-if (good-enough? guess x)
        guess
        (sqrt-iter (improve guess x) x))) ; This would get evaluated regardless of guess or x