# Welcome to the Jumpy Programming Language

Jumpy is a programming language developed by Matthew Spillman as part of the Honors Programming Languages course at The Westminster Schools, Atlanta GA in the Spring of 2020.

This language was developed according to the processes set forth by [Dr. John C. Lusth](https://eng.ua.edu/people/dr-john-lusth/), Associate professor in the Department of Computer Science, University of Alabama, Tuscaloosa, AL.

# Getting Started with Jumpy

## Table of Contents

1. [Hello, World!](#hello-world)
2. [Defining Variables](#defining-variables)
3. [Comments](#comments)
4. [Using Jump Statements](#using-jump-statements)
5. [Creating Functions](#creating-functions)
6. [Calling Functions](#calling-functions)
7. [Index of built-ins and keywords](#index-of-built-ins-and-keywords)

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

The variable types currently supported are `int`, `str`, `bool` and `float`. Currently, all variables are global in scope so can be accessed from anywhere in the program. Also note that Jumpy does not follow the order of operations and evaluates all expressions from right to left.

## Comments

Comments are opened and closed using the | (pipe) character.
```
int myNum = 3 | this is an integer with the value 3 |
| Prints the number |
println myNum
```

## Using Jump Statements

Jumpy does not have the looping or functional constructs of most languages. It instead has jump statements and markers. Markers "mark" a part of your code. They are created using a unique name followed by a colon.

```
addXAndY:
    int result = x + y
```

Tabs can be used to organize code into blocks, although scope is not changed. Jump statements allow the program to move execution to a marker. To jump to a marker, use the `jmp` keyword followed by the marker's name as a string:
```
jmp "addXAndY"
```

This also works with variables and expressions:
```
str myString = "addXAndY"
jmp myString
```

## Creating functions

Jumpy does not support functions in the typical way. Instead, `jmp` statements and markers must be used. To create a function to square the number x, you would use this code:

```
squareX:
    fncResult @ 0 = fncArgs @ 0 * fncArgs @ 0
    jmp fncReturn
```
For this to work, there must already be global arrays called `fncArgs` and `fncResult`, as well as a string called `fncReturn` which indicates where to jump once the function finishes.

## Calling functions

To call the function `squareX`, you would use this code:
```
float{} fncArgs = float{0}
float{} fncResult = float{0}
str fncReturn = ""
...
fncArgs = float{1}
fncResult = float{1}
fncArgs @ 0 = 3
fncReturn = "afterSquare"
jmp "squareX"
afterSquare:
println fncResult @ 0 | should print 9 |
...
```
## Index of Built-ins and Keywords

| Keyword | Functionality |
| :---: | --- |
| `int`, `str`, `bool`, `float` | Variable types |
| `println x` | Prints `x` to the console on its own line |
| `jmp` | Jump statement |
| `and`, `or`, `not` | Boolean operators |
| `float{}`, `str{}` ... | Array type |
| `float{size}`, `str{size}` ... | Array constructor |
| `arr @ n` | The element at position `n` in `arr` |
| `\|` | Begins and ends a comment |

*Arrays have not been added yet
