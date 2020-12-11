package december

import AbstractDay
import java.util.*

class Day9 : AbstractDay() {

    private val input = obtainInput { it.toLong() }

    private var part1Cached: Long = -1

    // Part 1: 32321523
    override fun part1(): String {
        val preambleLen = 25
        val choices = LinkedList<Long>()
        for (i in input.indices) {
            val v = input[i]
            if (i >= preambleLen) {
                val found = run {
                    for (x in 0 until choices.size - 1) {
                        if (choices[x] >= v) continue
                        for (y in x + 1 until choices.size) {
                            if (v == choices[x] + choices[y]) return@run true
                        }
                    }
                    false
                }
                if (!found) {
                    part1Cached = v // Time save used in part 2.
                    return v.toString()
                }
                choices.poll() // Remove oldest
            }
            choices.offer(v) // Add newest
        }
        return "Did not find a weakness in XMAS."
    }

    // Part 2: 4794981
    override fun part2(): String {
        for (l in 0 until input.size - 1) {
            var sum = input[l]
            for (r in l + 1 until input.size) {
                sum += input[r]
                if (sum > part1Cached) break
                if (sum == part1Cached) {
                    val range = input.subList(l, r + 1)
                    return (range.minOrNull()!! + range.maxOrNull()!!).toString()
                }
            }
        }
        return "Did not find a weakness in XMAS."
    }

}