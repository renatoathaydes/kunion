# KUnion

> Union types for Kotlin

KUnion brings *emulated* union types to Kotlin.

Union types allow a variable to take a value of two or more types.

For example, suppose you have a data source holding resources of three different types,
`User`, `Device` and `Organization`... the return type of a `getResourceById` function might be defined 
using `Union.U3` (a union between 3 types):

```kotlin
fun getResourceById(id: String): Union.U3<User, Device, Organization>
{
    // TODO call a data source
    return Union.U3.ofA(User())
}
```

To read the result of this function, you use the `use` method, which lets you specify a lambda for each type of 
resource that might be returned:

```kotlin
getResourceById("name = 'joe'").use(
        { user -> println("Got a user") },
        { device -> println("It is a device") },
        { org -> println("Org returned") })
```

Even though Kotlin infers the correct types for the argument of each lambda, you can also specify that explicitly
(just to prove that the types are correct):

```kotlin
getResourceById("name = 'joe'").use(
        { user: User -> println("Got a user") },
        { device: Device -> println("It is a device") },
        { org: Organization -> println("Org returned") })
```

Normally, you define a `typealias` to avoid having to deal with `Union` types directly.

For example, you can define the `Either` type like this:

```kotlin
typealias Either<A, B> = Union.U2<A, B>
```

Or the `Result` type (also called `Try`):

```kotlin
typealias Result<B> = Union.U2<Throwable, B>
```

## Motivation

