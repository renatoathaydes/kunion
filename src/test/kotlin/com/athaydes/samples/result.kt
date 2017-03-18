package com.athaydes.samples

import com.athaydes.kunion.Union
import java.io.File

/**
 * Result of a computation that may fail.
 */
typealias Result<B> = Union.U2<Throwable, B>

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

fun main(args: Array<String>)
{
    if (args.isEmpty())
    {
        println("Please provide a file as an argument")
    }
    else
    {
        val file = args.first()

        val result = readFileLines(file)

        result.use(
                { error -> println("An error has occurred: $error.") },
                { lines -> println("File $file has ${lines.size} lines.") })

        println("Result is success? " + (result.asInstance().value !is Throwable))
    }
}