
(define (average x y) (/ (+ x y) 2))

(define (improve guess x)
    (average guess (/ x guess))
)

(define (goodEnough guess x)
    (< (abs (- x (square guess))) 0.000001)
)

(define (square x) (* x x))

(define (goodGuess x base)
    (if (< (square base) x)
        (goodGuess x (+ base 1))
        base
    )
)

(define (sqrt x) (sqrtIter (goodGuess x 0) x))

(define (sqrtIter guess x)
    (println "Guess " guess)
    (if (goodEnough guess x)
        guess
        (sqrtIter (improve guess x) x)
    )
)

(inspect (sqrt 12912.0))