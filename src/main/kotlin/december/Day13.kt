package december

import AbstractDay
import java.util.*
import kotlin.math.ceil

class Day13 : AbstractDay() {

    private val input = obtainInput()
    private val earliestTime = input[0].toInt()
    private val busIDs = input[1].split(",").map { if (it == "x") -1 else it.toInt() }

    // Part 1: 3035
    override fun part1(): String {
        var bestID = 0
        var bestTime = Int.MAX_VALUE

        for (id in busIDs) {
            if (id == -1) continue
            val firstTime = ceil(earliestTime.toDouble() / id).toInt() * id
            if (firstTime < bestTime) {
                bestID = id
                bestTime = firstTime
            }
        }
        return (bestID * (bestTime - earliestTime)).toString()
    }

    // Part 2: 725169163285238
    override fun part2(): String {
        val checkOrder = mutableListOf<Pair<Int, Int>>()
        for (index in busIDs.indices) {
            if (busIDs[index] == -1) continue
            checkOrder.add(Pair(busIDs[index], index))
        }
        checkOrder.sortByDescending { it.first }

        var t = ceil(100_000_000_000_000.0 / checkOrder[0].first).toLong() * checkOrder[0].first - checkOrder[0].second
        var knownIndex = 1
        search@ while (true) {
            var v = 1
            for (i in 0 until knownIndex) v *= checkOrder[i].first
            t += v

            for (index in knownIndex until checkOrder.size) {
                if ((t + checkOrder[index].second) % checkOrder[index].first != 0L) continue@search
                else if (index < 5) knownIndex = index
            }
            break@search
        }

        return t.toString()
    }

}