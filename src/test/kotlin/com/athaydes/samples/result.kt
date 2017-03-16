package com.athaydes.samples

import com.athaydes.kunion.Union
import java.io.File

typealias ErrorMessage = String

/**
 * Result of a computation.
 */
typealias Result<V> = Union.U2<V, ErrorMessage>

fun readFileLines(path: String): Result<List<String>>
{
    val file = File(path)

    // turn a try/catch-based function into a union-type-based one
    return try
    {
        Result.ofA(file.readLines())
    }
    catch (e: Exception)
    {
        Result.ofB(e.message ?: e.toString())
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
                { lines -> println("File $file has ${lines.size} lines.") },
                { error -> println("An error has occurred: $error.") })
    }
}