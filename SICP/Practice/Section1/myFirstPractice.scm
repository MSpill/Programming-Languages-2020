(define (main)
    (println "AUTHOR: Matthew Spillman matthewspillman@westminster.net")
)

; Phone number is 1 (404) 952 3100
(inspect (- 2 1))
(inspect (/ 808 2))
(inspect (+ 950 2))
(inspect (* 50 62))

; Compound statements should give 11, 15, -22, and 2
(inspect (- (+ (* 3 4) (/ 4 2)) 3))
(inspect (* (+ 1 2) 5))
(inspect (- 3 (* (+ 2 3) 5)))
(inspect (/ 4 (+ 1 (- 2 1))))