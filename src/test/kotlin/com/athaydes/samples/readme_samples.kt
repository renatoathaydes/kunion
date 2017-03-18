package com.athaydes.samples

import com.athaydes.kunion.Union

//sealed class Result2<out B>
//{
//    data class Success<out B>(val b: B) : Result2<B>()
//    data class Failure(val error: Throwable) : Result2<Nothing>()
//}

//sealed class Either<out A, out B>
//{
//    class Left<out A>(a: A) : Either<A, Nothing>()
//    class Right<out B>(b: B) : Either<Nothing, B>()
//}
/*
# Rust
enum Result<T, E> {
   Ok(T),
   Err(E),
}
 */

class User
class Device
class Organization

fun getResourceById(id: String): Union.U3<User, Device, Organization>
{
    // TODO call a data source
    return Union.U3.ofA(User())
}

typealias Either<A, B> = Union.U2<A, B>

// http requests methods
// json values
// error

fun main(args: Array<String>)
{
    // inferred types
    getResourceById("name = 'joe'").use(
            { user -> println("Got a user") },
            { device -> println("It is a device") },
            { org -> println("Org returned") })

    // explicit types
    getResourceById("name = 'joe'").use(
            { user: User -> println("Got a user") },
            { device: Device -> println("It is a device") },
            { org: Organization -> println("Org returned") })

    // Either example
    val joe: Either<String, Int> = Either.ofA("joe")
    val number: Either<String, Int> = Either.ofB(20)

    fun useEither(either: Either<String, Int>)
    {
        val result = either.use({ s -> "String: \"$s\"" }, { i -> "Number: $i" })
        println(result)
    }

    useEither(joe)
    useEither(number)

}