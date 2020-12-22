package december

import AbstractDay
import java.util.*

class Day22 : AbstractDay() {

    private val input = obtainInput()

    private fun setup(): Pair<Deck, Deck> {
        val d1 = Deck()
        val d2 = Deck()
        var currentDeck = d1
        for (line in input) {
            if (line.trim().startsWith("Player")) {
                when {
                    '1' in line -> currentDeck = d1
                    '2' in line -> currentDeck = d2
                }
            } else {
                if (line.isNotBlank()) currentDeck.putBack(Card(line.trim().toInt()))
            }
        }
        return d1 to d2
    }

    private fun play(decks: Pair<Deck, Deck>): Long {
        var counter = 0
        while (round(decks)) counter++
        return if (decks.first.hasNext()) decks.first.score() else decks.second.score()
    }

    private fun round(decks: Pair<Deck, Deck>): Boolean {
        if (!decks.first.hasNext() || !decks.second.hasNext()) return false
        val p1Card = decks.first.draw()
        val p2Card = decks.second.draw()

        if (p1Card > p2Card) {
            decks.first.putBack(p1Card)
            decks.first.putBack(p2Card)
        } else {
            decks.second.putBack(p2Card)
            decks.second.putBack(p1Card)
        }
        return true
    }

    private fun recursivePlay(decks: Pair<Deck, Deck>): Int {
        val previouslySeen: MutableSet<Int> = mutableSetOf()
        while (true) {
            if (recursiveRound(
                    decks,
                    previouslySeen
                ) == 0 || !decks.first.hasNext() || !decks.second.hasNext()
            ) break
        }
        return if (decks.first.hasNext()) 1 else 2
    }

    private fun recursiveRound(decks: Pair<Deck, Deck>, previouslySeen: MutableSet<Int>): Int {
        if (decks.hashCode() in previouslySeen) return 0
        previouslySeen.add(decks.hashCode())
        if (!decks.first.hasNext() || !decks.second.hasNext()) return 0
        val p1Card = decks.first.draw()
        val p2Card = decks.second.draw()

        val winner: Int = if (decks.first.remaining() >= p1Card.value && decks.second.remaining() >= p2Card.value) {
            val decksCopy = decks.first.copy(p1Card.value) to decks.second.copy(p2Card.value)
            recursivePlay(decksCopy)
        } else {
            if (p1Card > p2Card) 1 else 2
        }
        if (winner == 1) {
            decks.first.putBack(p1Card)
            decks.first.putBack(p2Card)
        } else if (winner == 2) {
            decks.second.putBack(p2Card)
            decks.second.putBack(p1Card)
        }
        return winner
    }

    // Part 1: 32179
    override fun part1(): String = play(setup()).toString()

    // Part 2: 30498
    override fun part2(): String {
        val decks = setup()
        return if (recursivePlay(decks) == 1) decks.first.score().toString() else decks.second.score().toString()
    }

    class Card(val value: Int) {
        operator fun compareTo(other: Card): Int = this.value.compareTo(other.value)
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return value == (other as Card).value
        }

        override fun hashCode(): Int = value
        override fun toString(): String = value.toString()
    }

    class Deck(private val cards: LinkedList<Card> = LinkedList()) {
        fun putBack(card: Card) = cards.offer(card)
        fun draw(): Card = cards.poll()
        fun hasNext(): Boolean = !cards.isEmpty()
        fun score(): Long = cards.indices.fold(0) { acc, i -> acc + (i + 1) * cards[cards.size - (i + 1)].value }
        fun remaining(): Int = cards.size
        fun copy(value: Int): Deck {
            val newDeck = Deck()
            for (i in 0 until value) newDeck.putBack(cards[i])
            return newDeck
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return cards == (other as Deck).cards
        }

        override fun hashCode(): Int = cards.hashCode()
        override fun toString(): String = cards.toString()
    }
}