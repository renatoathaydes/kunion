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

TODO