# Welcome to the Jumpy Programming Language

Jumpy is a programming language developed by Matthew Spillman as part of the Honors Programming Languages course at The Westminster Schools, Atlanta GA in the Spring of 2020.

This language was developed according to the processes set forth by [Dr. John C. Lusth](https://eng.ua.edu/people/dr-john-lusth/), Associate professor in the Department of Computer Science, University of Alabama, Tuscaloosa, AL.

# Getting Started with Jumpy

## Table of Contents

1. [Hello, World!](#hello-world)
2. [Defining Variables](#defining-variables)
3. [Using Jump Statements](#using-jump-statements)
3. [Creating Functions](#creating-functions)
4. [Calling Functions](#calling-functions)
5. [Index of built-ints and keywords](#index-of-built-ins-and-keywords)

## Hello, World!

The following program prints `"Hello, world!"` to the console.

```
println "Hello, world!"
```

This is fairly straightforward and similar to many other languages, although it is important to note that parentheses are not allowed. Also, each statement in Jumpy is terminated with a newline (or the end of the file).

## Defining variables

Variables can be defined by writing their type, their name, an equals sign, and then an expression. All variables must be assigned a value when they are declared.

```
int x = 2+3*5
str myString = "hey there"
```

The only variable types currently supported are `int` and `str` for integers and strings, respectively. Variables have no scope limitations, so they can be accessed anywhere in the program.

## Using Jump Statements

Jumpy does not have the looping or functional constructs of most languages. It instead has jump statements and markers. Markers "mark" a part of your code. They are created using a unique name followed by a colon.

```
addXAndY:
    int result = x + y
```

Tabs can be used freely and are ignored by the language. Jump statements allow the program to move execution a certain number of lines forward or backward, or to a marker. To jump to a marker, use the `jmp` keyword followed by the marker's name as a string:
```
jmp "addXAndY"
```

This also works with variables and expressions:
```
str myString = "addXAndY"
jmp myString
```

If you pass an integer to the `jmp` keyword, it will jump that many lines ahead. For example, the code
```
int x = 1
x = x * (x+1)
jmp -1
```

would create an infinite loop with the variable x having the value n! at loop n.

## Defining functions

Jumpy does not support functions in the typical way. Instead, `jmp` statements and markers must be used. To create a function to square the number x, you would use this code:

```
squareX:
    result = x * x
    jmp doneWithSquareX
```
For this to work, there must already be a variables called `x` and `result`, as well as a string called `doneWithSquareX` which indicates where to jump once the function finishes.

## Calling functions

To call the function `squareX`, you would use this code:
```
int result = 0
int x = 4
str doneWithSquareX = "afterSquare"
jmp "squareX"
afterSquare:
...
```
