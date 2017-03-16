package com.athaydes.samples

import com.athaydes.kunion.Union

/**
 * This is a sample usage of the KUnion library.
 */

/*
// suites
class Spades() {}
class Diamonds() {}
class Hearts() {}
class Clubs() {}

// ranks
class Ace() {}
class Jack() {}
class Queen() {}
class King() {}

alias Rank => Integer|Ace|Jack|Queen|King;
alias Suit => Spades|Diamonds|Hearts|Clubs;

 */


sealed class Suit
{
    object Spades : Suit()
    object Diamonds : Suit()
    object Hearts : Suit()
    object Clubs : Suit()
}

object Ace
object Jack
object Queen
object King


/**
 * Rank is a union because we couldn't make a sealed class containing an integer
 */
typealias Rank = Union.U2<Int, Union.U4<Ace, Jack, Queen, King>>

typealias IntRank = Union.U2.U2_1<Int>
typealias NonIntRank = Union.U2.U2_2<Union.U4<Ace, Jack, Queen, King>>


typealias UAce = Union.U4.U4_1<Ace>
typealias UJack = Union.U4.U4_2<Jack>
typealias UQueen = Union.U4.U4_3<Queen>
typealias UKing = Union.U4.U4_4<King>

fun numericValueOf(rank: Rank): Int = when (rank)
{
    is IntRank    -> rank.value
    is NonIntRank -> when (rank.value)
    {
        is UAce   -> 14
        is UJack  -> 11
        is UQueen -> 12
        is UKing  -> 13
    }
}

fun numericValueOf(suit: Suit): Int = when (suit)
{
    is Suit.Spades   -> 10
    is Suit.Diamonds -> 9
    is Suit.Hearts   -> 8
    is Suit.Clubs    -> 7
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

// TODO
fun main(args: Array<String>)
{
    val cards = (1..10).map {
        val rand = Math.ceil(Math.random() * 15).toInt()
        val rank: Rank = when
        {
            rand < 11 -> IntRank(rand)
            rand < 12 -> NonIntRank(UJack(Jack))
            rand < 13 -> NonIntRank(UQueen(Queen))
            rand < 14 -> NonIntRank(UKing(King))
            else      -> NonIntRank(UAce(Ace))
        }
    }
}