package com.athaydes.samples

import com.athaydes.kunion.Union
import org.junit.Test

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

fun getResourceById(id: String): Union.U3<User, Device, Organization>?
{
    // TODO call a data source
    return Union.U3.ofA(User())
}

typealias Resource = Union.U3<User, Device, Organization>

fun getResourceById2(id: String): Resource?
{
    // TODO call a data source
    return Union.U3.ofA(User())
}

typealias Either<A, B> = Union.U2<A, B>

fun overloadExample(s: String): String = s

fun overloadExample(i: Int): Int = i

class ReadmeSamples
{

    @Test
    fun readmeSamples()
    {
        // inferred types
        getResourceById("name = 'joe'")?.use(
                { user -> println("Got a user") },
                { device -> println("It is a device") },
                { org -> println("Org returned") })

        // explicit types
        getResourceById("name = 'joe'")?.use(
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

        // rotate examples

        val a: Union.U2<String, Int> = Union.U2.ofA("a")
        val b: Union.U2<Int, String> = a.rotate()

        val x: Union.U3<Int, Float, Double> = Union.U3.ofC(0.3415)
        val y: Union.U3<Double, Int, Float> = x.rotate()
        val z: Union.U3<Float, Double, Int> = y.rotate()

        // asU2 examples

        val m: Union.U3<String, Int, Double> = Union.U3.ofC(0.3415)
        val n: Union.U2<String, Union.U2<Int, Double>> = m.asU2()

        // asInstance examples
        val i1: Union.U2<String, Int> = Union.U2.ofA("hello")
        val i2: Union.Instance<*> = i1.asInstance()

        // notice that Kotlin can only represent the type of a Union's value as Any?
        val i3: Any? = i2.value

        // if you know the exact type of the Union, then the Instance will have the expected type
        val i4: Union.U4.U4_1<String> = Union.U4.ofA("Hello")
        val i5: String = i4.asInstance().value

        // use examples

        val u: Union.U2<Float, Double> = Union.U2.ofA(0.5f)
        val v1: Double = u.use({ f -> f.toDouble() }, { d -> d })

        // or equivalently, but more verbose
        val v2: Double = when (u)
        {
            is Union.U2.U2_1<Float>  -> u.value.toDouble()
            is Union.U2.U2_2<Double> -> u.value
        }

        // map examples

        val p: Union.U2<Int, Float> = Union.U2.ofA(10)
        val q: Union.U2<Double, Long> = p.map(Int::toDouble, Float::toLong)
    }
}