Union types are a very convenient way to represent the return value of functions which may produce values of different
types. The classic example is the [Result](https://en.wikipedia.org/wiki/Result_type) type, which encodes a result
as either an error or a success (but never both).

For example, a function that reads a file might most certainly cause an error. As Kotlin does not have checked 
Exceptions, one way you could force the caller to check for an error condition would be to use the `Result` type:

```kotlin
fun readFileLines(path: String): Result<List<String>>
{
    val file = File(path)

    // turn a try/catch-based function into a union-type-based one
    return try
    {
        Result.ofB(file.readLines())
    }
    catch (e: Exception)
    {
        Result.ofA(e)
    }
}

// to use the result, we must check it first
val result = readFileLines(file)

result.use(
        { error -> println("An error has occurred: $error.") },
        { lines -> println("File $file has ${lines.size} lines.") })
```

It is also possible to access the value directly and check it:

```kotlin
println("Result is success? " + (result.asInstance().value !is Throwable))
```

You could have defined the `Result` type quite easily in Kotlin:

```kotlin
sealed class Result<out B>
{
    data class Success<out B>(val b: B) : Result<B>()
    data class Failure(val error: Throwable) : Result<Nothing>()
}
```

The problem that `KUnion` solves is, basically, to make it easier to define this kind of types in Kotlin:

```kotlin
typealias Result<B> = Union.U2<Throwable, B>
```

`KUnion` also adds a few useful operations to the `Union` types that makes it nicer to deal with their values,
as you'll see in the next sections.

## Usage

`KUnion` offers unions of 2, 3 and 4 types, called respectively, `Union.U2`, Union.U3` and Union.U4`.

Each generic type of a `Union` is referred to as `A`, `B`, `C` and `D`, in this order. So, to create a `Union.U2` whose
second type is `String`, you can do:

```kotlin
val value = Union.U2.ofB("my string")
```

> `Union` types provide factory functions called `ofA`, `ofB`, `ofC` (`Union.U3` and `Union.U4` only) 
  and `ofD` (`Union.U4` only), ie. they use the pattern `ofX` where `X` is the name of the generic type of the value.

This value would have a type of `Union.U2<Nothing, String>`, making it a sub-type of any `Union.U2` whose second type
is also a `String`, for example `Union.U2<Int, String>` and `Union.U2<Boolean, String>`.
 
Unfortunately, the order of the types matter, therefore a `Union.U2<A, B>` is not the same as `Union.U2<B, A>`, for
example. If you run into this problem, use the `rotate()` operation, which turns the types around (see below).

Because Kotlin supports method overloading, union types are mostly useful as the return type of functions. However,
having both method overloading and return types with overloading allows for the expression of powerful ideas!

### Guidelines for usage

`Union` types can be very useful, but Kotlin already offers a few alternatives, so knowing when to use them is
important.

Below, we discuss when to use use and when not to use union types:

#### When to use union types

* If you have a function that may return a value of different types depending on the inputs, using a `Union` type
  for its return type is a good solution.
  
* When processing a stream of data which might generate intermediate values of different types, union types can
  definitely simplify the code.

* In general, whenever you might use a `sealed class`, consider using a `Union` type instead and defining a `typealias` 
  for that (see the *Motivation* section).

#### When NOT to use union types

* Prefer to use `enum class`es where possible (ie. if all values you may have can be determined at compile time).

* Avoid using union types for function arguments, use overloading instead.

For example, if you need a function that may accept a `String` or an `Int`, write one version for each type:

```kotlin
fun overloadExample(s: String): String = s

fun overloadExample(i: Int): Int = i
```

## Union operations

### `fun rotate(): Union`

Rotates the types in the `Union` to the right.

* `U2<A, B>` returns `U2<B, A>`.
* `U3<A, B, C>` returns `U3<C, A, B>`.
* `U4<A, B, C, D>` returns `U4<D, A, B, C>`.

Examples:

```kotlin
val a: Union.U2<String, Int> = Union.U2.ofA("a")
val b: Union.U2<Int, String> = a.rotate()
```

```kotlin
val x: Union.U3<Int, Float, Double> = Union.U3.ofC(0.3415)
val y: Union.U3<Double, Int, Float> = x.rotate()
val z: Union.U3<Float, Double, Int> = y.rotate()
```

### `fun asU2(): U2<A, B | U2<B, C> | U3<B, C, D>>`

Converts any `Union` to a `Union.U2`.

* `U2<A, B>` returns itself.
* `U3<A, B, C>` returns `U2<A, U2<B, C>>`.
* `U4<A, B, C, D>` return `U2<A, U3<B, C, D>>`.

Examples:

```kotlin
val m: Union.U3<String, Int, Double> = Union.U3.ofC(0.3415)
val n: Union.U2<String, Union.U2<Int, Double>> = m.asU2() 
```

### `fun asInstance(): Instance<*>`

Returns the value of the union type's instance.

Examples:

```kotlin
val i1: Union.U2<String, Int> = Union.U2.ofA("hello")
val i2: Union.Instance<*> = i1.asInstance()

// notice that Kotlin can only represent the type of a Union's value as Any?
val i3: Any? = i2.value
```

```kotlin
// if you know the exact type of the Union, then the Instance will have the expected type
val i4: Union.U4.U4_1<String> = Union.U4.ofA("Hello")
val i5: String = i4.asInstance().value
```

### `fun <R> use(useA: (A) -> R, useB: (B) -> R[, useC: (C) -> R, useD: (D) -> R]): R`

Uses one of the types of the `Union` (whatever the type of the value it holds is), returning whatever the selected
function returns.

This is similar to Kotlin's `when` statements over sealed classes (in fact, you can use `when` statements with `Union`
types, though that's not as convenient as using `use()`).

Examples:

```kotlin
val u: Union.U2<Float, Double> = Union.U2.ofA(0.5f)
val v1: Double = u.use({ f -> f.toDouble() }, { d -> d })

// or equivalently, but more verbose
val v2: Double = when(u) {
    is Union.U2.U2_1<Float> -> u.value.toDouble()
    is Union.U2.U2_2<Double> -> u.value
}
```

### `fun <E, F[, G, H]> map(mapA: (A) -> E, mapB: (B) -> F[, mapC: (C) -> G, mapD: (D) -> H]): U4<E, F[, G, H]>`

Maps a `Union` to another `Union` of different type using the provided transformations.

Examples:

```kotlin
val p: Union.U2<Int, Float> = Union.U2.ofA(10)
val q: Union.U2<Double, Long> = p.map(Int::toDouble, Float::toLong)
```

## `equals()`, `hashCode()` and `toString()`

`Union` types implement `equals(other: Any?)` such that it returns `true` if:
 
* `other` is any other `Union` whose value is the same as this `Union`'s.
* `other` is equal to the value of this `Union`.

`hashCode()` always returns the hash code of the `Union`'s value (or `0` for `null`).

`toString()`, in the same spirit, returns whatever the `Union`'s value returns for `toString()` (or `<null>` for `null`).

The idea is to consider the `Union` as if it were the value itself. The `Union` should be considered merely a 
inconvenient, but necessary wrapper around a value.
