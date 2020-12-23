package december

import AbstractDay

class Day23 : AbstractDay() {

    private val input = obtainInput().first().trim().map { Character.getNumericValue(it) }

    // Part 1: 96342875
    override fun part1(): String = shuffle(100, 9)

    // Part 2: 563362809504
    override fun part2(): String = shuffle(10_000_000, 1_000_000, false)

    private fun initialize(lastCupLabel: Int): Array<Cup> {
        val arr = Array(lastCupLabel + 1) { Cup(it) }
        for (i in 0 until input.size - 1) arr[input[i]].next = arr[input[i + 1]]
        if (arr.size - 1 == input.size) arr[input[input.size - 1]].next = arr[input[0]]
        else {
            arr[input[input.size - 1]].next = arr[input.size + 1]
            for (i in input.size + 1 until lastCupLabel) arr[i].next = arr[i + 1]
            arr[arr.size - 1].next = arr[input[0]]
        }
        return arr
    }

    private fun shuffle(rounds: Int, lastCupLabel: Int, returnPart1: Boolean = true): String {
        val cups = initialize(lastCupLabel)
        var cur = cups[input[0]]
        for (r in 1..rounds) {
            val remove = arrayOf(cur.next!!, cur.next!!.next!!, cur.next!!.next!!.next!!)
            val ids = remove.map { it.id }
            cur.next = cur.next!!.next!!.next!!.next!!
            var dest = cur.id
            do {
                dest--
                if (dest == 0) dest = cups.size - 1
            } while (dest in ids)
            remove[remove.size - 1].next = cups[dest].next!!
            cups[dest].next = remove[0]
            cur = cur.next!!
        }
        return if (returnPart1) order(cups[1], cups.size) else {
            val n = cups[1].next!!
            (n.id.toLong() * n.next!!.id).toString()
        }
    }

    private fun order(c: Cup, size: Int): String {
        var n = c.next!!
        val sb = StringBuilder()
        for (i in 1 until size - 1) {
            sb.append(n)
            n = n.next!!
        }
        return sb.toString()
    }

    class Cup(val id: Int, var next: Cup? = null) {
        override fun toString(): String = id.toString()
    }
}