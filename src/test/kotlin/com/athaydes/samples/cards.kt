package com.athaydes.samples


/**
 * This is a sample usage of the KUnion library.
 */
import com.athaydes.kunion.Union
import java.util.Collections

enum class Suit
{
    Spades, Diamonds, Hearts, Clubs
}

enum class Symbol
{
    Ace, Jack, Queen, King
}

typealias Rank = Union.U2<Int, Symbol>

fun rank(number: Int): Rank? = if (number in 1..10) Union.U2.ofA(number) else null

fun rank(symbol: Symbol): Rank = Union.U2.ofB(symbol)


fun numericValueOf(rank: Rank): Int = rank.use(
        { number -> number },
        { rank ->
            when (rank)
            {
                Symbol.Ace   -> 14
                Symbol.Jack  -> 11
                Symbol.Queen -> 12
                Symbol.King  -> 13
            }
        })

fun numericValueOf(suit: Suit): Int = when (suit)
{
    Suit.Spades   -> 10
    Suit.Diamonds -> 9
    Suit.Hearts   -> 8
    Suit.Clubs    -> 7
}

data class Card(val rank: Rank, val suit: Suit) : Comparable<Card>
{

    override fun compareTo(other: Card): Int
    {
        val rankComparison = numericValueOf(this.rank).compareTo(numericValueOf(other.rank))
        return if (rankComparison != 0)
            rankComparison
        else
            numericValueOf(this.suit).compareTo(numericValueOf(other.suit))
    }

}

fun main(args: Array<String>)
{
    val ranks = (1..10).map(::rank).filterNotNull() +
            listOf(Symbol.Ace, Symbol.Jack, Symbol.Queen, Symbol.King).map(::rank)

    val suites = listOf(Suit.Clubs, Suit.Hearts, Suit.Diamonds, Suit.Spades)

    val cards = ranks.flatMap { r -> suites.map { s -> Card(r, s) } }.toMutableList()

    println("Sorted cards:")
    println(cards)

    Collections.shuffle(cards)

    println("Shuffled cards:")
    println(cards)
}